package com.projectsr.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

public class mainGame extends Game implements ApplicationListener  {

	private AssetManager assetManager;
	public static GameScreen gameScreen;
	public static MenuScreen menuScreen;
	public static CraftingScreen craftingScreen;

	@Override
	public void create () {
		assetManager = new AssetManager();

		// Load enemy assets
		// Skeleton
		assetManager.load("Enemy/Skeleton/Walk.png", Texture.class);
		assetManager.load("Enemy/Skeleton/Attack.png", Texture.class);
		//assetManager.load("Attack2.png", Texture.class);
		assetManager.load("Enemy/Skeleton/Death.png", Texture.class);

		assetManager.load("EnemyAudio/skeletonAttack.wav", Sound.class);
		assetManager.load("EnemyAudio/skeletonDie.mp3", Sound.class);
		assetManager.load("EnemyAudio/skeletonTakeHit.mp3", Sound.class);

		// Essences
		assetManager.load("Essences/red_essence.png", Texture.class);
		assetManager.load("Essences/green_essence.png", Texture.class);

		assetManager.load("Music/The Pirate And The Dancer.mp3", Music.class);
		assetManager.load("Music/Voxel Revolution.mp3", Music.class);

		assetManager.finishLoading(); // Blocks until all assets are loaded

		gameScreen = new GameScreen(this, assetManager);
		menuScreen = new MenuScreen(this, assetManager);
		craftingScreen = new CraftingScreen(this);

		//Uncomment this code once the Main Menu class has been implemented
		setScreen(menuScreen);
		//setScreen(gameScreen);
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