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
import com.deco2800.game.worker.WorkerBaseFactory;
import com.deco2800.game.worker.resources.TreeFactory;
import com.deco2800.game.worker.type.ForagerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;

/** Atlantis game area for creating the map the game is played in */
public class AtlantisGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final int NUM_TREES = 5;
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
        //spawnForager();
        //spawnWorkerBase();
        //spawnTrees();
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
     * Spawns forager at the centre of the Atlantean city
     * @return entity corresponding to the spawned forager
     */
    private Entity spawnForager() {
        GridPoint2 spawn = RandomPointGenerator.getCityCenter(terrainFactory);
        Entity newForager = ForagerFactory.createForager();
        spawnEntityAt(newForager, spawn, true, true);
        return newForager;
    }

    /**
     * Randomly spawns a worker base on the map
     */
    private void spawnWorkerBase() {
        GridPoint2 randomPos = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        Entity workerBase = WorkerBaseFactory.createWorkerBase();
        spawnEntityAt(workerBase, randomPos, false, false);
    }

    /**
     * Spawns random tree within the city which is used by a forager to collect wood.
     *
     */
    private void spawnTrees() {
        for (int i = 0; i < NUM_TREES; i++) {
            GridPoint2 randomPos = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.75);
            Entity tree = TreeFactory.createTree();
            spawnEntityAt(tree, randomPos, false, false);
        }
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
