package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Iterator;


/**
 * Represents an enemy character in the game.
 */
public abstract class Enemy {

    public enum STATE {
        CHASING,
        ATTACKING,
        DEATH
    }

    protected GameScreen gameScreen;

    protected STATE currentState = STATE.CHASING;
    protected AssetManager assetManager;

    // Animations
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> deathAnimation;
    protected Animation<TextureRegion> currentAnimation;

    // Rendering
    protected TextureRegion[] walkFrames;
    protected TextureRegion[] deathFrames;
    protected TextureRegion currentFrame;
    Vector2 position;
    private float enemyHeight = 150;
    private float enemyWidth = 150;
    private float speed;

    // Game clock
    protected float stateTime;

    // Stats
    protected float health;

    // Collision
    protected Body body;
    public static final float PPM = 100.0f; // Pixels Per Meter conversion

    /**
     * Initialises the enemy character, setting its initial position and movement speed.
     *
     * @param assetManager manages assets and loads assets used.
     */
    public Enemy(AssetManager assetManager, Vector2 enemySpawnPos, float health, World world) {
        position = enemySpawnPos;
        speed = 50; // Change value as needed
        this.assetManager = assetManager;
        this.health = health;
        createCollisionBody(world, enemySpawnPos);
        logPositions("Constructor");
    }

    public void logPositions(String tag) {
        System.out.println(tag + " - Enemy position: " + position.toString());
        System.out.println(tag + " - Body position: " + body.getPosition().toString());
    }

    /**
     * Gets enemy assets, and creates animations and sets up initial game state and positioning.
     */
    public abstract void create();

    public void createCollisionBody(World world, Vector2 enemySpawnPos) {

        Vector2 physicsSpawnPos = new Vector2(enemySpawnPos.x / PPM, enemySpawnPos.y / PPM);

        // Initialise the collision body
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(position.x - 880, position.y - 460);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(enemyWidth / 4, enemyHeight / 4); // Set size

        // Initialise the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        body.createFixture(fixtureDef);
        shape.dispose();

        logPositions("createCollisionBody");
    }

    /**
     * Renders the enemy on the screen with updated animation frames.
     * @param batch The SpriteBatch used for drawing the enemy's current frame.
     */
    public void render(SpriteBatch batch) {

        stateTime += Gdx.graphics.getDeltaTime();
        // Prevent death animation from looping
        if (currentState == STATE.DEATH) {
            currentFrame = currentAnimation.getKeyFrame(stateTime, false);
        }
        else {
            currentFrame = currentAnimation.getKeyFrame(stateTime, true);
        }
        batch.draw(currentFrame, position.x, position.y);
    }

    /**
     * Updates the enemy's position based on its speed and the elapsed time since the last frame.
     * @param f The time in seconds since the last update.
     */
    public abstract void update(float f, Player player, Iterator<Enemy> enemyIter);

    public void setState(STATE state) {
        currentState = state;
    }

    public void flipEnemy(Player player) {

        float PosX = (player.position.x + (player.width / 2)) - (this.position.x + (this.enemyWidth / 2));
        boolean shouldFaceLeft = PosX < -7;
        boolean shouldFaceRight = PosX > 7;

        // TODO: Fix flutter effect when flipping
        // Ensure a frame exists
        if (currentFrame != null) {
            // Check if the texture needs to be flipped
            if (currentFrame.isFlipX() && shouldFaceRight) {
                currentFrame.flip(true, false);
            }
            else if (!currentFrame.isFlipX() && shouldFaceLeft) {
                currentFrame.flip(true, false);
            }
        }
    }

    public void chasePlayer(float f, Player player) {

        // Calculate the direction vector from enemy to player
        float directionX = (player.position.x - (player.width / 2 - 20)) - this.position.x;
        float directionY = (player.position.y - (player.height / 2 + 10)) - this.position.y;

        // Calculate the distance
        float distance = (float) Math.sqrt(directionX * directionX + directionY * directionY);

        // Normalise the vector (convert to length of 1)
        float normalisedX = directionX / distance;
        float normalisedY = directionY / distance;

        // Scale the normalised vector by the speed
        this.position.x += normalisedX * this.speed * f;
        this.position.y += normalisedY * this.speed * f;

        logPositions("chasePlayer");
    }

    /**
     * Disposes of the resources used by the enemy.
     */
    public void dispose() {
        // Dispose of resources
        assetManager.dispose();
    }

    /**
     * Gets the distance from the player to the enemy using vector2 positions.
     * Position is calculated from the centre of the sprite.
     *
     * @param player The player character
     * @return A float value of the distance between the enemy and player.
     */
    public float distanceFrom(Player player) {
        // Get the centre of the player
        Vector2 playerPos = new Vector2(
                player.position.x + (player.width / 2),
                player.position.y + (player.height / 2)
        );
        // Get the centre of the enemy
        Vector2 enemyPos = new Vector2(
                this.position.x + (this.enemyWidth / 2),
                this.position.y + (this.enemyHeight / 2)
        );
        return enemyPos.dst(playerPos);
    }

    public float getHealth() {
        return health;
    }

    public void takeDamage(float damage) {
        health -= damage;
    }

    public void setCurrentState(String state) {
        if (state.equals("CHASING")) {
            currentState = STATE.CHASING;
        }
        else if (state.equals("ATTACKING")) {
            currentState = STATE.ATTACKING;
        }
        else if (state.equals("DEATH")) {
            stateTime = 0;
            currentState = STATE.DEATH;
        }
    }

    public String getCurrentState() {
        if (currentState == STATE.CHASING) {
            return "CHASING";
        }
        else if (currentState == STATE.ATTACKING) {
            return "ATTACKING";
        }
        else if (currentState == STATE.DEATH) {
            return "DEATH";
        }
        return "CHASING";
    }

    public void enemyDeath(Iterator<Enemy> enemyIter) {
        enemyIter.remove();
    }
}
