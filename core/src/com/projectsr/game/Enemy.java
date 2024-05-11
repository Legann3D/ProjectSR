package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;


/**
 * Represents an enemy character in the game.
 */
public abstract class Enemy {

    public enum STATE {
        CHASING,
        ATTACKING
    }

    private STATE currentState = STATE.CHASING;

    // Animations
    protected Animation<TextureRegion> walkAnimation;
    protected Animation<TextureRegion> deathAnimation;

    // Rendering
    protected TextureRegion[] walkFrames;
    protected TextureRegion[] deathFrames;
    protected TextureRegion currentFrame;
    Vector2 position;
    float enemyHeight;
    float enemyWidth;
    float speed;

    // Game clock
    protected float stateTime;

    // Collision
    Rectangle bounds = new Rectangle();
    protected AssetManager assetManager;

    /**
     * Initialises the enemy character, setting its initial position and movement speed.
     *
     * @param assetManager manages assets and loads assets used.
     */
    public Enemy(AssetManager assetManager, Vector2 enemySpawnPos) {
        position = enemySpawnPos;
        speed = 50; // Change value as needed
        this.assetManager = assetManager; // Needs to be initialised
    }

    /**
     * Gets enemy assets, and creates animations and sets up initial game state and positioning.
     */
    public abstract void create();

    /**
     * Renders the enemy on the screen with updated animation frames.
     * @param batch The SpriteBatch used for drawing the enemy's current frame.
     */
    public void render(SpriteBatch batch) {

        stateTime += Gdx.graphics.getDeltaTime();
        currentFrame = walkAnimation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, position.x, position.y);
    }

    /**
     * Updates the enemy's position based on its speed and the elapsed time since the last frame.
     * @param f The time in seconds since the last update.
     */
    public void update(float f, Player player) {

        switch(this.currentState) {
            case CHASING:
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

                // Check if the enemy is close enough to attack
//                if (distanceFrom(player) < 200) {
//                    // Set state to attacking
//                    currentState = STATE.ATTACKING;
//                }
                break;

            case ATTACKING:
                // TODO: Check for collision overlapping
//                if () {
//                    player.loseLife(); // TODO: Need method to remove a life from player
//                }
                // Check if the enemy is not in reach to attack
                if (distanceFrom(player) > 200) {
                    // Set state to chasing
                    currentState = STATE.CHASING;
                }
                break;
            default:
                // code block
        }
        updateCollision();
    }

    /**
     * Updates the collision bounds for the enemy
     */
    public abstract void updateCollision();

    /**
     * Disposes of the resources used by the enemy.
     */
    public void dispose() {
        // Dispose of resources
    }

    public float distanceFrom(Player player) {
        return 0;
        //return this.getPosition().dst(player.getPosition()); // TODO: Need player class and methods
    }

//    public Vector2 getPosition() {
//        float currentX = this.position.x + (this.currentFrame.getWidth()  / 2.0f);
//        float currentY = this.position.y + (this.currentFrame.getHeight() / 2.0f);
//        return new Vector2(currentX, currentY);
//    }
}
