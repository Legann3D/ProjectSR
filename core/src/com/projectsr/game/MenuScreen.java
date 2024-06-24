package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {
    private mainGame game;
    private AssetManager assetManager;
    private Stage stage;
    private Viewport viewport;
    private Texture startButton;
    private final int SCREEN_WIDTH = 1080;
    private final int SCREEN_HEIGHT = 1920;
    private Music menuMusic;

    public MenuScreen(mainGame game, AssetManager assetManager) {
        this.game = game;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        this.assetManager = assetManager;
    }

    public void create() {

        menuMusic = assetManager.get("Music/Voxel Revolution.mp3", Music.class);
        menuMusic.play();
        menuMusic.setVolume(1.0f);
        menuMusic.setLooping(true);

        // TODO: Change texture, and use asset manager
        startButton = new Texture("badlogic.jpg");

        final Image button = new Image(startButton);
        button.setWidth(1000f);
        button.setHeight(500f);
        button.setPosition(SCREEN_WIDTH / 2 - button.getWidth() / 2, SCREEN_HEIGHT / 2 - button.getHeight() / 2);

        button.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(mainGame.gameScreen);
                menuMusic.stop();
            }
        });

        stage.addActor(button);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        startButton.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }

    @Override
    public void show() {
        create();
    }

    @Override
    public void hide() { }
}
