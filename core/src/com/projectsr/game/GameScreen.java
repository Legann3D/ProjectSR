package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    mainGame  game;
    SpriteBatch batch;
    private AssetManager assetManager;

    // Level
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera camera;

    // Player
    Texture playerTex;
    Player playerCharacter;

    // Enemies
    private Enemy skeletonEnemy;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float timeSinceEnemyWave = 10;
    private float enemySpawnCount = 5;

    public GameScreen(mainGame game, AssetManager assetManager){
        this.assetManager = assetManager;
    }

    public void create(){
        batch = new SpriteBatch();

        //Player
        playerCharacter = new Player();

        //Level
        TmxMapLoader loader = new TmxMapLoader();
        this.map = loader.load("Level Design/nc_80x80.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);
        camera = new OrthographicCamera();
    }

    public void update(float f){
        spawnEnemies(f);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        playerCharacter.update(delta);
        renderer.setView(camera);
        renderer.render();

        /*
        BATCH BEGIN DRAW
         */
        batch.begin();
        playerCharacter.render(batch);

        // Loop through each enemy and render it
        for (Enemy enemy : enemies) {
            enemy.render(batch);
        }

        batch.end();
    }

    public void spawnEnemies(float f) {

        // Update the time since the last enemy wave
        timeSinceEnemyWave += f;

        // Check the time since the last enemy spawned
        if (timeSinceEnemyWave >= 10) {
            for (int i = 0; i < enemySpawnCount; i++) {
                // Create flying enemy
                skeletonEnemy = new SkeletonEnemy(this.assetManager);
                skeletonEnemy.create();
                // Add the enemy to the enemies array
                enemies.add(skeletonEnemy);
                timeSinceEnemyWave = 0; // Reset the timer
            }
        }

        // Create enemy iterator array
        Iterator<Enemy> enemyIter = enemies.iterator();

        // Loop as long as there is another enemy
        while (enemyIter.hasNext()) {

            Enemy enemy = enemyIter.next();
            enemy.update(f, playerCharacter);
        }
    }

    @Override
    public void dispose(){
        map.dispose();
        renderer.dispose();
    }

    @Override
    public void resize(int width, int height){
        camera.viewportWidth = width;
        camera.viewportHeight = height;
        camera.update();
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
