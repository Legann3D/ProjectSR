package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

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
        IDLE, WALKING, ATTACKING, HURT, DEATH, BERSERK
    }
    State currentState = State.IDLE;

    private Vector2 joystickOrigin;
    private Vector2 joystickCurrent;
    private float joystickRadius;
    private float deadZoneRadius;

    private int maxHeart = 3;
    private int currentHeart;
    private int attackBaseDamage = 10;
    private int damageModifier = 0;

    private int greenEssences;
    private int redEssences;
    private int enemiesKilled;

    private World world;
    private Body body;
    private Body attackBody;
    private FixtureDef fixtureDef;
    private FixtureDef attackFixtureDef;

    private int enemiesAttackedInTimeFrame = 0;
    private float attackTimeFrame = 5.0f;
    private float elapsedTime = 0.0f;
    private int attackThreshold = 5; // Number of enemies to attack within the time frame to get a damage modifier
    private boolean isBerserk = false;
    private float berserkDuration = 10.0f;
    private float berserkTimer = 0.0f;


    public Player(World world) {

        this.world = world;
        position = new Vector2(880, 500);
        velocity = new Vector2(0,0);

        // initialise camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / h * 250, 250);

        // initialise animations and frame counts
        animations = new TextureRegion[6][];
        frameCounts = new int[6];

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

        // loading death animation
        frameCounts[4] = 9;
        animations[4] = new TextureRegion[frameCounts[4]];
        for (int i = 0; i < frameCounts[4]; i++) {
            animations[4][i] = new TextureRegion(new Texture("Character/HeroKnight/Death/HeroKnight_Death_" + i + ".png"));
        }

        // loading berserk animation
        frameCounts[5] = 5;
        animations[5] = new TextureRegion[frameCounts[5]];
        for (int i = 0; i < frameCounts[5]; i++) {
            animations[5][i] = new TextureRegion(new Texture("Character/HeroKnight/Berserk/HeroKnight_Berserk_" + i + ".png"));
        }

        // initialise joystick variables
        joystickOrigin = new Vector2();
        joystickCurrent = new Vector2();
        joystickRadius = 70;
        deadZoneRadius = 50;

        // initialise heart
        currentHeart = maxHeart;

        // crating the box2d body
        createCollisionBody();

    }

    private void createCollisionBody() {

        // initialise the collision body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x + width / 2, position.y + height /2);
        bodyDef.fixedRotation = true;
        body = world.createBody(bodyDef);

        // sets the size
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 7.5f, height / 3f);

        // initialise the fixture
        fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1.0f;
        fixtureDef.friction = 0.3f;
        fixtureDef.filter.categoryBits = GameContactListener.PLAYER_CATEGORY;
        fixtureDef.filter.maskBits = GameContactListener.ENEMY_CATEGORY | GameContactListener.ENEMY_ATTACK_CATEGORY | GameContactListener.ESSENCE_CATEGORY;

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);

        // initialise the attacking collision body
        BodyDef attackBodyDef = new BodyDef();
        attackBodyDef.type = BodyDef.BodyType.DynamicBody;
        attackBodyDef.position.set(position.x + width / 2, position.y + height / 2);
        attackBodyDef.fixedRotation = true;
        attackBody = world.createBody(attackBodyDef);

        PolygonShape attackShape = new PolygonShape();
        attackShape.setAsBox(width / 4, height / 2);

        // initialise the fixture
        attackFixtureDef = new FixtureDef();
        attackFixtureDef.shape = attackShape;
        attackFixtureDef.density = 1.0f;
        attackFixtureDef.friction = 0.3f;
        attackFixtureDef.filter.categoryBits = GameContactListener.PLAYER_ATTACK_CATEGORY;
        attackFixtureDef.filter.maskBits = GameContactListener.ENEMY_CATEGORY;

        attackBody.createFixture(attackFixtureDef);
        attackShape.dispose();

        attackBody.setUserData(this);

    }

    public void update(float deltaTime, List<Enemy> enemies, List<Essence> essences) {
        controls();
        position.set(body.getPosition().x - width / 2, body.getPosition().y - height / 2);
        body.setLinearVelocity(velocity);

        // Update the elapsed time
        elapsedTime += deltaTime;

        // Reset the count if the time frame expires
        if (elapsedTime >= attackTimeFrame) {
            enemiesAttackedInTimeFrame = 0;
            elapsedTime = 0.0f;

        }

        // update the berserk timer when in berserk mode
        if (isBerserk) {
            berserkTimer += deltaTime;
            if (berserkTimer >= berserkDuration) {
                // reset the damage modifier and berserk mode after the duration
                damageModifier = 0;
                isBerserk = false;
                currentState = State.IDLE;
                berserkTimer = 0.0f;
            }
        }

        // determines if the attack body in front of character based on where the character is facing
        float attackBodyX;
        if (facingRight) {
            attackBodyX = position.x + width;
        } else {
            attackBodyX = position.x - width / 100;
        }
        float attackBodyY = position.y + height / 2;

        attackBody.setTransform(attackBodyX, attackBodyY, 0);

        // checking for collision with the enemy
        checkCollision(enemies, essences);

        // update animation frame
        this.frame += 20 * deltaTime;
        if (this.frame >= frameCounts[currentState.ordinal()]) {
            this.frame = 0;
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

                // check if berserk is on or off
                if (!isBerserk) {
                    currentState = State.IDLE;
                }
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
            if (velocity.len() > 0 && !isBerserk) {
                currentState = State.WALKING;
            } else if (!isBerserk) {
                currentState = State.IDLE;
            }

        } else {
            // Reset joystick origin when touch is released
            joystickOrigin.setZero();
            velocity.set(0, 0);
            if (!isBerserk) {
                currentState = State.IDLE;
            }
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
            currentState = State.DEATH;
        }
        currentState = State.HURT;
    }
    public void heal(int amount){
        currentHeart += amount;
        if (currentHeart > maxHeart) {
            currentHeart = maxHeart;
        }
    }
    public void addMaxHeart(int amount){
        maxHeart += amount;

    }
    public void removerMaxHeart(int amount){
        maxHeart -= amount;
        if (maxHeart <= 0){
            maxHeart = 0;
            currentState = State.DEATH;
        }
    }
    public int getCurrentHeart() {
        return currentHeart;
    }
    public int getMaxHeart() {
        return  maxHeart;
    }

    // This section keeps track of the players essence

    public void addEssence(int amount, Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                redEssences += amount;
                break;

            case GREEN:
                greenEssences += amount;
                break;
        }
    }
    public void removeEssence(int amount, Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                redEssences -= amount;
                if (redEssences < 0) {
                    redEssences = 0;
                }
                break;

            case GREEN:
                greenEssences -= amount;
                if (greenEssences < 0) {
                    greenEssences = 0;
                }
                break;
        }
    }
    public int getEssences(Essence.Type essenceType) {
        switch (essenceType) {
            case RED:
                return redEssences;

            case GREEN:
                return greenEssences;

            default:
                return 0;
        }
    }


    public void attack(Enemy enemy){
        if (!isBerserk) {
            currentState = State.ATTACKING;
        }
        enemy.takeDamage(attackBaseDamage + damageModifier);

        if (enemy.getCurrentState().equals("DEATH")){
            enemiesKilled++;
            //  increment the count of enemies attacked in time frame
            enemiesAttackedInTimeFrame++;
        }

        elapsedTime = 0.0f;

        // check if threshold is met for applying the damage modifier
        checkAndApplyDamageModifier();
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

    public void checkCollision(List<Enemy> enemies, List<Essence> essences) {
        for (Enemy enemy : enemies) {

            // check if the player's body is colliding with the enemy's attackbody
            if (body.getFixtureList().first().testPoint(enemy.attackBody.getPosition())) {
                collisionDamageWithEnemy(enemy);
            }

            // check if the player attack body is colliding with the enemy body
            if (attackBody.getFixtureList().first().testPoint(enemy.body.getPosition())) {
                attack(enemy);
                enemy.setTakeDamageSoundPlayed(true);
            }
        }

        // check if the player collides with essence
        for (Essence essence : essences){
            if (body.getFixtureList().first().testPoint(essence.body.getPosition())){
                addEssence(1, essence.getType());
                // game crashes here
                //world.destroyBody(essence.getBody());
            }

        }
    }

    public void collisionDamageWithEnemy(Enemy enemy){
        takeDamage(1);
    }

    private void checkAndApplyDamageModifier() {
        if (enemiesAttackedInTimeFrame >= attackThreshold) {
            damageModifier += 10;
            currentState = State.BERSERK;
            isBerserk = true;

            // reset timer
            berserkTimer = 0.0f;

            // reset the count of attacked enemies
            enemiesAttackedInTimeFrame = 0;
            // reset the elapsed time
            elapsedTime = 0.0f;
        }
    }

    public void dispose(){
        for (TextureRegion[] animation : animations) {
            for (TextureRegion texRegion : animation) {
                texRegion.getTexture().dispose();
            }
        }
    }

}
