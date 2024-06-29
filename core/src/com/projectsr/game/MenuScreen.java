package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class MenuScreen implements Screen {
    private mainGame game;
    private AssetManager assetManager;
    private Stage stage;
    private Viewport viewport;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;
    private Music menuMusic;
    private Sound buttonPressSound;
    private Texture background;
    private ImageButton startButton, settingsButton, quitButton;
    public static SettingsScreen settingsScreen;

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
        menuMusic.setVolume(Settings.getVolume());
        menuMusic.setLooping(true);

        buttonPressSound = assetManager.get("Audio/MiscAudio/buttonPress.wav", Sound.class);
        buttonPressSound.setVolume(12345, 100.0f);

        settingsScreen = new SettingsScreen(game, menuMusic, assetManager);

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        background = new Texture("Ui/Buttons/main_menu_background.png");
        // Create and add background image
        Image backgroundImage = new Image(background);
        backgroundImage.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);

        // Start Button
        Texture sb = new Texture(Gdx.files.internal("Ui/Buttons/play_button_main_menu.png"));
        Drawable sbDraw = new TextureRegionDrawable(sb);
        startButton = new ImageButton(sbDraw);
        startButton.setWidth(524f);
        startButton.setHeight(140f);
        startButton.setPosition(SCREEN_WIDTH / 2 - 796f, SCREEN_HEIGHT  - startButton.getWidth()  - 400f);


        // Settings Button
        Texture setB = new Texture(Gdx.files.internal("Ui/Buttons/settings_button_main_menu.png"));
        Drawable setBDraw = new TextureRegionDrawable(setB);
        settingsButton = new ImageButton(setBDraw);
        settingsButton.setWidth(524f);
        settingsButton.setHeight(140f);
        settingsButton.setPosition(SCREEN_WIDTH  / 2 - 262f , SCREEN_HEIGHT - settingsButton.getWidth() - 400f);


        // Quit Button
        Texture qb = new Texture(Gdx.files.internal("Ui/Buttons/quit_button_main_menu.png"));
        Drawable qbDraw = new TextureRegionDrawable(qb);
        quitButton = new ImageButton(qbDraw);
        quitButton.setWidth(524f);
        quitButton.setHeight(140f);
        quitButton.setPosition(SCREEN_WIDTH / 2 + 272f, SCREEN_HEIGHT - quitButton.getWidth()  - 400f);

        stage.addActor(backgroundImage);
        stage.addActor(startButton);
        stage.addActor(settingsButton);
        stage.addActor(quitButton);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                buttonPressSound.play();
                menuMusic.stop();
                game.setScreen(mainGame.craftingScreen);
            }
        });

        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // Button clicked code here
                buttonPressSound.play();
                game.setScreen(settingsScreen);
            }
        });

        quitButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                buttonPressSound.play();
                Gdx.app.exit();
                System.exit(-1);
            }
        });
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
        background.dispose();
        assetManager.dispose();
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
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }
}
