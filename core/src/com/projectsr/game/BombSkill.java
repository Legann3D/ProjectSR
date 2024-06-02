package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

public class BombSkill {

    private Vector2 position;
    private AssetManager assetManager;
    private float baseDamage = 100f;
    private float explosionRadius = 100f; // pixels
    private float lifetimeTick = 0f;
    private float stateTime;

    private Texture bomb;
    private TextureRegion[] explosionFrames;
    private TextureRegion currentFrame;
    private Animation<TextureRegion> explosionAnimation;
    private boolean isExploding = false;


    public BombSkill(AssetManager assetManager, Vector2 position) {
        this.assetManager = assetManager;
        this.position = position;
    }

    public void create() {

        bomb = assetManager.get("Skills/bomb.png", Texture.class);
        Texture explosionSheet = assetManager.get("Skills/explosion.png", Texture.class);

        // Set up the walking frames
        explosionFrames = new TextureRegion[9];

        explosionFrames[0] = new TextureRegion(explosionSheet, 0, 0, 32, 32);
        explosionFrames[1] = new TextureRegion(explosionSheet, 32, 0, 32, 32);
        explosionFrames[2] = new TextureRegion(explosionSheet, 64, 0, 32, 32);
        explosionFrames[3] = new TextureRegion(explosionSheet, 96, 0, 32, 32);
        explosionFrames[4] = new TextureRegion(explosionSheet, 128, 0, 32, 32);
        explosionFrames[5] = new TextureRegion(explosionSheet, 160, 0, 32, 32);
        explosionFrames[6] = new TextureRegion(explosionSheet, 192, 0, 32, 32);
        explosionFrames[7] = new TextureRegion(explosionSheet, 224, 0, 32, 32);
        explosionFrames[8] = new TextureRegion(explosionSheet, 256, 0, 32, 32);

        // Create explosion animation at 30 FPS
        explosionAnimation = new Animation<>(0.133f, explosionFrames);
    }

    public void render(SpriteBatch batch, float f) {

        stateTime += Gdx.graphics.getDeltaTime();

        if (isExploding) {
            System.out.println("Rendering explosion");
            currentFrame = explosionAnimation.getKeyFrame(f, false);
            // Render the explosion
        }
        //else if (!explosionAnimation.isAnimationFinished(stateTime) ) {
            // Render the bomb
            //batch.draw(bomb, position.x, position.y, 25, 37.5f);
        //}
        batch.draw(currentFrame, position.x, position.y, 50, 50);
    }

    public void update(float f, ArrayList<Enemy> enemies) {
        tickUntilExplosion(f, enemies);
    }

    public void tickUntilExplosion(float f, ArrayList<Enemy> enemies) {

        if (lifetimeTick >= 3f && !isExploding) {
            // Set bomb to explode
            explodeBomb(enemies);
            lifetimeTick = 0;
        }
        else {
            lifetimeTick += f;
        }
    }

    public void explodeBomb(ArrayList<Enemy> enemies) {

        isExploding = true;

        // Check which enemies are within the explosion radius
        for (Enemy enemy : enemies) {
            if (isWithinExplosionRadius(enemy)) {
                dealDamage(enemy);
            }
        }
    }

    private boolean isWithinExplosionRadius(Enemy enemy) {

        // Check the distance between the enemy and the bomb
        float distance = position.dst(enemy.getPosition());

        return distance <= explosionRadius;
    }

    public void dealDamage(Enemy enemy) {

        // For now, we'll simulate the bonus damage, which is usually the player upgrade bonus damage
        float bonusDamage = baseDamage + (baseDamage * 0.10f);

        float totalDamage = baseDamage += bonusDamage;

        // Cause enemy to take damage if in radius
        enemy.takeDamage(totalDamage);
    }
}