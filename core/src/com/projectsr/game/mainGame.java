package com.projectsr.game;

<<<<<<< Updated upstream
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class mainGame extends ApplicationAdapter {
	SpriteBatch batch;
	Texture img;
	
	@Override
	public void create () {
		batch = new SpriteBatch();
		img = new Texture("badlogic.jpg");
=======
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;


public class mainGame extends Game implements ApplicationListener {

	public static GameScreen gameScreen;
	public static MenuScreen menuScreen;
	
	@Override
	public void create () {
		gameScreen = new GameScreen(this);
		menuScreen = new MenuScreen(this);

		//Uncomment this code once the Main Menu class has been implemented
		//setScreen(menuScreen);
		setScreen(gameScreen);
>>>>>>> Stashed changes
	}

	@Override
	public void render () {
<<<<<<< Updated upstream
		ScreenUtils.clear(1, 0, 0, 1);
		batch.begin();
		batch.draw(img, 0, 0);
		batch.end();
=======
		super.render();
>>>>>>> Stashed changes
	}
	
	@Override
	public void dispose () {
<<<<<<< Updated upstream
		batch.dispose();
		img.dispose();
=======
		super.dispose();
>>>>>>> Stashed changes
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
