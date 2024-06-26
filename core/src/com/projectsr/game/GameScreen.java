package com.projectsr.game;

import static com.projectsr.game.mainGame.deathScreen;

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
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * This gamescreen class is used for rendering the gameplay screen,
 * managing the player, enemies and the game world
 */
public class GameScreen implements Screen {
    private mainGame  game;
    SpriteBatch batch;
    private AssetManager assetManager;
    //private Box2DDebugRenderer debugRenderer;

    //HUB
    private Hub hub = null;

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
    private float enemyHealth = 30;

    private ArrayList<Essence> essences = new ArrayList<>();

    // Audio
    private Music gameMusic;

    /**
     * This constructs a game screen  with game and asset manager
     *
     * @param game which is the main game instance
     * @param assetManager which is the asset manager for loading the assets
     */
    public GameScreen(mainGame game, AssetManager assetManager) {
        this.game = game;
        this.assetManager = assetManager;
        this.world = new World(new Vector2(0, 0), true); // No gravity
        //this.debugRenderer = new Box2DDebugRenderer();
        world.setContactListener(new GameContactListener()); // Collision

    }

    /**
     * This initialises the game screen by setting up the game world, player,
     * enemies, map and audio
     */
    public void create() {

        batch = new SpriteBatch();

        //Player
        if (this.hub == null) {
            hub = new Hub(game);
        }
        playerCharacter = new Player(world, assetManager, game.craftingScreen);
        //HUB

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
        gameMusic.setVolume(Settings.getVolume());
        gameMusic.setLooping(true);

        float offset = 15.5f * ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth();
        float mapWidth = 180 * ((TiledMapTileLayer) map.getLayers().get(0)).getTileWidth();
        float mapHeight = 180 * ((TiledMapTileLayer) map.getLayers().get(0)).getTileHeight();

        createMapEdgeCollision(-offset, offset, mapWidth - offset, offset); // Bottom
        createMapEdgeCollision(-mapWidth + offset, mapHeight - offset, mapWidth - offset, mapHeight - offset); // Top
        createMapEdgeCollision(offset, offset, offset, mapHeight - offset); // Left
        createMapEdgeCollision(mapWidth - offset, -offset, mapWidth - offset, mapHeight - offset); // Right
    }

    /**
     * This creates the collision boundaries for the map
     *
     * @param startX  which is the starting x cord of the edge
     * @param startY which is the starting y cord of the edge
     * @param endX which is the ending x cord of the edge
     * @param endY which is the ending y cord of the edge
     */
    public void createMapEdgeCollision(float startX, float startY, float endX, float endY) {

        // Define the body
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(new Vector2(startX, startY));

        // Create the body in the world
        Body body = world.createBody(bodyDef);

        // Define the shape as an edge
        EdgeShape edgeShape = new EdgeShape();
        edgeShape.set(0, 0, endX - startX, endY - startY);

        // Define the fixture
        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = edgeShape;
        fixtureDef.friction = 0.5f;
        fixtureDef.restitution = 0.5f;
        fixtureDef.filter.categoryBits = GameContactListener.MAP_CATEGORY;
        fixtureDef.filter.maskBits = GameContactListener.PLAYER_CATEGORY;
        fixtureDef.isSensor = false;

        // Create the fixture on the body
        body.createFixture(fixtureDef);

        // Dispose of the shape
        edgeShape.dispose();
    }

    /**
     * This updates the game state  and spawn the enemies
     *
     * @param f which is the delta the seconds since last update
     */
    public void update(float f){
        gameCam.update();
        spawnEnemies(f);
    }

    /**
     * This renders the game screen
     *
     * @param delta The time in seconds since the last render.
     */
    public void render(float delta) {

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        update(delta);

        playerCharacter.update(delta);
        mapRenderer.setView(playerCharacter.camera);
        mapRenderer.render();

        /*
        BATCH BEGIN DRAW
         */
        batch.begin();

        // Check if the player is still alive
        if (!playerCharacter.isDead()) {
            playerCharacter.render(batch);

            // Loop through each enemy and render it
            for (Enemy enemy : enemies) {
                enemy.render(batch);
            }

            // Loop through each essence and render it
            for (Essence essence : essences) {
                essence.render(batch);
            }
        }
        else {
            // Loop through each essence and destroy the collision
            for (Essence essence : essences) {
                world.destroyBody(essence.getBody()); // Dispose of collision
            }
            // Loop through each enemy and destroy the collisions
            for (Enemy enemy : enemies) {
                world.destroyBody(enemy.body); // Dispose of collision
                world.destroyBody(enemy.attackBody);
            }
            // Clear the arrays

            essences.clear();
            enemies.clear();

            // Set the game screen to the death screen

            game.setScreen(deathScreen);
        }

        batch.end();

        world.step(1 / 30f, 6, 2);
        //debugRenderer.render(world, playerCharacter.camera.combined);

        //Render the Hub
        hub.render(delta);
    }

    /**
     * This calculates the spawn position for the enemies based on the player's position
     *
     * @return the spawn position as Verctor2
     */
    public Vector2 calculateSpawnPosition() {

        // Calculate the random angle
        float angle = (float) (Math.random() * 2 * Math.PI);

        // Calculate the spawn position
        float spawnX = playerCharacter.position.x + spawnDistance * (float) Math.cos(angle);
        float spawnY = playerCharacter.position.y + spawnDistance * (float) Math.sin(angle);

        return new Vector2(spawnX, spawnY);
    }

    /**
     * This spawns the enemies at regular intervals
     *
     * @param f which is the the seconds since last update
     */
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

    /**
     * This gets the iterator for the enemies
     *
     *
     * @param enemyIter which is the iterator for the enemies
     * @return the iterator for the enemies
     */
    public Iterator<Enemy> getEnemyIter(Iterator<Enemy> enemyIter) {
        return enemyIter;
    }

    /**
     * This adds an essence to the list of essences
     *
     * @param essence which is the essence to add
     */
    public void addEssence(Essence essence) {
        essences.add(essence);
    }

    /**
     * This removes an essence from the list of essences
     *
     * @param essence which is the essence to remove
     */
    public void removeEssence(Essence essence) {
        essences.remove(essence);
    }

    /**
     * This parses the map to create collision objects
     */
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

    /**
     * This creates a body definition for the coordinates
     *
     * @param x  which is the x -cord
     * @param y  which is the y -cord
     * @return the body definition
     */
    private BodyDef getBodyDef(float x, float y) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(x, y);

        return bodyDef;
    }

    /**
     * This gets the hun instance
     *
     * @return the hub's instance
     */
    public Hub getHub() {
        return hub;
    }

    /**
     * This disposes of the game screen resources
     */
    @Override
    public void dispose() {
        map.dispose();
        world.dispose();
        mapRenderer.dispose();
        //debugRenderer.dispose();
        batch.dispose();
        assetManager.dispose();
    }

    /**
     * This resizes the viewport to the width and height
     *
     * @param width
     * @param height
     */
    @Override
    public void resize(int width, int height){
        viewport.update(width, height, true);
    }

    /**
     * This pauses the game screen
     */
    @Override
    public void pause() { }

    /**
     * This resumes the game screen
     */
    @Override
    public void resume() { }

    /**
     * This shows the game screen
     */
    @Override
    public void show() {
        create();
    }

    /**
     * This hides the game screen
     */
    @Override
    public void hide() {
        gameMusic.stop();
    }

}
