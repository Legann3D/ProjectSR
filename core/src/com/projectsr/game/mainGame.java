package com.projectsr.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class mainGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
<<<<<<< Updated upstream
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
=======
		assetManager = new AssetManager();

		// Load enemy assets
		// Skeleton
		assetManager.load("Enemy/Skeleton/Walk.png", Texture.class);
		//assetManager.load("Attack1.png", Texture.class);
		//assetManager.load("Attack2.png", Texture.class);
		//assetManager.load("Death.png", Texture.class);

		assetManager.finishLoading(); // Blocks until all assets are loaded

		gameScreen = new GameScreen(this, assetManager);
		menuScreen = new MenuScreen(this);

		//Uncomment this code once the Main Menu class has been implemented
		//setScreen(menuScreen);
		setScreen(gameScreen);
>>>>>>> Stashed changes
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
