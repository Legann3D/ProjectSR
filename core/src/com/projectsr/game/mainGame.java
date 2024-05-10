package com.projectsr.game;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;

public class mainGame extends Game implements ApplicationListener  {

	AssetManager assetManager;
	public static GameScreen gameScreen;
	public static MenuScreen menuScreen;

	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);

        // Load enemy assets
        // Skeleton
        assetManager.load("Walk.png", Texture.class);
        assetManager.load("Attack1.png", Texture.class);
        assetManager.load("Attack2.png", Texture.class);
        assetManager.load("Death.png", Texture.class);
		//Uncomment this code once the Main Menu class has been implemented
		//setScreen(menuScreen);
		setScreen(gameScreen);
	}

	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		super.render();
	}
	
	@Override
	public void dispose () {
		super.dispose();
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}