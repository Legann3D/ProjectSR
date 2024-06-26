package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;

public class Hub {
    private Stage stage;
    private Table table;
    private ImageButton menuButton;
    private Image life1, life2, life3, bottomMiddleImage;
    private ImageButton skill1, skill2, skill3;
    private Label timerLabel;
    private float time;

    public void create () {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        mainTable.setDebug(false); // This is optional, but enables debug lines for tables.

        // Load the button image
        Texture buttonTexture = new Texture(Gdx.files.internal("Ui/Buttons/menu_icon.png"));
        Drawable buttonDrawable = new TextureRegionDrawable(buttonTexture);

        // Load the skill image
        Texture s1 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s2 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture s3 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Drawable s1Draw = new TextureRegionDrawable(s1);
        Drawable s2Draw = new TextureRegionDrawable(s2);
        Drawable s3Draw = new TextureRegionDrawable(s3);

        skill1 = new ImageButton(s1Draw);
        skill2 = new ImageButton(s2Draw);
        skill3 = new ImageButton(s3Draw);

        // Load the images for the top-left position
        Texture lifeT1 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));
        Texture lifeT2 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));
        Texture lifeT3 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));

        // Load the images for the bottom-center position
        Texture bottomMiddleTexture = new Texture(Gdx.files.internal("Ui/Hub Elements/hub_bottom.png"));

        // Create the MenuButton
        menuButton = new ImageButton(buttonDrawable);
        menuButton.getImage();

        life1 = new Image(new TextureRegionDrawable(lifeT1));
        life2 = new Image(new TextureRegionDrawable(lifeT2));
        life3 = new Image(new TextureRegionDrawable(lifeT3));
        bottomMiddleImage = new Image(new TextureRegionDrawable(bottomMiddleTexture));

        Table firstColumn = new Table();
        firstColumn.top();
        firstColumn.add(life1).left().padTop(20);
        firstColumn.add(life2).left().padTop(20);
        firstColumn.add(life3).left().padTop(20);

        // Second Column
        Table secondColumn = new Table();
        secondColumn.row();
        secondColumn.add(skill1).bottom().padBottom(20).expandY();
        secondColumn.add(skill2).bottom().padBottom(20);
        secondColumn.add(skill3).bottom().padBottom(20);

        // Third Column
        Table thirdColumn = new Table();
        thirdColumn.top();
        thirdColumn.add(menuButton).right().padTop(20);

        // Adding columns to the main table
        mainTable.add(firstColumn).expand().fill().pad(10);
        mainTable.add(secondColumn).expand().fill().pad(10);
        mainTable.add(thirdColumn).expand().fill().pad(10);

    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render () {
        //stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        ((TextureRegionDrawable) life1.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life2.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life3.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) bottomMiddleImage.getDrawable()).getRegion().getTexture().dispose();
    }
}
