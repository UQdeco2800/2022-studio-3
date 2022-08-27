package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.utils.math.RandomUtils;
import com.deco2800.game.worker.WorkerBaseFactory;
import com.deco2800.game.worker.type.ForagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/** Atlantis game area for creating the map the game is played in */
public class AtlantisGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);

    private static final String[] forestTextures = {
            "images/Ocean.png",
            "images/Sand.png",
            "images/Grass.png",
            "images/box_boy_leaf.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/base.png"
    };
    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/forager.atlas"
    };
    private static final String[] atlantisSounds = {"sounds/Impact4.ogg"};
    private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
    private static final String[] atlantisMusic = {backgroundMusic};

    private final AtlantisTerrainFactory terrainFactory;

    private Entity player;

    public AtlantisGameArea(AtlantisTerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }

    /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        //player = spawnPlayer();
        playMusic();
        spawnForager();
        spawnWorkerBase();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Atlantis' Legacy"));
        spawnEntity(ui);
    }

    private void spawnTerrain() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        //Create map
        terrain = terrainFactory.createAtlantisTerrainComponent();
        spawnEntity(new Entity().addComponent(terrain));
        //Set tile size for camera
        terrainFactory.getCameraComponent().getEntity().getComponent(CameraInputComponent.class)
                .setMapDetails(terrain.getTileSize(), mg.getWidth(), mg.getHeight());

        //Spawn boundaries where each ocean tile is
        spawnIslandBounds();

        //Spawn boundaries around the map itself
        spawnMapBounds();
    }

    /**
     * Spawns invisible boundaries where each ocean tile is to box the island in
     */
    private void spawnIslandBounds() {
        // Terrain boundaries
        MapGenerator mg = terrainFactory.getMapGenerator();
        float tileSize = terrain.getTileSize();

        for (int i = 0; i < mg.getWidth(); i++) {
            for (int j = 0; j < mg.getHeight(); j++) {
                if (mg.getMap()[j][i] == mg.getOceanChar()) {
                    //Spawn collider entities at each ocean tile - scaled down by 0.7f arbitrarily
                    spawnEntityAt(
                            ObstacleFactory.createWall(tileSize - 0.7f, tileSize - 0.7f),
                            new GridPoint2(i, mg.getHeight() - j),
                            true,
                            false);
                }
            }
        }
    }

    /**
     * Creates an invisible wall of obstacles around the boundaries of the map so entities cannot escape
     */
    private void spawnMapBounds() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        float tileSize = terrain.getTileSize();
        //North/South side bounds
        for (int i = -1; i < mg.getWidth(); i ++) {
            spawnEntityAt(
                    ObstacleFactory.createWall(tileSize - 0.7f, tileSize - 0.7f),
                    new GridPoint2(i, mg.getHeight()),
                    true,
                    false);
            spawnEntityAt(
                    ObstacleFactory.createWall(tileSize - 0.7f, tileSize - 0.7f),
                    new GridPoint2(i, -1),
                    true,
                    false);
        }
        //East/West side bounds
        for (int j = -1; j < mg.getHeight(); j ++) {
            spawnEntityAt(
                    ObstacleFactory.createWall(tileSize - 0.7f, tileSize - 0.7f),
                    new GridPoint2(mg.getWidth(), j),
                    true,
                    false);
            spawnEntityAt(
                    ObstacleFactory.createWall(tileSize - 0.7f, tileSize - 0.7f),
                    new GridPoint2(-1, j),
                    true,
                    false);
        }
    }

    /**
     * Spawns player at the centre of the Atlantean city
     * @return Entity corresponding to the spawned player
     */
    private Entity spawnPlayer() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        //Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Store centre of city
        Coordinate centre = cityDetails.get("Centre");
        //Spawn player at centre of city
        GridPoint2 spawn = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());

        Entity newPlayer = PlayerFactory.createPlayer();
        spawnEntityAt(newPlayer, spawn, true, true);

        //Move camera to player
        Vector2 centreWorld = terrain.tileToWorldPosition(spawn.x, spawn.y);
        terrainFactory.getCameraComponent().getEntity().setPosition(centreWorld);

        return newPlayer;
    }

    /**
     * Randomly spawns a worker base on the map
     */
    private void spawnWorkerBase() {
        GridPoint2 minPos = new GridPoint2(0, 0);
        GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
        GridPoint2 randomPos = getRandomPointInRange(0.25);
        Entity workerBase = WorkerBaseFactory.createWorkerBase();
        spawnEntityAt(workerBase, randomPos, false, false);
    }

    /**
     * Get a random point within a certain range of the map centre.
     *
     * @param scale the scaling factor for the city map (between 0 and 1)
     * @throws IllegalArgumentException if scale < 0 or scale 1
     *
     * @return a random point in the range
     */
    public GridPoint2 getRandomPointInRange(double scale) throws IllegalArgumentException {
        if (scale < 0 || scale > 1) {
            throw new IllegalArgumentException("Must be a ratio between 0.0 and 1.0!");
        }
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Get top left-corner of city
        Coordinate topLeftCorner = cityDetails.get("NW");
        // Get bottom right-corner of city
        Coordinate bottomRightCorner = cityDetails.get("SE");
        // Get width and height of city
        float width = bottomRightCorner.getX() - topLeftCorner.getX();
        float height = bottomRightCorner.getY() - topLeftCorner.getY();
        // Re-scale width and height and divide by 2
        int rescaledHalfWidth = (int) (scale * width / 2);
        int rescaledHalfHeight = (int) (scale * height / 2);
        // Get city center
        Coordinate center = cityDetails.get("Centre");
        // Rescale corners
        Coordinate rescaledTopLeftCorner = new Coordinate(
                center.getX() - rescaledHalfWidth,
                center.getY() - rescaledHalfHeight
        );
        Coordinate rescaledBottomRightCorner = new Coordinate(
                center.getX() + rescaledHalfWidth,
                center.getY() + rescaledHalfHeight
        );
        // Convert to grid points
        GridPoint2 topLeftGridPoint = new GridPoint2(
                rescaledTopLeftCorner.getX(),
                mg.getHeight() - rescaledBottomRightCorner.getY()
        );
        GridPoint2 bottomRightGridPoint = new GridPoint2(
                rescaledBottomRightCorner.getX(),
                mg.getHeight() - rescaledTopLeftCorner.getY()
        );
        System.out.println(topLeftGridPoint);
        System.out.println(bottomRightGridPoint);
        // Return random point in range
        return RandomUtils.random(topLeftGridPoint, bottomRightGridPoint);
    }

    /**
     * Get the centre of the city
     */
    public GridPoint2 getCityCenter() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Store centre of city
        Coordinate centre = cityDetails.get("Centre");
        return new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());
    }

    /**
     * Spawns forager at the centre of the Atlantean city
     * @return entity corresponding to the spawned forager
     */
    private Entity spawnForager() {
        GridPoint2 spawn = getCityCenter();

        Entity newForager = ForagerFactory.createForager();
        spawnEntityAt(newForager, spawn, true, true);
        return newForager;
    }

    private void playMusic() {
        //Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        //music.setLooping(true);
        //music.setVolume(0.3f);
        //music.play();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(atlantisSounds);
        resourceService.loadMusic(atlantisMusic);

        while (!resourceService.loadForMillis(10)) {
            // This could be upgraded to a loading screen
            logger.info("Loading... {}%", resourceService.getProgress());
        }
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(forestTextures);
        resourceService.unloadAssets(forestTextureAtlases);
        resourceService.unloadAssets(atlantisSounds);
        resourceService.unloadAssets(atlantisMusic);
    }

    @Override
    public void dispose() {
        super.dispose();
        ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
        this.unloadAssets();
    }
}
