package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CraftingScreen implements Screen {
    private mainGame game;
    private Stage stage;
    private Viewport viewport;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;

    private ImageButton backButton, startButton, craftButton, craftSlot, slot1, slot2, slot3, slot4, slot5, slot6;

    private Image charView, essenceRed, essenceGreen;
    private Texture background;

    public CraftingScreen(mainGame game) {
        this.game = game;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
    }

    public void create() {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create and add background image
        Texture bgTexture = new Texture(Gdx.files.internal("Ui/Buttons/t1.png"));
        Drawable bgDraw = new TextureRegionDrawable(bgTexture);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        mainTable.setDebug(false); // This is optional, but enables debug lines for tables.

        // Set the background drawable for the main table
        mainTable.setBackground(bgDraw);

        // Character Preview Window
        Texture charImg = new Texture(Gdx.files.internal("Ui/Buttons/character_selection.png"));
        charView = new Image(new TextureRegionDrawable(charImg));
        charView.setWidth(524f);
        charView.setHeight(524f);

        // Start button
        Texture sb = new Texture(Gdx.files.internal("Ui/Buttons/start_button.png"));
        Drawable sbDraw = new TextureRegionDrawable(sb);
        startButton = new ImageButton(sbDraw);

        // Craft button
        Texture cb = new Texture(Gdx.files.internal("Ui/Buttons/craft_button.png"));
        Drawable cbDraw = new TextureRegionDrawable(cb);
        craftButton = new ImageButton(cbDraw);

        // Back to menu Button
        Texture bb = new Texture(Gdx.files.internal("Ui/Buttons/back_button.png"));
        Drawable bbDraw = new TextureRegionDrawable(bb);
        backButton = new ImageButton(bbDraw);

        // Selected item for crafting
        Texture  cs = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Drawable csDraw = new TextureRegionDrawable(cs);
        craftSlot = new ImageButton(csDraw);

        // Resources required for crafting specific item
        Texture r1 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture r2 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));

        Drawable r1Draw = new TextureRegionDrawable(r1);
        Drawable r2Draw = new TextureRegionDrawable(r2);

        essenceRed = new Image(r1Draw);
        essenceGreen = new Image(r2Draw);


        // Table containing item crafting recipes
        Texture s1 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s2 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s3 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s4 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s5 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s6 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));

        Drawable s1Draw = new TextureRegionDrawable(s1);
        Drawable s2Draw = new TextureRegionDrawable(s2);
        Drawable s3Draw = new TextureRegionDrawable(s3);
        Drawable s4Draw = new TextureRegionDrawable(s4);
        Drawable s5Draw = new TextureRegionDrawable(s5);
        Drawable s6Draw = new TextureRegionDrawable(s6);

        slot1 = new ImageButton(s1Draw);
        slot2 = new ImageButton(s2Draw);
        slot3 = new ImageButton(s3Draw);
        slot4 = new ImageButton(s4Draw);
        slot5 = new ImageButton(s5Draw);
        slot6 = new ImageButton(s6Draw);

        Table firstColumn = new Table();
        firstColumn.add(backButton).center().row();
        firstColumn.add(charView).expandY().center().row();
        firstColumn.add(startButton).center();

        // Second Column
        Table secondColumn = new Table();
        secondColumn.add(craftSlot).center().row();
        Table secondColumnBottom = new Table();
        secondColumnBottom.add(essenceRed).left().pad(20);
        secondColumnBottom.add(essenceGreen).right().pad(20);
        secondColumn.add(secondColumnBottom).bottom();
        secondColumn.row();
        secondColumn.add(craftButton).bottom().padTop(50);

        // Third Column
        Table thirdColumn = new Table();
        thirdColumn.add(slot1).pad(5);
        thirdColumn.add(slot2).pad(5);
        thirdColumn.row();
        thirdColumn.add(slot3).pad(5);
        thirdColumn.add(slot4).pad(5);
        thirdColumn.row();
        thirdColumn.add(slot5).pad(5);
        thirdColumn.add(slot6).pad(5);

        // Adding columns to the main table
        mainTable.add(firstColumn).expand().fill().pad(10);
        mainTable.add(secondColumn).expand().fill().pad(10);
        mainTable.add(thirdColumn).expand().fill().pad(10);

        startButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(mainGame.gameScreen);
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(mainGame.menuScreen);
            }
        });
        craftButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                // Button clicked code here
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
