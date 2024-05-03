package com.projectsr.game;

import com.badlogic.gdx.assets.AssetManager;


public class SkeletonEnemy extends Enemy {

    /**
     * Initialises the enemy character, setting its initial position and movement speed.
     *
     * @param assetManager manages assets and loads assets used.
     */
    public SkeletonEnemy(AssetManager assetManager) {
        super(assetManager);
    }

    /**
     * Gets enemy assets, and creates animations and sets up initial game state and positioning.
     */
    @Override
    public void create() {

    }

    /**
     * Updates the collision bounds for the enemy
     */
    @Override
    public void updateCollision() {

    }
}
