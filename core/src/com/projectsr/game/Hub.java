package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Hub {
    private mainGame game;
    private CraftingScreen craftingScreen;
    private AssetManager assetManager;
    private Stage stage;
    private Table table;
    private ImageButton menuButton;
    private Image life1, life2, life3, bottomMiddleImage, skill1, skill2, skill3;
    public int collectedItems;
    public Label counterLabel;

    public Hub(mainGame game){
        this.game = game;
    }
    public void create () {
        this.collectedItems = 0;
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        craftingScreen = new CraftingScreen(game,assetManager);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);

        BitmapFont font = new BitmapFont(); // Use your own font here
        font.getData().setScale(3);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        this.counterLabel = new Label("0", labelStyle);

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

        skill1 = new Image(s1Draw);
        skill2 = new Image(s2Draw);
        skill3 = new Image(s3Draw);

        // Load the images for the top-left position
        Texture lifeT1 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));
        Texture lifeT2 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));
        Texture lifeT3 = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));

        // Load the essence counter
        Texture essenceGreen = new Texture(Gdx.files.internal("Ui/Hub Elements/life_full.png"));

        // Load the images for the bottom-center position
        Texture bottomMiddleTexture = new Texture(Gdx.files.internal("Ui/Hub Elements/hub_bottom.png"));

        // Create the MenuButton
        menuButton = new ImageButton(buttonDrawable);

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
        secondColumn.add(counterLabel).top().left();
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

        if (craftingScreen.hasBerserkMedallion){
            // Load new textures
            Texture bersSkill = new Texture(Gdx.files.internal("Ui/Buttons/berserker_icon.png"));

            // Create new Drawable instances
            Drawable bersSkillDraw = new TextureRegionDrawable(bersSkill);

            // Set new drawables to the ImageButton instances
            skill1.setDrawable(bersSkillDraw);

        }
        if (craftingScreen.hasDefenceMedallion) {
            // Load new textures
            Texture defSkill = new Texture(Gdx.files.internal("Ui/Buttons/defence_icon.png"));

            // Create new Drawable instances
            Drawable defSkillDraw = new TextureRegionDrawable(defSkill);

            // Set new drawables to the ImageButton instances
            skill2.setDrawable(defSkillDraw);

        }
        if (craftingScreen.hasAttackMedallion) {
            // Load new textures
            Texture attSkill = new Texture(Gdx.files.internal("Ui/Buttons/attack_icon.png"));

            // Create new Drawable instances
            Drawable attSkillDraw = new TextureRegionDrawable(attSkill);

            // Set new drawables to the ImageButton instances
            skill3.setDrawable(attSkillDraw);
        }

        menuButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                game.setScreen(mainGame.craftingScreen);
            }
        });
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
