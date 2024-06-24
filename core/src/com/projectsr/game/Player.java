package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import java.util.List;

public class Player {
    Vector2 position;
    Vector2 velocity;
    float width = 100;
    float height = 55;
    float speed = 2;

    TextureRegion[][] animations;
    int[] frameCounts;
    float frame = 0;

    boolean facingRight = true;

    public OrthographicCamera camera;

    enum State {
        IDLE, WALKING, ATTACKING, HURT
    }
    State currentState = State.IDLE;

    private Vector2 joystickOrigin;
    private Vector2 joystickCurrent;
    private float joystickRadius;
    private float deadZoneRadius;

    private int maxHeart = 3;
    private int currentHeart;
    private int attackBaseDamage = 10;
    private int damageModifier;
    private float attackBaseRange = 80;

    private int greenEsences;
    private int redEssences;

    private int enemiesKilled;


    public Player() {

        position = new Vector2(880, 500);
        velocity = new Vector2(0,0);

        // initialise camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / h * 250, 250);

        // initialise animations and frame counts
        animations = new TextureRegion[4][];
        frameCounts = new int[4];

        //loading idle animation
        frameCounts[0] = 8;
        animations[0] = new TextureRegion[frameCounts[0]];
        for (int i = 0; i < frameCounts[0]; i++) {
            animations[0][i] = new TextureRegion(new Texture("Character/HeroKnight/Idle/HeroKnight_Idle_" + i + ".png"));
        }

        // loading walking animation
        frameCounts[1] = 10;
        animations[1] = new TextureRegion[frameCounts[1]];
        for (int i = 0; i < frameCounts[1]; i++) {
            animations[1][i] = new TextureRegion(new Texture("Character/HeroKnight/Run/HeroKnight_Run_" + i + ".png"));
        }

        // loading attacking animation
        frameCounts[2] = 6;
        animations[2] = new TextureRegion[frameCounts[2]];
        for (int i = 0; i < frameCounts[2]; i++) {
            animations[2][i] = new TextureRegion(new Texture("Character/HeroKnight/Attack1/HeroKnight_Attack1_" + i + ".png"));
        }

        //loading hurt animation
        frameCounts[3] = 3;
        animations[3] = new TextureRegion[frameCounts[3]];
        for (int i = 0; i < frameCounts[3]; i++) {
            animations[3][i] = new TextureRegion(new Texture("Character/HeroKnight/Hurt/HeroKnight_Hurt_" + i + ".png"));
        }

        // initialise joystick variables
        joystickOrigin = new Vector2();
        joystickCurrent = new Vector2();
        joystickRadius = 70;
        deadZoneRadius = 50;

        // initialise heart
        currentHeart = maxHeart;

    }

    public void update(float deltaTime, List<Enemy> enemies) {
        controls();
        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

        // Nearest enemy withing attack range in front of player
        Enemy nearestEnemy = findNearestEnemyInFront(enemies);
        if (nearestEnemy != null) {
            currentState = State.ATTACKING;
            attack(nearestEnemy);
        }
        // update animation frame
        this.frame += 20 * deltaTime;
        if (this.frame >= frameCounts[currentState.ordinal()]) {
            this.frame = 0;

            if (nearestEnemy != null) {
                nearestEnemy.setTakeDamageSoundPlayed(false);
            }
        }

        // update camera position
        camera.position.set(position.x + width / 2, position.y + height / 2, 0);
        camera.update();


    }

    public void controls() {
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());


            // If this is the first touch, set the joystick origin
            if (joystickOrigin.isZero()) {
                joystickOrigin.set(touchPos);
            }

            joystickCurrent.set(touchPos);
            Vector2 direction = joystickCurrent.cpy().sub(joystickOrigin).nor();
            float physicalTouchDistance = joystickCurrent.dst(joystickOrigin);

            // Check if the touch is within the dead zone
            if (physicalTouchDistance < deadZoneRadius) {
                velocity.set(0, 0);
                currentState = State.IDLE;
                return;
            }

            // Check if the touch distance is greater than joystick radius:
            if (physicalTouchDistance > joystickRadius) {
                // limits the joystick to the specified radius
                joystickCurrent.set(joystickOrigin.cpy().add(direction.scl(joystickRadius)));
                physicalTouchDistance = joystickRadius;
            }

            velocity.set(direction.scl(speed * (physicalTouchDistance / joystickRadius)));

            // check if the player is moving to the left or right
            if (velocity.x > 0) {
                facingRight = true;
            } else if (velocity.x < 0) {
                facingRight = false;
            }

            // check to see if th player is moving
            if (velocity.len() > 0) {
                currentState = State.WALKING;
            } else {
                currentState = State.IDLE;
            }


        } else {
            // Reset joystick origin when touch is released
            joystickOrigin.setZero();
            velocity.set(0, 0);
            currentState = State.IDLE;
        }
    }

    public void render (SpriteBatch batch) {
        batch.setProjectionMatrix(camera.combined);
        TextureRegion[] currentTexture = animations[currentState.ordinal()];
        TextureRegion frameTexture = currentTexture[(int)this.frame];

        // Flip the texture if needed
        if (facingRight && frameTexture.isFlipX()) {
            frameTexture.flip(true, false);
        } else if (!facingRight && !frameTexture.isFlipX()) {
            frameTexture.flip(true, false);
        }

        batch.draw(frameTexture, position.x, position.y, width, height);
    }

    // This section tracks the health for the player
    public void takeDamage(int amount){
        currentHeart -= amount;
        if (currentHeart < 0 ) {
            currentHeart = 0;
        }
        currentState = State.HURT;
    }
    public void heal(int amount){
        currentHeart += amount;
        if (currentHeart > maxHeart) {
            currentHeart = maxHeart;
        }
    }
    public int getCurrentHeart() {
        return currentHeart;
    }
    public int getMaxHeart() {
        return  maxHeart;
    }

    // This section keeps track of the players essence
    public void removeEssence(int amount, String essence){
        if (essence == "red"){
            redEssences -= amount;
            if (redEssences < 0 ) {
                redEssences = 0;
            }
        }

        if (essence == "green"){
            greenEsences -= amount;
            if (greenEsences < 0 ) {
                greenEsences = 0;
            }
        }
    }
    public void addEssence(int amount, String essence){
        if (essence == "red"){
            redEssences += amount;
        }

        if (essence == "green"){
            greenEsences += amount;
        }
    }
    public int getEssences(String essence) {
        if (essence == "red"){
            return redEssences;
        }
        else if (essence == "green"){
            return greenEsences;
        }
        else{
            return 0;
        }

    }

    // Finding the nearest enemy in front of the player to be used for auto attacking them
    public Enemy findNearestEnemyInFront(List<Enemy> enemies) {
        Enemy nearestEnemy = null;

        for (Enemy enemy : enemies) {
            float distance = position.dst(enemy.position);
            boolean isInFront = (facingRight && enemy.position.x > position.x) ||
                    (!facingRight && enemy.position.x < position.x);

            if (distance <= attackBaseRange && isInFront) {
                nearestEnemy = enemy;
            }
        }
        return nearestEnemy;
    }
    public void attack(Enemy enemy){
        enemy.takeDamage(attackBaseDamage + damageModifier);

        if (enemy.getCurrentState().equals("DEATH")){
            enemiesKilled++;
        }
    }
    public void setDamageModifier(int amount) {
        this.damageModifier = amount;
    }
    public int getDamageModifier() {
        return damageModifier;
    }

    public int getEnemiesKilled(){
        return enemiesKilled;
    }

    public void dispose(){
        for (TextureRegion[] animation : animations) {
            for (TextureRegion texRegion : animation) {
                texRegion.getTexture().dispose();
            }
        }
    }

}
