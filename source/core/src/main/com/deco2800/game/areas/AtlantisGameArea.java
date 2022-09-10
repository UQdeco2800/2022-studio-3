package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.maingame.DialogueBoxActions;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import com.deco2800.game.components.maingame.InfoBoxDisplay;
import com.deco2800.game.areas.terrain.MinimapComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.map.MapService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.worker.WorkerBaseFactory;
import com.deco2800.game.worker.resources.StoneFactory;
import com.deco2800.game.worker.resources.TreeFactory;
import com.deco2800.game.worker.type.ForagerFactory;
import com.deco2800.game.worker.type.MinerFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * Atlantis game area for creating the map the game is played in
 */
public class AtlantisGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final int NUM_TREES = 5;
    private static final int NUM_STONE = 10;
    private static final String[] forestTextures = {
            "test/files/dummyTexture.png",
            "test/files/dummyOcean.png",
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
            "images/Information_Box_Deepsea.png",
            "images/TransBox.png",
            "images/white.png",
            "images/barracks_level_1.0.png",
            "images/barracks_level_1.1.png",
            "images/barracks_level_1.2.png",
            "images/barracks_level_2.0.png",
            "images/stone_wall.png",
            "images/stone_wall_2_.png",
            "images/stone_wall_3.png",
            "images/base.png",
            "images/stone.png"
    };

    private static final String[] uiTextures = {
            "images/dialogue_box_pattern2_background.png",
            "images/dialogue_box_image_default.png",
            "images/exit-button.PNG"
    };
    private static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/forager_forward.atlas", "images/miner_forward.atlas"
    };
    private static final String[] atlantisSounds = {"sounds/Impact4.ogg"};
    private static final String backgroundMusic = "sounds/menu.wav";
    private static final String[] atlantisMusic = {backgroundMusic};

    private final AtlantisTerrainFactory terrainFactory;

    private final DialogueBoxDisplay dialogueBoxDisplay;

    private Entity player;

    public AtlantisGameArea(AtlantisTerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        dialogueBoxDisplay = new DialogueBoxDisplay();
    }

    /**
     * Create the game area, including terrain, static entities (trees), dynamic entities (player)
     */
    @Override
    public void create() {
        loadAssets();
        displayUI();
        spawnTerrain();
        player = spawnPlayer();
        for (int i = 0; i < 5; i++) {
            spawnPlayer();
        }
        //playMusic();

        // Spawn Buildings in the city
        spawnTownHall();
        spawnBarracks();
        spawnWalls();

        spawnForager();
        spawnForager();
        // spawnWorkerBase();
        spawnTrees();
        spawnStone();
        // spawnMiner();
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Atlantis' Legacy"));
        spawnEntity(ui);

        Entity infoUi = new Entity();
        infoUi.addComponent(new InfoBoxDisplay());
        spawnEntity(infoUi);

        Entity dialogueBox = new Entity();
        dialogueBoxDisplay.setDialogue("This is example dialogue text");
        dialogueBoxDisplay.setTitle("example title");
        dialogueBox.addComponent(dialogueBoxDisplay);
        dialogueBox.addComponent(new DialogueBoxActions(dialogueBoxDisplay));

        spawnEntity(dialogueBox);
    }

    private void spawnTerrain() {
        MapGenerator mg = terrainFactory.getMapGenerator();

        //Create map
        terrain = terrainFactory.createAtlantisTerrainComponent();
        // register map details with MapService (TODO: move somewhere nicer)
        ServiceLocator.getMapService().registerMapDetails(mg.getHeight(), mg.getWidth(), terrain.getTileSize());
        //Add minimap component
        MinimapComponent minimapComponent = new MinimapComponent(terrain.getMap(), (OrthographicCamera) terrainFactory.getCameraComponent().getCamera());
        spawnEntity(new Entity().addComponent(terrain).addComponent(minimapComponent));
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
        for (int i = -1; i < mg.getWidth(); i++) {
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
        for (int j = -1; j < mg.getHeight(); j++) {
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
     *
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

        Entity newPlayer = PlayerFactory.createPlayer().addComponent(new MapComponent());
        spawnEntityAt(newPlayer, spawn, true, true);

        //Move camera to player
        Vector2 centreWorld = terrain.tileToWorldPosition(spawn.x, spawn.y);
        terrainFactory.getCameraComponent().getEntity().setPosition(centreWorld);

        return newPlayer;
    }

    /**
     * Spawns TownHall in city center
     */
    private void spawnTownHall() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate centre = mg.getCityDetails().get("Centre");
        // Get GridPoint for the city centre
        GridPoint2 spawn = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());
        Entity townHall = BuildingFactory.createTownHall().addComponent(new MapComponent());
        spawnEntityAt(townHall, spawn.add(0, 2), true, true);
    }

    /**
     * Spawns two Barracks in locations next to the TownHall
     */
    private void spawnBarracks() {
        // Position offset from centre of city
        int offset = 10;
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate centre = mg.getCityDetails().get("Centre");
        // Two spawn-points for the barracks next ot TownHall located in the centre
        GridPoint2 spawn1 = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY()).add(offset, 0);
        GridPoint2 spawn2 = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY()).sub(offset, 0);

        spawnEntityAt(BuildingFactory.createBarracks(), spawn1, true, true);
        spawnEntityAt(BuildingFactory.createBarracks(), spawn2, true, true);
    }

    /**
     * Spawns corner bounding walls for the city
     * Sets texture accordingly to which direction (positive x or y) the wall should be pointing
     */
    private void spawnWalls() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate corner;
        GridPoint2 position;
        int yLength = 10; // Amount of walls to spawn in x direction
        int xLength = 20; // Amount of walls to spawn in y direction
        String[] cityCorners = {"NW", "NE", "SW", "SE"}; // Four corner locations to spawn walls in
        int direction = 1; // Spawning direction

        // Spawns walls in positive x direction from 2 left corners, and negative x direction from 2 right corners
        for (int n = 0; n < 4; n++) {
            corner = mg.getCityDetails().get(cityCorners[n]); // nth corner
            position = new GridPoint2(corner.getX(), mg.getHeight() - corner.getY() - 1); // position of nth corner

            // Absolute corner walls will have default wall texture (doesn't point in any direction)
            Entity wall = BuildingFactory.createWall();
            spawnEntityAt(wall, position, true, true);

            for (int i = 0; i < xLength; i++) {
                wall = BuildingFactory.createWall();
                // Sets wall texture which points in positive x direction
                wall.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/stone_wall_2_.png", Texture.class));
                spawnEntityAt(wall, position.add(direction, 0), true, true);
            }
            direction *= -1;
        }
        // Spawns walls in positive y direction from 2 bottom corners, and negative y direction from 2 top corners
        for (int n = 0; n < 4; n++) {
            direction = n<2 ? -1 : 1;
            corner = mg.getCityDetails().get(cityCorners[n]);
            position = new GridPoint2(corner.getX(), mg.getHeight() - corner.getY() - 1);

            for (int i = 0; i < yLength; i++) {
                Entity wall = BuildingFactory.createWall();
                spawnEntityAt(wall, position.add(0, direction), true, true);
                // Sets wall texture which points in negative y direction
                wall.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/stone_wall_3.png", Texture.class));
            }

        }
    }

    /**
     * Spawns forager at the centre of the Atlantean city
     *
     * @return entity corresponding to the spawned forager
     */
    private Entity spawnForager() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        Entity newForager = ForagerFactory.createForager().addComponent(new MapComponent());
        spawnEntityAt(newForager, spawn, true, true);
        return newForager;
    }

    /**
     * Creates and spawns a new Miner unit
     */
    private Entity spawnMiner() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        Entity newMiner = MinerFactory.createMiner().addComponent(new MapComponent());
        spawnEntityAt(newMiner, spawn, true, true);
        return newMiner;
    }

    /**
     * Randomly spawns a worker base on the map
     */
    private void spawnWorkerBase() {
        GridPoint2 randomPos = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        Entity workerBase = WorkerBaseFactory.createWorkerBase().addComponent(new MapComponent());
        spawnEntityAt(workerBase, randomPos, false, false);
    }

    /**
     * Spawns random tree within the city which is used by a forager to collect wood.
     */
    private void spawnTrees() {
        for (int i = 0; i < NUM_TREES; i++) {
            GridPoint2 randomPos = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.75);
            Entity tree = TreeFactory.createTree();
            spawnEntityAt(tree, randomPos, false, false);
        }
    }

    /**
     * Spawns random stones within the city which is used by a miner to collect stone.
     */
    private void spawnStone() {
        for (int i = 0; i < NUM_STONE; i++) {
            GridPoint2 randomPos = RandomPointGenerator.getRandomPointInRange(terrainFactory, 1);
            spawnEntityAt(StoneFactory.createStone(), randomPos, false, false);
        }
    }

    private void playMusic() {
        Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
        music.setLooping(true);
        music.setVolume(0.5f);
        music.play();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextures(uiTextures);
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
