package com.projectsr.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Iterator;


public class SkeletonEnemy extends Enemy {

    private TextureRegion[] attack1Frames;
    private TextureRegion[] attack2Frames;
    protected Animation<TextureRegion> attack1Animation;
    protected Animation<TextureRegion> attack2Animation;
    private Sound attackSound;
    private Sound takeDamageSound;
    private Sound deathSound;

    /**
     * Initialises the enemy character, setting its initial position and movement speed.
     *
     * @param assetManager manages assets and loads assets used.
     */
    public SkeletonEnemy(AssetManager assetManager, Vector2 enemySpawnPos, float health, World world, GameScreen gameScreen) {
        super(assetManager, enemySpawnPos, health, world, gameScreen);
    }

    /**
     * Gets enemy assets, and creates animations and sets up initial game state and positioning.
     */
    @Override
    public void create() {

        // Get the skeleton assets
        Texture walkSheet = assetManager.get("Enemy/Skeleton/Walk.png", Texture.class);
        Texture attack1Sheet = assetManager.get("Enemy/Skeleton/Attack.png", Texture.class);
        //Texture attack2Sheet = assetManager.get("Attack2.png", Texture.class);
        Texture deathSheet = assetManager.get("Enemy/Skeleton/Death.png", Texture.class);

        attackSound = assetManager.get("EnemyAudio/skeletonAttack.wav", Sound.class);
        takeDamageSound = assetManager.get("EnemyAudio/skeletonTakeHit.mp3", Sound.class);
        deathSound = assetManager.get("EnemyAudio/skeletonDie.mp3", Sound.class);

        // Set up the walking frames
        walkFrames = new TextureRegion[4];

        walkFrames[0] = new TextureRegion(walkSheet, 0, 0, 150, 150);
        walkFrames[1] = new TextureRegion(walkSheet, 150, 0, 150, 150);
        walkFrames[2] = new TextureRegion(walkSheet, 300, 0, 150, 150);
        walkFrames[3] = new TextureRegion(walkSheet, 450, 0, 150, 150);

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
        //attack2Frames = new TextureRegion[8];

        //attack2Frames[0] = new TextureRegion(attack2Sheet,0,0,150,150);
        //attack2Frames[1] = new TextureRegion(attack2Sheet,150,0,150,150);
        //attack2Frames[2] = new TextureRegion(attack2Sheet,300,0,150,150);
        //attack2Frames[3] = new TextureRegion(attack2Sheet,450,0,150,150);
        //attack2Frames[4] = new TextureRegion(attack2Sheet,600,0,150,150);
        //attack2Frames[5] = new TextureRegion(attack2Sheet,750,0,150,150);
        //attack2Frames[6] = new TextureRegion(attack2Sheet,900,0,150,150);
        //attack2Frames[7] = new TextureRegion(attack2Sheet,1050,0,150,150);

        // Create the walking animation at 30 FPS
        //attack2Animation = new Animation<>(0.133f, attack2Frames);

        // Set up death frames
        deathFrames = new TextureRegion[4];

        deathFrames[0] = new TextureRegion(deathSheet,0,0,150,150);
        deathFrames[1] = new TextureRegion(deathSheet,150,0,150,150);
        deathFrames[2] = new TextureRegion(deathSheet,300,0,150,150);
        deathFrames[3] = new TextureRegion(deathSheet,450,0,150,150);

        // Create the walking animation at 30 FPS
        deathAnimation = new Animation<>(0.133f, deathFrames);
    }

    public void update(float f, Player player, Iterator<Enemy> enemyIter) {

        // Check if the enemy still has health

        switch (this.currentState) {
            case CHASING:
                fixtureDef.isSensor = false;
                attackFixtureDef.isSensor = true;

                currentAnimation = walkAnimation;

                flipEnemy(player);
                chasePlayer(f, player);

                // Check for collisions and apply repelling force
                applyRepellingForce();

                Vector2 desiredPosition = new Vector2(this.position.x + 74, this.position.y + 75);
                body.setTransform(desiredPosition, body.getAngle()); // Update main body collision

                // Check if the enemy is close enough to attack
                if (distanceFrom(player) < 25) {
                    // Set state to attacking
                    setState(STATE.ATTACKING);
                    stateTime = 0;
                    attackSoundPlayed = false;
                }
                break;
            case ATTACKING:
                currentAnimation = attack1Animation;

                flipEnemy(player);
                applyRepellingForce();

                // Check if the sound has already played for this attack
                if (!attackSoundPlayed) {
                    attackSound.play();
                    attackSoundPlayed = true;
                }

                // Reset attack sound boolean after the attack animation completes
                if (currentAnimation.isAnimationFinished(stateTime)) {
                    attackSoundPlayed = false;
                    stateTime = 0;
                }

                // TODO: Double check collisions work as expected when player can take dmg
                // TODO: Remove this stuff if it doesn't or becomes cumbersome
                // Check if the animation is almost completed
                if (currentAnimation.getKeyFrameIndex(f) > currentAnimation.getKeyFrameIndex(f) % 6) {
                    // Enable and disable collision accordingly
                    fixtureDef.isSensor = true;
                    attackFixtureDef.isSensor = false;
                }
                else {
                    fixtureDef.isSensor = false;
                    attackFixtureDef.isSensor = true;
                }

                desiredPosition = new Vector2(this.position.x + 70, this.position.y + 75);
                attackBody.setTransform(desiredPosition, attackBody.getAngle()); // Update attack collision

                // TODO: Check for collision overlapping
//              if () {
                    // Might be a good idea to have a counter of 3 in the player before taking a life
                    // Overlap should not be checked here - maybe in GameScreen or Player class
//                  player.loseLife(); // TODO: Need method to remove a life from player
//              }
                // Check if the enemy is not in reach to attack
                if (distanceFrom(player) > 50) {
                    // Set state to chasing
                    setState(STATE.CHASING);

                }
                break;
            case DEATH:
                currentAnimation = deathAnimation;

                if (!deathSoundPlayed) {
                    deathSound.play();
                    deathSoundPlayed = true;
                }

                if (currentAnimation.isAnimationFinished(f)) {
                    world.destroyBody(this.body); // Dispose of collision
                    world.destroyBody(this.attackBody);
                    spawnEssence();
                    enemyDeath(enemyIter);
                }
                break;
            default:
                // code block
        }
    }

    private void applyRepellingForce() {

        // Loop over the contacted enemies
        for (Contact contact : world.getContactList()) {

            // Check if the contacted enemies are touching
            if (contact.isTouching()) {

                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                System.out.println("Enemies touching");

                // Check if the both fixtures colliding are enemies
                if (GameContactListener.isEnemyFixture(fixtureA) && GameContactListener.isEnemyFixture(fixtureB)) {

                    Body bodyA = fixtureA.getBody();
                    Body bodyB = fixtureB.getBody();

                    Enemy enemyA = (Enemy) bodyA.getUserData();
                    Enemy enemyB = (Enemy) bodyB.getUserData();

                    Vector2 positionA = bodyA.getPosition();
                    Vector2 positionB = bodyB.getPosition();

                    Vector2 repellingForce = new Vector2(positionA).sub(positionB).nor().scl(1f); // Adjust scalar for velocity

                    // Apply the force to the enemy positions
                    enemyA.position.add(repellingForce.scl(0.1f));
                    enemyB.position.add(repellingForce.scl(-0.1f));

                    System.out.println("Repelling force applied between enemies");
                }
            }
        }
    }

    @Override
    public void takeDamage(float damage) {
        if (this.currentState != STATE.DEATH && !takeDamageSoundPlayed) {
            health -= damage;
            takeDamageSound.play();
            takeDamageSoundPlayed = true;
        }
    }
}
