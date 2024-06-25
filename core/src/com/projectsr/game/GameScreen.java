package com.projectsr.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

public class GameScreen implements Screen {
    mainGame  game;
    SpriteBatch batch;
    private AssetManager assetManager;
    private Box2DDebugRenderer debugRenderer;

    //HUB
    private Hub hub;

    // Level
    private World world;
    private TiledMap map;
    private int tileSize;
    private OrthogonalTiledMapRenderer mapRenderer;
    private Viewport viewport;

    // Player
    Texture playerTex;
    Player playerCharacter;

    OrthographicCamera gameCam;

    // Enemies
    private Enemy skeletonEnemy;
    private ArrayList<Enemy> enemies = new ArrayList<>();
    private float timeSinceEnemyWave = 10;
    private int enemySpawnCount = 5;
    private float spawnDistance = 500;
    private float enemyHealth = 50;

    private ArrayList<Essence> essences = new ArrayList<>();

    // Audio
    private Music gameMusic;

    public GameScreen(mainGame game, AssetManager assetManager) {
        this.assetManager = assetManager;
        this.world = new World(new Vector2(0, 0), true); // No gravity
        this.debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new GameContactListener()); // Collision
    }

    public void create(){

        batch = new SpriteBatch();

        //Player
        playerCharacter = new Player(world);

        //HUB
        hub = new Hub();
        hub.create();

        //Level
        map = new TmxMapLoader().load("Level Design/endless.tmx");
        tileSize = ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth();
        mapRenderer = new OrthogonalTiledMapRenderer(map, 1f);
        viewport = new ExtendViewport(30 * tileSize, 20 * tileSize);
        gameCam = new OrthographicCamera();
        parseMap();

        // Audio
        gameMusic = assetManager.get("Music/The Pirate And The Dancer.mp3", Music.class);
        gameMusic.play();
        gameMusic.setVolume(1.0f);
        gameMusic.setLooping(true);
    }

    public void update(float f){
        gameCam.update();
        spawnEnemies(f);
    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        playerCharacter.update(delta, enemies, essences);
        mapRenderer.setView(playerCharacter.camera);
        mapRenderer.render();



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

        //Render the Hub
        hub.render();
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
                skeletonEnemy = new SkeletonEnemy(this.assetManager, enemySpawnPos, enemyHealth, world, this);

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
    private void parseMap() {
        TiledMapTileLayer layer = (TiledMapTileLayer) map.getLayers().get(0);
        for (int x = 0; x < layer.getWidth(); x++) {
            for (int y = 0; y < layer.getHeight(); y++) {
                TiledMapTileLayer.Cell cell = layer.getCell(x, y);
                if (cell == null)
                    continue;

                MapObjects cellObjects = cell.getTile().getObjects();
                if (cellObjects.getCount() != 1)
                    continue;

                MapObject mapObject = cellObjects.get(0);

                if (mapObject instanceof RectangleMapObject) {
                    RectangleMapObject rectangleObject = (RectangleMapObject) mapObject;
                    Rectangle rectangle = rectangleObject.getRectangle();

                    float centerX = (x + 0.5f) * tileSize;
                    float centerY = (y + 0.5f) * tileSize;

                    BodyDef bodyDef = new BodyDef();
                    bodyDef.position.set(centerX, centerY);

                    Body body = world.createBody(bodyDef);
                    PolygonShape polygonShape = new PolygonShape();
                    polygonShape.setAsBox(tileSize / 2f, tileSize / 2f);
                    body.createFixture(polygonShape, 0.0f);
                    polygonShape.dispose();
                }
            }
        }
    }
    private BodyDef getBodyDef(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        return bodyDef;
    }

    @Override
    public void dispose() {
        map.dispose();
        world.dispose();
        mapRenderer.dispose();
        debugRenderer.dispose();
        batch.dispose();
    }

    @Override
    public void resize(int width, int height){

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
    public void hide() { }

}
