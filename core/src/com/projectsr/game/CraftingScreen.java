package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class CraftingScreen implements Screen {
    private mainGame game;
    private Hub hub;

    public Collectable collectable = null;
    private AssetManager assetManager;
    private Stage stage;
    private Viewport viewport;
    private final int SCREEN_WIDTH = 1920;
    private final int SCREEN_HEIGHT = 1080;

    private ImageButton backButton, startButton, craftButton, slot1, slot2, slot3, slot4, slot5, slot6;

    private Image charView, essenceRed, essenceGreen, craftSlot;
    private Texture background;
    private Sound buttonPressSound;

    boolean hasAttackMedallion = false;
    boolean hasDefenceMedallion = false;
    boolean hasBerserkMedallion = false;

    boolean bPendant = false;
    boolean aPendant = false;
    boolean hpPendant = false;


    public CraftingScreen(mainGame game, AssetManager assetManager) {
        this.game = game;
        this.viewport = new FitViewport(SCREEN_WIDTH, SCREEN_HEIGHT);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        this.assetManager = assetManager;
    }

    public void create() {

        if (mainGame.gameScreen.getHub() == null) {
            hub = new Hub(game);
        }
        else {
            hub = mainGame.gameScreen.getHub();
        }

        if (this.collectable == null) {
            this.collectable = new Collectable(hub);
        }

        stage = new Stage();
        Gdx.input.setInputProcessor(stage);

        // Create and add background image
        Texture bgTexture = new Texture(Gdx.files.internal("Ui/Buttons/t1.png"));
        Drawable bgDraw = new TextureRegionDrawable(bgTexture);

        buttonPressSound = assetManager.get("Audio/MiscAudio/buttonPress.wav", Sound.class);
        buttonPressSound.setVolume(12345, 3.0f);

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
        craftSlot = new Image(csDraw);

        // Resources required for crafting specific item
        Texture r1 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
        Texture r2 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));

        Drawable r1Draw = new TextureRegionDrawable(r1);
        Drawable r2Draw = new TextureRegionDrawable(r2);

        essenceRed = new Image(r1Draw);
        essenceGreen = new Image(r2Draw);


        // Table containing item crafting recipes
        Texture s1 = new Texture(Gdx.files.internal("Ui/Buttons/berserker_icon.png"));
        Texture s2 = new Texture(Gdx.files.internal("Ui/Buttons/attack_icon.png"));
        Texture s3 = new Texture(Gdx.files.internal("Ui/Buttons/defence_icon.png"));
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
                buttonPressSound.play();
                game.setScreen(mainGame.gameScreen);
            }
        });
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                buttonPressSound.play();
                game.setScreen(mainGame.menuScreen);
            }
        });
        craftButton.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                if(aPendant == true){
                    craftAttackMedallion();
                } else if (hpPendant == true) {
                    craftDefenceMedallion();
                } else if (bPendant == true){
                    craftBerserkMedallion();
                }
                buttonPressSound.play();
            }
        });
        slot1.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                bPendant = true;
                // Load new textures
                Texture newRedEssenceTexture = new Texture(Gdx.files.internal("Ui/Buttons/attack_icon.png"));
                Texture newGreenEssenceTexture = new Texture(Gdx.files.internal("Ui/Buttons/defence_icon.png"));
                Texture newCraftSlotTexture = new Texture(Gdx.files.internal("Ui/Buttons/berserker_icon.png"));

                // Create new Drawable instances
                Drawable newRedEssenceDrawable = new TextureRegionDrawable(newRedEssenceTexture);
                Drawable newGreenEssenceDrawable = new TextureRegionDrawable(newGreenEssenceTexture);
                Drawable newCraftSlotDrawable = new TextureRegionDrawable(newCraftSlotTexture);

                // Set new drawables to the ImageButton instances
                craftSlot.setDrawable(newCraftSlotDrawable);
                essenceRed.setDrawable(newRedEssenceDrawable);
                essenceGreen.setDrawable(newGreenEssenceDrawable);
            }
        });

        slot2.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                aPendant = true;

                // Load new textures
                Texture newRedEssenceTexture1 = new Texture(Gdx.files.internal("Ui/Buttons/red_essence_icon.png"));
                Texture newGreenEssenceTexture1 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
                Texture newCraftSlotTexture1 = new Texture(Gdx.files.internal("Ui/Buttons/attack_icon.png"));

                // Create new Drawable instances
                Drawable newRedEssenceDrawable1 = new TextureRegionDrawable(newRedEssenceTexture1);
                Drawable newGreenEssenceDrawable1 = new TextureRegionDrawable(newGreenEssenceTexture1);
                Drawable newCraftSlotDrawable1 = new TextureRegionDrawable(newCraftSlotTexture1);

                // Set new drawables to the ImageButton instances
                craftSlot.setDrawable(newCraftSlotDrawable1);
                essenceRed.setDrawable(newRedEssenceDrawable1);
                essenceGreen.setDrawable(newGreenEssenceDrawable1);
            }
        });

        slot3.addListener(new ClickListener() {
            @Override
            public void clicked (InputEvent event, float x, float y) {
                hpPendant = true;

                // Load new textures
                Texture newRedEssenceTexture2 = new Texture(Gdx.files.internal("Ui/Buttons/blank_icon.png"));
                Texture newGreenEssenceTexture2 = new Texture(Gdx.files.internal("Ui/Buttons/green_essence_icon.png"));
                Texture newCraftSlotTexture2 = new Texture(Gdx.files.internal("Ui/Buttons/defence_icon.png"));

                // Create new Drawable instances
                Drawable newRedEssenceDrawable2 = new TextureRegionDrawable(newRedEssenceTexture2);
                Drawable newGreenEssenceDrawable2 = new TextureRegionDrawable(newGreenEssenceTexture2);
                Drawable newCraftSlotDrawable2 = new TextureRegionDrawable(newCraftSlotTexture2);

                // Set new drawables to the ImageButton instances
                craftSlot.setDrawable(newCraftSlotDrawable2);
                essenceRed.setDrawable(newRedEssenceDrawable2);
                essenceGreen.setDrawable(newGreenEssenceDrawable2);
            }
        });

    }

    /**
     * This will craft an attack medallion if there is enough red essence
     * and it will add 10 to damage modifier
     */

    public void craftAttackMedallion() {
        if (collectable.getEssences(Essence.Type.RED) >= 1) {
            collectable.removeEssence(1, Essence.Type.RED);
            hasAttackMedallion = true;
            //System.out.println("Crafted Attack Medallion");
            //System.out.println("Essence Amount" + collectable.getEssences(Essence.Type.RED));
        }
    }
    /**
     * THis will craft a defence medallion if there is enough green essence
     * and it will add 5 to defence
     */
    public void craftDefenceMedallion() {
        if (collectable.getEssences(Essence.Type.GREEN) >= 1) {
            collectable.removeEssence(1, Essence.Type.GREEN);
            hasDefenceMedallion = true;
            System.out.println("Crafted Defence Medallion");
        }
    }
    /**
     * This will craft the berserk medallion and it will allow the player
     * to go berserk if they have this medallion
     */
    public void craftBerserkMedallion() {
        if (hasAttackMedallion && hasDefenceMedallion) {
            hasBerserkMedallion = true;
            System.out.println("Crafted Berserk Medallion");
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.draw();
    }

    public Collectable getCollectable() {
        return collectable;
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
