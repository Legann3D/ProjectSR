package com.projectsr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;

public class mainGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;

	AssetManager assetManager;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");

		// Load enemy assets
		// Skeleton
		assetManager.load("Walk.png", Texture.class);
		assetManager.load("Attack1.png", Texture.class);
		assetManager.load("Attack2.png", Texture.class);
		assetManager.load("Death.png", Texture.class);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		img.dispose();
	}
}
