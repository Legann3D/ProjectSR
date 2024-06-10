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

public class Player {
    Vector2 position;
    Vector2 velocity;
    float width = 100;
    float height = 55;
    float speed = 90;
    Body body;
    private World world;

    TextureRegion[][] animations;
    int[] frameCounts;
    float frame = 0;

    boolean facingRight = true;

    public OrthographicCamera camera;

    enum State {
        IDLE, WALKING, ATTACKING, HURT
    }

    State currentState = State.IDLE;

    public Player(World world) {

        position = new Vector2(880, 600);
        velocity = new Vector2(0,0);

        // initialize camera
        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        camera = new OrthographicCamera();
        camera.setToOrtho(false, w / h * 250, 250);

        // Initialize the world
        this.world = world;

        // initialize animations and frame counts
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

        //Collision
        // Create Box2D body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody; // Player is a dynamic body
        bodyDef.position.set(position.x, position.y); // Initial position
        body = world.createBody(bodyDef);

        // Create fixture
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width / 2, height / 2); // Set box shape based on player dimensions
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = 1f; // Adjust density as needed
        fixtureDef.friction = 0.5f; // Adjust friction as needed
        body.createFixture(fixtureDef);
        fixtureDef.isSensor = true;
        fixtureDef.filter.categoryBits = GameContactListener.PLAYER_CATEGORY;
        fixtureDef.filter.maskBits = GameContactListener.PLAYER_CATEGORY; // Ensure proper collision detection
        shape.dispose();

    }

    public void update(float deltaTime) {
        controls();

        // Update player position based on Box2D body
        position.set(body.getPosition().x, body.getPosition().y);

        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

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
            velocity = touchPos.sub(position).nor().scl(speed);

            // check if the player is moving to the left or right
            if (velocity.x > 0) {
                facingRight = true;
            } else if (velocity.x < 0) {
                facingRight = false;
            }

            currentState = State.WALKING;

        } else {
            velocity.set(0, 0);
            currentState = State.IDLE;
        }


    }

    // Method to handle collision events (called from GameContactListener)
    public void handleCollision() {
        // Adjust player's velocity based on collision
        // set velocity to zero to prevent movement
        velocity.set(0, 0);
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

    public void dispose(){
        for (TextureRegion[] animation : animations) {
            for (TextureRegion texRegion : animation) {
                texRegion.getTexture().dispose();
            }
        }
    }

}
