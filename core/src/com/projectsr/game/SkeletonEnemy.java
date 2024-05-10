package com.projectsr.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public class SkeletonEnemy extends Enemy {

    private TextureRegion[] attack1Frames;
    private TextureRegion[] attack2Frames;
    protected Animation<TextureRegion> attack1Animation;
    protected Animation<TextureRegion> attack2Animation;

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

        // Get the skeleton assets
        Texture walkSheet = assetManager.get("Enemy/Skeleton/Walk.png", Texture.class);
        Texture attack1Sheet = assetManager.get("Attack1.png", Texture.class);
        Texture attack2Sheet = assetManager.get("Attack2.png", Texture.class);
        Texture deathSheet = assetManager.get("Death.png", Texture.class);

        // Set up the walking frames
        walkFrames = new TextureRegion[4];

        walkFrames[0] = new TextureRegion(walkSheet,0,0,150,150);
        walkFrames[1] = new TextureRegion(walkSheet,150,0,150,150);
        walkFrames[2] = new TextureRegion(walkSheet,300,0,150,150);
        walkFrames[3] = new TextureRegion(walkSheet,450,0,150,150);

        // Create the walking animation at 30 FPS
        walkAnimation = new Animation<>(0.133f, walkFrames);

        // Set up attack 1 frames
        attack1Frames = new TextureRegion[8];

        attack1Frames[0] = new TextureRegion(attack1Sheet,0,0,150,150);
        attack1Frames[1] = new TextureRegion(attack1Sheet,150,0,150,150);
        attack1Frames[2] = new TextureRegion(attack1Sheet,300,0,150,150);
        attack1Frames[3] = new TextureRegion(attack1Sheet,450,0,150,150);
        attack1Frames[4] = new TextureRegion(attack1Sheet,600,0,150,150);
        attack1Frames[5] = new TextureRegion(attack1Sheet,750,0,150,150);
        attack1Frames[6] = new TextureRegion(attack1Sheet,900,0,150,150);
        attack1Frames[7] = new TextureRegion(attack1Sheet,1050,0,150,150);

        // Create the attack1 animation at 30 FPS
        attack1Animation = new Animation<>(0.133f, attack1Frames);

        // Set up attack 2 frames
        attack2Frames = new TextureRegion[8];

        attack2Frames[0] = new TextureRegion(attack2Sheet,0,0,150,150);
        attack2Frames[1] = new TextureRegion(attack2Sheet,150,0,150,150);
        attack2Frames[2] = new TextureRegion(attack2Sheet,300,0,150,150);
        attack2Frames[3] = new TextureRegion(attack2Sheet,450,0,150,150);
        attack2Frames[4] = new TextureRegion(attack2Sheet,600,0,150,150);
        attack2Frames[5] = new TextureRegion(attack2Sheet,750,0,150,150);
        attack2Frames[6] = new TextureRegion(attack2Sheet,900,0,150,150);
        attack2Frames[7] = new TextureRegion(attack2Sheet,1050,0,150,150);

        // Create the walking animation at 30 FPS
        attack2Animation = new Animation<>(0.133f, attack2Frames);

        // Set up death frames
        deathFrames = new TextureRegion[4];

        deathFrames[0] = new TextureRegion(deathSheet,0,0,150,150);
        deathFrames[1] = new TextureRegion(deathSheet,150,0,150,150);
        deathFrames[2] = new TextureRegion(deathSheet,300,0,150,150);
        deathFrames[3] = new TextureRegion(deathSheet,450,0,150,150);

        // Create the walking animation at 30 FPS
        deathAnimation = new Animation<>(0.133f, deathFrames);
    }

    /**
     * Updates the collision bounds for the enemy
     */
    @Override
    public void updateCollision() {

    }
}
