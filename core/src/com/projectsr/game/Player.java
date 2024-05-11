package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

public class Player {
    Texture texture;
    Vector2 position;
    Vector2 velocity;
    float width = 100;
    float height = 55;
    float speed = 90;

    public Player() {
        texture = new Texture("Character/HeroKnight/Idle/HeroKnight_Idle_0.png");
        position = new Vector2(880, 500);
        velocity = new Vector2(0,0);
    }

    public void update(float deltaTime) {
        if (Gdx.input.isTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.graphics.getHeight() - Gdx.input.getY());
            velocity = touchPos.sub(position).nor().scl(speed);
        } else {
            velocity.set(0, 0);
        }

        position.add(velocity.x * deltaTime, velocity.y * deltaTime);

    }

    public void render (SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, width, height);
    }

    public void dispose(){
        texture.dispose();
    }

}
