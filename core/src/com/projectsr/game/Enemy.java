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

    STATE currentState;

    // Animations
    protected Animation<TextureRegion> animation;

    // Rendering
    protected TextureRegion[] frames;
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
    public Enemy(AssetManager assetManager) {
        position = new Vector2(0, 0);
        speed = -200; // Change value as needed
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
        currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, enemyWidth, enemyHeight);
    }

    /**
     * Updates the enemy's position based on its speed and the elapsed time since the last frame.
     * @param f The time in seconds since the last update.
     */
    public void update(float f, Player player) {

        switch(this.currentState) {
            case CHASING:
                // TODO: Implement chasing
                // Check if the player is higher or lower than the enemy
                if (this.getPosition().y < player.getPosition().y)
                    // Move down
                    this.position.y += this.speed * f * 4;
                if (this.getPosition().y > player.getPosition().y)
                    // Move up
                    this.position.y -= this.speed * f * 4;
                // Check if the player is left or right of the enemy
                if (this.getPosition().x < player.getPosition().x)
                    // Move right
                    this.position.x += this.speed * f * 4;
                if (this.getPosition().x > player.getPosition().x)
                    // Move left
                    this.position.x -= this.speed * f * 4;

                // Check if the enemy is close enough to attack
                if (distanceFrom(player) < 200) {
                    // Set state to attacking
                    currentState = STATE.ATTACKING;
                }
                break;

            case ATTACKING:
                // TODO: Implement attacking
                break;
            default:
                // code block
        }
        position.x += speed * f;
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
        return this.getPosition().dst(player.getPosition()); // TODO: Need player class and methods
    }

    public Vector2 getPosition() {
        float currentX = this.position.x + (this.currentFrame.getWidth()  / 2.0f);
        float currentY = this.position.y + (this.currentFrame.getHeight() / 2.0f);
        return new Vector2(currentX, currentY);
    }
}
