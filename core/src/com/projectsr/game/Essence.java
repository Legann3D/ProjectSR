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
    private Body body;
    private World world;

    public Essence(AssetManager assetManager, Vector2 position, Type type, World world) {
        this.assetManager = assetManager;
        this.position = position;
        this.world = world;
        setType(type);
        createCollisionBody(this.world);
    }

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

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x + 65, position.y + 65, 20, 20);
    }

    public void setType(Type type) {
        this.type = type;
    }

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
        fixtureDef.filter.maskBits = GameContactListener.ESSENCE_CATEGORY; // Ensure proper collision detection

        body.createFixture(fixtureDef);
        shape.dispose();

        body.setUserData(this);
    }

    public void dispose() {
        // Dispose of resources
        assetManager.dispose();
    }
}
