package com.projectsr.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Essence {

    public enum Type {
        GREEN,
        RED
    }

    private Type type;
    private AssetManager assetManager;
    private Vector2 position;
    private Texture texture;


    public Essence(AssetManager assetManager, Vector2 position, Type type) {
        this.assetManager = assetManager;
        this.position = position;
        setType(type);
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

    public void dispose() {
        // Dispose of resources
        assetManager.dispose();
    }

}
