package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;

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
    protected Viewport viewport;

    /**
     * Initialises the enemy character, setting its initial position and movement speed.
     */
    public Enemy(AssetManager assetManager, Viewport viewport) {
        position = new Vector2(viewport.getWorldWidth(), viewport.getWorldHeight());
        speed = -200; // Change value as needed
        this.assetManager = assetManager; // Needs to be initialised
        this.viewport = viewport;
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

        switch(this.currentState) {
            case CHASING:
                // TODO: Implement chasing
                break;
            case ATTACKING:
                // TODO: Implement attacking
                break;
            default:
                // code block
        }

        currentFrame = animation.getKeyFrame(stateTime, true);
        batch.draw(currentFrame, enemyWidth, enemyHeight);
    }

    /**
     * Updates the enemy's position based on its speed and the elapsed time since the last frame.
     * @param f The time in seconds since the last update.
     */
    public void update(float f) {
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
}
