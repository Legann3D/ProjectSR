package com.projectsr.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Essence {

    public enum Type {
        GREEN,
        RED
    }

    private Type type;
    private AssetManager assetManager;
    private Vector2 position;
    private Texture texture;
    Body body;
    private World world;

    /**
     * Set up and initialise the essence class attributes and type.
     * Collision is created on set up.
     *
     * @param assetManager The asset manager instance being used in the game.
     * @param position The position the essence spawns
     * @param type The type of essence to be spawned.
     * @param world The world instance being used in the game for collision.
     */
    public Essence(AssetManager assetManager, Vector2 position, Type type, World world) {
        this.assetManager = assetManager;
        this.position = position;
        this.world = world;
        setType(type);
        createCollisionBody(this.world);
    }

    /**
     * Create the collision depending on the type passed to the constructor.
     */
    public void create() {

        switch(type) {
            case GREEN:
                texture = assetManager.get("Essences/green_essence.png", Texture.class);
                break;

            case RED:
                texture = assetManager.get("Essences/red_essence.png", Texture.class);
                break;

            default:
                // Something
        }
    }

    /**
     * Render the essence texture at the set position with slight offset, at set size.
     *
     * @param batch The batch used by the game.
     */
    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x + 65, position.y + 65, 20, 20);
    }

    /**
     * Set the type of essence, either red or green.
     *
     * @param type The type that it will be set to.
     */
    public void setType(Type type) {
        this.type = type;
    }

    public Type getType() {
        return type;
    }

    /**
     * The collision for the essence set up, at set position with correct filters.
     *
     * @param world The world instance being used by the game.
     */
    public void createCollisionBody(World world) {

        // Initialise the collision body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(position.x + 75, position.y + 75);
        body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(10, 10); // Set size

        // Initialise the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.filter.categoryBits = GameContactListener.ESSENCE_CATEGORY;
        fixtureDef.filter.maskBits = GameContactListener.PLAYER_CATEGORY; // Ensure proper collision detection

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public Body getBody() {
        return body;
    }

    /**
     * Dispose of resources.
     */
    public void dispose() {
        // Dispose of resources
        assetManager.dispose();
    }


}
