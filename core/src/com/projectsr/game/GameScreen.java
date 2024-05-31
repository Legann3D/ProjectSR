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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    mainGame  game;
    SpriteBatch batch;
    private AssetManager assetManager;
    private World world;
    private Box2DDebugRenderer debugRenderer;

    // Level
    private TiledMap map;
    private OrthogonalTiledMapRenderer renderer;
    private OrthographicCamera gameCam, camera;

    // Player
    Texture playerTex;
    Player playerCharacter;

    // Enemies
    private Enemy skeletonEnemy;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float timeSinceEnemyWave = 10;
    private int enemySpawnCount = 5;
    private float spawnDistance = 500;
    private float enemyHealth = 50;

    private ArrayList<Essence> essences = new ArrayList<>();

    public GameScreen(mainGame game, AssetManager assetManager) {
        this.assetManager = assetManager;
        this.world = new World(new Vector2(0, 0), true); // No gravity
        this.debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new GameContactListener()); // Collision
    }

    public void create(){
        batch = new SpriteBatch();

        //Player
        playerCharacter = new Player();

        //Level
        TmxMapLoader loader = new TmxMapLoader();
        this.map = loader.load("Level Design/endless.tmx");

        renderer = new OrthogonalTiledMapRenderer(map);
        gameCam = new OrthographicCamera();

    }

    public void update(float f){
        gameCam.update();
        spawnEnemies(f);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        playerCharacter.update(delta);
        renderer.setView(playerCharacter.camera);
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

        // Loop through each essence and render it
        for (Essence essence : essences) {
            essence.render(batch);
        }

        batch.end();

        world.step(1 / 30f, 6, 2);
        debugRenderer.render(world, playerCharacter.camera.combined);
    }

    public Vector2 calculateSpawnPosition() {

        // Calculate the random angle
        float angle = (float) (Math.random() * 2 * Math.PI);

        // Calculate the spawn position
        float spawnX = playerCharacter.position.x + spawnDistance * (float) Math.cos(angle);
        float spawnY = playerCharacter.position.y + spawnDistance * (float) Math.sin(angle);

        return new Vector2(spawnX, spawnY);
    }

    public void spawnEnemies(float f) {

        // Update the time since the last enemy wave
        timeSinceEnemyWave += f;

        // Check the time since the last enemy spawned
        if (timeSinceEnemyWave >= 10 && enemies.size() < 30) {
            for (int i = 0; i < enemySpawnCount; i++) {
                // Calculate the spawn position
                Vector2 enemySpawnPos = calculateSpawnPosition();

                // For debugging
                //System.out.println("Enemy Spawn Position: " + enemySpawnPos);
                //System.out.println("Player Spawn Position: " + playerCharacter.position);

                // Create skeleton enemy
                skeletonEnemy = new SkeletonEnemy(this.assetManager, enemySpawnPos, enemyHealth, world);

                skeletonEnemy.create();
                // Add the enemy to the enemies array
                enemies.add(skeletonEnemy);
                timeSinceEnemyWave = 0; // Reset the timer
            }
            // Check the spawn count, it should not exceed the maximum
            if (enemySpawnCount < 5) {
                // Add slightly more enemies each wave that spawns
                enemySpawnCount = Math.round(enemySpawnCount * 1.25f);
            }
            // Increase enemy health each wave
            enemyHealth *= 1.25f; // Change value as needed
        }

        // Create enemy iterator array
        Iterator<Enemy> enemyIter = enemies.iterator();

        // Loop as long as there is another enemy
        while (enemyIter.hasNext()) {

            Enemy enemy = enemyIter.next();
            enemy.update(f, playerCharacter, enemyIter);

            if (enemy.getHealth() <= 0 && !enemy.getCurrentState().equals("DEATH")) {
                enemy.setCurrentState("DEATH");
            }
        }
    }

    public Iterator<Enemy> getEnemyIter(Iterator<Enemy> enemyIter) {
        return enemyIter;
    }

    public void addEssence(Essence essence) {
        essences.add(essence);
    }

    public void removeEssence(Essence essence) {
        essences.remove(essence);
    }

    @Override
    public void dispose(){
        map.dispose();
        renderer.dispose();
        world.dispose();
        debugRenderer.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height){
        gameCam.viewportWidth = width;
        gameCam.viewportHeight = height;
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
