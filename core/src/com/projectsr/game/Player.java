package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    mainGame game;
    Vector2 position;
    Vector2 velocity;
    float width = 100;
    float height = 55;
    float speed;

    private Collectable collectable;
    private CraftingScreen craftingScreen;
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
    private int defenceValue = 2; // Default 2 to lose life
    private int maxDefence = 2;
    private boolean isDead = false;
    private int attackBaseDamage = 10;
    private int damageModifier = 0;
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
    private boolean hasDealtDamage = false;
    private boolean hasAttackMedallion = false;
    private boolean hasDefenceMedallion = false;
    private boolean hasBerserkMedallion = false;

    // Sound effects
    private Sound damageSound;
    private Sound deathSound;
    private Sound berserkSound;
    private Sound lootingSound;

    public Player(World world,AssetManager assetManager, CraftingScreen craftingScreen) {
        this.craftingScreen = craftingScreen;
        this.collectable = craftingScreen.getCollectable();

        this.world = world;
        position = new Vector2(880, 500);
        velocity = new Vector2(0,0);

        // change speed here
        speed = 100;

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

        assetManager.load("Audio/PlayerAudio/berserk.wav", Sound.class);
        assetManager.load("Audio/PlayerAudio/damage.wav", Sound.class);
        assetManager.load("Audio/PlayerAudio/death.wav", Sound.class);
        assetManager.load("Audio/PlayerAudio/looting.wav", Sound.class);
        assetManager.finishLoading();

        damageSound = assetManager.get("Audio/PlayerAudio/damage.wav", Sound.class);
        deathSound =  assetManager.get("Audio/PlayerAudio/death.wav", Sound.class);
        berserkSound = assetManager.get("Audio/PlayerAudio/berserk.wav", Sound.class);
        lootingSound = assetManager.get("Audio/PlayerAudio/looting.wav", Sound.class);
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
        fixtureDef.filter.maskBits = GameContactListener.ENEMY_CATEGORY | GameContactListener.ENEMY_ATTACK_CATEGORY | GameContactListener.ESSENCE_CATEGORY | GameContactListener.MAP_CATEGORY;
        fixtureDef.isSensor = true;

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

    public void update(float deltaTime) {

        if (craftingScreen.hasAttackMedallion) {
            setDamageModifier(10);
        } else if (craftingScreen.hasDefenceMedallion){
            setDefenceValue(5);
        }

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

        // checking for collision with things
        checkCollision();

        // update animation frame
        this.frame += 20 * deltaTime;
        if (this.frame >= frameCounts[currentState.ordinal()]) {
            this.frame = 0;
        }

        // Reset the hasDealtDamage flag when the attack animation is complete
        if ((int)this.frame >= animations[State.ATTACKING.ordinal()].length - 1 || currentState != State.ATTACKING) {
            hasDealtDamage = false;
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

            direction.nor();
            velocity.set(direction.scl(speed));

            // check if the player is moving to the left or right
            if (velocity.x > 0) {
                facingRight = true;
            } else if (velocity.x < 0) {
                facingRight = false;
            }

            // check to see if the player is moving
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

        if (currentState == State.DEATH) {
            // Check if the current frame is the last one
            if ((int)this.frame >= animations[State.DEATH.ordinal()].length - 1) {
                isDead = true;
            }
        }
        else {
            batch.draw(frameTexture, position.x, position.y, width, height);
        }
    }

    // This section tracks the health for the player
    public void takeDamage(int amount) {
        currentHeart -= amount;
        if (currentHeart < 0 ) {
            currentHeart = 0;
            currentState = State.DEATH;
            deathSound.play();
        }
        else {

            currentState = State.HURT;
            damageSound.play();
        }
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
    public void attack(Enemy enemy){

        if (!isBerserk) {
            currentState = State.ATTACKING;
        }

        // Only damage enemy once per swing
        if (!hasDealtDamage && (int)this.frame <= animations[State.ATTACKING.ordinal()].length) {
            enemy.takeDamage(attackBaseDamage + damageModifier);
            hasDealtDamage = true;
        }

        // Check if enemy is in death state but not already dead
        if (enemy.getCurrentState().equals("DEATH") && !enemy.isDead()) {
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

    public int getEnemiesKilled() {
        return enemiesKilled;
    }

    public void checkCollision() {

        // Get the objects colliding in the world
        for (Contact contact : world.getContactList()) {

            // Check if the contacted objects are touching
            if (contact.isTouching()) {

                Fixture fixtureA = contact.getFixtureA();
                Fixture fixtureB = contact.getFixtureB();

                // Check if the contact is between an enemy attack and player fixture
                if (GameContactListener.isEnemyAttackFixture(fixtureA) && GameContactListener.isPlayerFixture(fixtureB) ||
                        GameContactListener.isPlayerFixture(fixtureA) && GameContactListener.isEnemyAttackFixture(fixtureB)) {

                    // Player takes damage
                    collisionDamageWithEnemy();
                }
                // Check if the contact is  between enemy and player attack fixture
                if (GameContactListener.isEnemyFixture(fixtureA) && GameContactListener.isPlayerAttackFixture(fixtureB) ||
                        GameContactListener.isPlayerAttackFixture(fixtureA) && GameContactListener.isEnemyFixture(fixtureB)) {

                    Body enemyBody = null;

                    // Check which fixture is the enemy
                    if (GameContactListener.isEnemyFixture(fixtureA)) {
                        // Get the enemy body
                        enemyBody = fixtureA.getBody();
                    }
                    else if (GameContactListener.isEnemyFixture(fixtureB)) {
                        enemyBody = fixtureB.getBody();
                    }

                    // Check that the enemy body was defined and is part of Enemy
                    if (enemyBody != null && enemyBody.getUserData() instanceof Enemy) {

                        // Get the enemy
                        Enemy enemy = (Enemy) enemyBody.getUserData();

                        // Attack the enemy collided with
                        attack(enemy);
                        enemy.setTakeDamageSoundPlayed(true);
                    }
                }
                collectEssence(fixtureA, fixtureB);

                checkMapCollision(fixtureA, fixtureB);
            }
        }
    }

    public void checkMapCollision(Fixture fixtureA, Fixture fixtureB) {

        // Check if the contact is between player and map edge
        if ((GameContactListener.isPlayerFixture(fixtureA) && GameContactListener.isMapFixture(fixtureB)) ||
                (GameContactListener.isMapFixture(fixtureA) && GameContactListener.isPlayerFixture(fixtureB))) {

            Vector2 currentVelocity = body.getLinearVelocity();

            Body mapBody = null;

            // Check which fixture is the map
            if (GameContactListener.isMapFixture(fixtureA)) {
                // Get the map body
                mapBody = fixtureA.getBody();
            }
            else if (GameContactListener.isMapFixture(fixtureB)) {
                mapBody = fixtureB.getBody();
            }

            // Check that the map body was defined
            if (mapBody != null) {

                boolean xCollision = false;
                boolean yCollision = false;

                // Check horizontal collisions
                if (currentVelocity.x > 0 && mapBody.getPosition().x > body.getPosition().x) { // Right wall
                    body.setLinearVelocity(0, currentVelocity.y);
                    xCollision = true;
                }
                else if (currentVelocity.x < 0 && mapBody.getPosition().x < body.getPosition().x) { // Left wall
                    body.setLinearVelocity(0, currentVelocity.y);
                    xCollision = true;
                }

                // Check vertical collisions
                if (currentVelocity.y > 0 && mapBody.getPosition().y > body.getPosition().y) { // Top wall
                    body.setLinearVelocity(currentVelocity.x, 0);
                    yCollision = true;
                }
                else if (currentVelocity.y < 0 && mapBody.getPosition().y < body.getPosition().y) { // Bottom wall
                    body.setLinearVelocity(currentVelocity.x, 0);
                    yCollision = true;
                }

                // Adjust movement based on collisions
                if (xCollision && yCollision) {
                    // If both x and y collisions, stop all movement
                    body.setLinearVelocity(0, 0);
                }
                else if (xCollision) {
                    // If only x collision, stop x movement but allow y movement
                    body.setLinearVelocity(0, currentVelocity.y);
                }
                else if (yCollision) {
                    // If only y collision, stop y movement but allow x movement
                    body.setLinearVelocity(currentVelocity.x, 0);
                }
            }
        }
    }

    public void collectEssence(Fixture fixtureA, Fixture fixtureB) {

        // Check if the contacts are the player and essence fixtures
        if (GameContactListener.isPlayerFixture(fixtureA) && GameContactListener.isEssenceFixture(fixtureB) ||
                GameContactListener.isEssenceFixture(fixtureA) && GameContactListener.isPlayerFixture(fixtureB)) {

            Body essenceBody = null;

            // Check which fixture is the essence
            if (GameContactListener.isEssenceFixture(fixtureA)) {
                // Get the essence body
                essenceBody = fixtureA.getBody();
            }
            else if (GameContactListener.isEssenceFixture(fixtureB)) {
                essenceBody = fixtureB.getBody();
            }

            // Check that the essence body was defined
            if (essenceBody != null) {

                // Get the essence
                Essence essence = (Essence) essenceBody.getUserData();
                collectable.addEssence(1, essence.getType());

                // Delete the essence from the game
                world.destroyBody(essence.body);
                mainGame.gameScreen.removeEssence(essence);
            }
        }
    }

    public void collisionDamageWithEnemy() {

        // Check if the player no longer has any defence
        if (defenceValue <= 0) {
            takeDamage(1);
            defenceValue = maxDefence;
        }
        else {
            // Just remove a defence
            defenceValue--;
        }

    }

    private void checkAndApplyDamageModifier() {

        if (enemiesAttackedInTimeFrame >= attackThreshold && hasBerserkMedallion) {
            damageModifier += 10;
            currentState = State.BERSERK;
            isBerserk = true;

            berserkSound.play();

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

    public boolean isDead() {
        return isDead;
    }

    public void setDefenceValue(int newValue) {
        this.defenceValue = newValue;
    }


}
