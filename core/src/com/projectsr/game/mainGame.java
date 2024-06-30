package com.projectsr.game;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.ScreenUtils;

/**
 * This is the main game class which is the central class for the game,
 * it manages loading assets and different screens
 */
public class mainGame extends Game implements ApplicationListener  {

	private AssetManager assetManager;
	public static GameScreen gameScreen;
	public static MenuScreen menuScreen;
	public static CraftingScreen craftingScreen;
	public static DeathScreen deathScreen;

	/**
	 * This loads all the files for texture and audio into asset manager.
	 *
	 * Credit to https://mixkit.co/ for the free audio for the player
	 * Credit to https://luizmelo.itch.io/monsters-creatures-fantasy for the free enemy assets
	 *
	 */
	@Override
	public void create () {
		assetManager = new AssetManager();

		// Load enemy assets
		// Skeleton
		assetManager.load("Enemy/Skeleton/Walk.png", Texture.class);
		assetManager.load("Enemy/Skeleton/Attack.png", Texture.class);
		//assetManager.load("Attack2.png", Texture.class);
		assetManager.load("Enemy/Skeleton/Death.png", Texture.class);

		assetManager.load("Audio/EnemyAudio/skeletonAttack.wav", Sound.class);
		assetManager.load("Audio/EnemyAudio/skeletonDie.mp3", Sound.class);
		assetManager.load("Audio/EnemyAudio/skeletonTakeHit.mp3", Sound.class);

		 // Load Player Sounds
		assetManager.load("Audio/PlayerAudio/berserk.wav", Sound.class);
		assetManager.load("Audio/PlayerAudio/damage.wav", Sound.class);
		assetManager.load("Audio/PlayerAudio/death.wav", Sound.class);
		assetManager.load("Audio/PlayerAudio/looting.wav", Sound.class);

		// Essences
		assetManager.load("Essences/red_essence.png", Texture.class);
		assetManager.load("Essences/green_essence.png", Texture.class);

		// Music
		assetManager.load("Music/The Pirate And The Dancer.mp3", Music.class);
		assetManager.load("Music/Voxel Revolution.mp3", Music.class);

		// UI
		assetManager.load("Ui/Buttons/t1.png", Texture.class);
		assetManager.load("Ui/Buttons/slider_line.png", Texture.class);
		assetManager.load("Ui/Buttons/blank_icon.png", Texture.class);

		assetManager.load("Audio/MiscAudio/buttonPress.wav", Sound.class);

		assetManager.finishLoading(); // Blocks until all assets are loaded

		gameScreen = new GameScreen(this, assetManager);
		menuScreen = new MenuScreen(this, assetManager);
		craftingScreen = new CraftingScreen(this, assetManager);
		deathScreen = new DeathScreen(this,assetManager);

		setScreen(menuScreen);
		//setScreen(gameScreen);
		//setScreen(deathScreen);
	}

	/**
	 * This renders the game by first clearing the screen and the then delegating to active screen
	 */
	@Override
	public void render () {
		ScreenUtils.clear(1, 0, 0, 1);
		super.render();
	}

	/**
	 * This disposes of the game resources
	 */
	@Override
	public void dispose () {
		super.dispose();
	}

	/**
	 * This just resizes the game screen to the width and height
	 *
	 * @param width the new width in pixels
	 * @param height the new height in pixels
	 */
	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	/**
	 * This pauses the game
	 */
	@Override
	public void pause() {
		super.pause();
	}

	/**
	 * This resumes the game
	 */
	@Override
	public void resume() {
		super.resume();
	}
}