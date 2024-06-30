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
    private ImageButton menuButton;
    private Image life1, life2, life3, bottomMiddleImage, skill1, skill2, skill3;
    public int collectedRedEssence, collectedGreenEssence;
    private Label counterLabelRed, counterLabelGreen;

    /**
     * Set up and initialise the Hub class attributes and type.
     *
     * @param game The world instance of the Game.
     */
    public Hub(mainGame game){
        this.game = game;
    }

    /**
     * Loads a font.
     * Initialize Textures, Drawables, and Table Rows and Columns for the UI.
     * Initialize new Textures, Drawables if specific ImageButtons are presses.
     */
    public void create () {
        this.collectedRedEssence = 0;
        this.collectedGreenEssence = 0;

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        craftingScreen = new CraftingScreen(game,assetManager);
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        stage.addActor(mainTable);


        BitmapFont font = new BitmapFont();
        font.getData().setScale(3);
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        this.counterLabelRed = new Label(" ", labelStyle);
        this.counterLabelGreen = new Label(" ", labelStyle);

        // This enables debug lines for tables.
        mainTable.setDebug(false);

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

        // First Column of the Table
        Table firstColumn = new Table();
        firstColumn.top().left();
        firstColumn.add(life1).left().padTop(20);
        firstColumn.add(life2).left().padTop(20);
        firstColumn.add(life3).left().padTop(20);

        // Second Column of the Table
        Table secondColumn = new Table();
        secondColumn.top().left();
        secondColumn.add(counterLabelRed).top().left().pad(20);
        secondColumn.add(counterLabelGreen).top().left().pad(20);
        secondColumn.row();
        secondColumn.bottom().left();
        secondColumn.add(skill1).center().bottom().pad(20).expandY();
        secondColumn.add(skill2).center().bottom().pad(20);
        secondColumn.add(skill3).center().bottom().pad(20);

        // Third Column of the Table
        Table thirdColumn = new Table();
        thirdColumn.top().right();
        thirdColumn.add(menuButton).right().pad(20);

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
    /**
     * Updates the Collected Red Essence Ui Label element
     */
    public void updateRedLabel(){
        this.counterLabelRed.setText(collectedRedEssence);
    }

    /**
     * Updates the Collected Green Essence Ui Label element
     */
    public void updateGreenLabel(){
        this.counterLabelGreen.setText(collectedGreenEssence);
    }

    public void resize (int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    public void render (float delta) {
        stage.act(delta);
        stage.draw();
    }

    /**
     * Dispose of resources.
     */
    public void dispose() {
        stage.dispose();
        ((TextureRegionDrawable) life1.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life2.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) life3.getDrawable()).getRegion().getTexture().dispose();
        ((TextureRegionDrawable) bottomMiddleImage.getDrawable()).getRegion().getTexture().dispose();
    }
    // assetManager.dispose(); // Dispose of if assigned to class attribute
}
