package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Scaling;

public class Hub {
    private Stage stage;
    private Table table;
    private ImageButton menuButton;
    private Image topMiddleImage, life1, life2, life3, bottomMiddleImage;

    public void create () {
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        table = new Table();
        table.setFillParent(true); // This ensures the table scales to the screen size
        stage.addActor(table);

        table.setDebug(true); // This is optional, but enables debug lines for tables.

        // Load the button image
        Texture buttonTexture = new Texture(Gdx.files.internal("Ui/Buttons/menu_icon.png"));
        Drawable buttonDrawable = new TextureRegionDrawable(buttonTexture);

        // Load the image texture for the top middle image
        Texture topMiddleTexture = new Texture(Gdx.files.internal("Ui/Hub Elements/hub_top.png"));
        Drawable topMiddleDrawable = new TextureRegionDrawable(topMiddleTexture);

        // Load the images for the top-left position
        Texture lifeT1 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));
        Texture lifeT2 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));
        Texture lifeT3 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));

        // Load the images for the bottom-center position
        Texture bottomMiddleTexture = new Texture(Gdx.files.internal("Ui/Hub Elements/hub_bottom.png"));

        // Create the MenuButton
        menuButton = new ImageButton(buttonDrawable);
        menuButton.getImage().setScaling(Scaling.fit);

        // Create the TopMiddleOverlay
        topMiddleImage = new Image(topMiddleDrawable);
        topMiddleImage.setScaling(Scaling.fit);

        life1 = new Image(new TextureRegionDrawable(lifeT1));
        life2 = new Image(new TextureRegionDrawable(lifeT2));
        life3 = new Image(new TextureRegionDrawable(lifeT3));
        bottomMiddleImage = new Image(new TextureRegionDrawable(bottomMiddleTexture));

        // Add the images to the table
        table.top().add(life1).padTop(20).left().padLeft(20);
        table.add(life2).padTop(20).left().padLeft(20);
        table.add(life3).padTop(20).left().padLeft(20);
        table.add(topMiddleImage).expandX().padTop(20).center();
        table.add(menuButton).size(80, 80).right().padTop(20).padRight(20);

        // Add a new row for the bottom center image
        table.row();
        table.add(bottomMiddleImage).expandY().bottom().colspan(5).padBottom(20);
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
        ((TextureRegionDrawable) topMiddleImage.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life1.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life2.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life3.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) bottomMiddleImage.getDrawable()).getRegion().getTexture().dispose();
    }
}
