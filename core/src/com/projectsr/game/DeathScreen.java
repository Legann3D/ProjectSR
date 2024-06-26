package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class DeathScreen implements Screen {

    private mainGame game;
    private AssetManager assetManager;
    private Stage stage;
    private Viewport viewport;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;

    private ImageButton menuButton;
    private Texture background;
    private Sound buttonPressSound;
    public DeathScreen(mainGame game, AssetManager assetManager) {
        this.game = game;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        this.assetManager = assetManager;
    }

    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create and add background image
        Texture bgTexture = new Texture(Gdx.files.internal("Ui/Buttons/death_screen.png"));
        Drawable bgDraw = new TextureRegionDrawable(bgTexture);

        buttonPressSound = assetManager.get("Audio/MiscAudio/buttonPress.wav", Sound.class);
        buttonPressSound.setVolume(12345, 3.0f);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        mainTable.setDebug(false); // This is optional, but enables debug lines for tables.

        // Set the background drawable for the main table
        mainTable.setBackground(bgDraw);


        // Menu button
        Texture sb = new Texture(Gdx.files.internal("Ui/Buttons/menu_icon.png"));
        Drawable sbDraw = new TextureRegionDrawable(sb);
        menuButton = new ImageButton(sbDraw);


        Table firstColumn = new Table();
        firstColumn.add(menuButton).bottom().pad(200).expandY();


        // Adding columns to the main table
        mainTable.add(firstColumn).expand().fill().pad(10);

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                buttonPressSound.play();
                game.setScreen(mainGame.craftingScreen);
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

