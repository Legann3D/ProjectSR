package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class SettingsScreen implements Screen {
    private MenuScreen menu;
    private mainGame game;
    private AssetManager assetManager;
    private Stage stage;
    private Slider volumeSlider;
    private Music music;
    private Sound buttonPressSound;
    private Texture bgTexture, sliderLine, sliderKnob;
    private Button backButton;
    private float volume;

    /**
     * Set up the UI and buttons for the settings screen.
     *
     * @param game The main game instance.
     * @param music The music that is currently playing
     * @param assetManager The asset manager instance used.
     */
    public SettingsScreen(mainGame game, Music music, AssetManager assetManager) {

        this.music = music;
        this.game = game;
        this.assetManager = assetManager;

        // Create and add background image
        bgTexture = assetManager.get("Ui/Buttons/t1.png", Texture.class);
        sliderLine = assetManager.get("Ui/Buttons/slider_line.png", Texture.class);
        sliderKnob = assetManager.get("Ui/Buttons/blank_icon.png", Texture.class);
        Drawable bgDraw = new TextureRegionDrawable(bgTexture);

        buttonPressSound = assetManager.get("Audio/MiscAudio/buttonPress.wav", Sound.class);
        buttonPressSound.setVolume(12345, 3.0f);

        // Back to menu Button
        Texture bb = new Texture(Gdx.files.internal("Ui/Buttons/back_button.png"));
        Drawable bbDraw = new TextureRegionDrawable(bb);
        backButton = new ImageButton(bbDraw);

        Table mainTable = new Table();
        mainTable.setFillParent(true);

        // Set the background drawable for the main table
        mainTable.setBackground(bgDraw);

        // Create a stage and add input processor
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create the slider style
        Slider.SliderStyle sliderStyle = new Slider.SliderStyle();
        sliderStyle.background = new TextureRegionDrawable(new TextureRegion(sliderLine));
        sliderStyle.knob = new TextureRegionDrawable(new TextureRegion(sliderKnob));

        // Create the slider
        volumeSlider = new Slider(0f, 1f, 0.02f, false, sliderStyle);
        volumeSlider.setValue(music.getVolume()); // Set initial value based on current music volume

        // Add a listener to change the music volume
        volumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                volume = volumeSlider.getValue();
                music.setVolume(volume);
                Settings.setVolume(volume);
            }
        });

        // Create new table to display the button in top left
        Table topLeftTable = new Table();
        // Add the back button to the top left
        topLeftTable.add(backButton).width(600).height(150).left().top().padTop(30);

        // Add the nested table to the main table
        mainTable.add(topLeftTable).expand().left().top();

        mainTable.row();
        // Add the slider to the table
        mainTable.add(volumeSlider).width(800).height(50).center().padBottom(100);

        // Add filler row so slider is center
        mainTable.row();
        mainTable.add().expand();

        // Add the main table to the stage
        stage.addActor(mainTable);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                buttonPressSound.play();
                game.setScreen(mainGame.menuScreen);
            }
        });
    }

    /**
     * Render the stage, which includes the UI.
     *
     * @param delta The time in seconds since the last render.
     */
    @Override
    public void render(float delta) {
        // Clear the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Draw the stage
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();
    }

    /**
     * Resize the viewport with the set screen size.
     *
     * @param width The width of the screen.
     * @param height The height of the screen.
     */
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    /**
     * Show is called when the screen is set to this one.
     */
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    /**
     * Hide is called when the screen is set to another screen from this one.
     */
    @Override
    public void hide() {
        // Clear the input processor
        Gdx.input.setInputProcessor(null);
    }

    /**
     * Called when the game is paused, not implemented.
     */
    @Override
    public void pause() {
    }

    /**
     * Called when the game is resumed after being paused, not implemented.
     */
    @Override
    public void resume() {
    }

    /**
     * Called automatically when the class goes out of scope.
     * Disposes of the stage.
     */
    @Override
    public void dispose() {
        stage.dispose();
    }
}

