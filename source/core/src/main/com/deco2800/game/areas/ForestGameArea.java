package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory.TerrainType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.friendlyunits.gamearea.GameAreaDisplay;
import com.deco2800.game.worker.WorkerFactory;
import com.deco2800.game.worker.resources.StoneFactory;
import com.deco2800.game.worker.resources.TreeFactory;
import com.deco2800.game.worker.type.MinerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/** Forest area for the demo game with trees, a player, and some enemies. */
public class ForestGameArea extends GameArea {
  private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
  private static final int NUM_TREES = 7;
  private static final int NUM_GHOSTS = 2;

  private static final GridPoint2 PLAYER_SPAWN = new GridPoint2(10, 10);
  private static final GridPoint2 WORKER_SPAWN = new GridPoint2(20, 20);
  private static final GridPoint2 MINER_SPAWN = new GridPoint2(20, 20);
  private static final GridPoint2 FORAGER_SPAWN = new GridPoint2(20, 15);
  private static final GridPoint2 STONE_SPAWN = new GridPoint2(23, 20);
  private static final GridPoint2 TREE_SPAWN = new GridPoint2(23, 15);
  private static final GridPoint2 BASE_SPAWN = new GridPoint2(23, 15);

  private static final float WALL_WIDTH = 0.1f;
  private static final String[] forestTextures = {
    "images/base.png",
    "images/forager.png",
    "images/box_boy_leaf.png",
    "images/tree.png",
    "images/Base.png",
    "images/barracks atlantis.png",
    "images/barracks medieval.png",
    "images/ghost_king.png",
    "images/ghost_1.png",
    "images/grass_1.png",
    "images/grass_2.png",
    "images/grass_3.png",
    "images/wall_1.png",
    "images/hex_grass_1.png",
    "images/hex_grass_2.png",
    "images/hex_grass_3.png",
    "images/iso_grass_1.png",
    "images/iso_grass_2.png",
    "images/iso_grass_3.png"
  };
  private static final String[] forestTextureAtlases = {
    "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas"
  };
  private static final String[] forestSounds = {"sounds/Impact4.ogg"};
  private static final String backgroundMusic = "sounds/BGM_03_mp3.mp3";
  private static final String[] forestMusic = {backgroundMusic};

  private static final float largeScale = 7f;

  private static final float mediumScale = 5f;

  private static final float smallScale = 3f;

  private final TerrainFactory terrainFactory;

  private Entity player;

  public ForestGameArea(TerrainFactory terrainFactory) {
    super();
    this.terrainFactory = terrainFactory;
  }

  /** Create the game area, including terrain, static entities (trees), dynamic entities (player) */
  @Override
  public void create() {
    loadAssets();

    displayUI();

    spawnTerrain();
    //spawnTrees();
    // spawnWalls();
    spawnTownHall(new GridPoint2(9, 7));
//    spawnBarracks(new GridPoint2(3, 6));
    spawnWalls(0,3);
    player = spawnPlayer();
  }


  private void displayUI() {
    Entity ui = new Entity();
    ui.addComponent(new GameAreaDisplay("TOP LEFT"));
    spawnEntity(ui);
  }

  private void spawnTerrain() {
    // Background terrain
    terrain = terrainFactory.createTerrain(TerrainType.FOREST_DEMO_ISO);
    spawnEntity(new Entity().addComponent(terrain));

    // Move camera to player
    MapGenerator mg = terrainFactory.getMapGenerator();
    Map<String, Coordinate> cityDetails = mg.getCityDetails();
    Coordinate centre = cityDetails.get("Centre");
    Vector2 centreWorld = terrain.tileToWorldPosition(centre.getX(), mg.getHeight() - centre.getY());
    terrainFactory.getCameraComponent().getEntity().setPosition(centreWorld);

    // Terrain walls
    float tileSize = terrain.getTileSize();
    GridPoint2 tileBounds = terrain.getMapBounds(0);
    Vector2 worldBounds = new Vector2(tileBounds.x * tileSize, tileBounds.y * tileSize);

    for (int i = 0; i < mg.getWidth(); i++) {
      for (int j = 0; j < mg.getHeight(); j++) {
        if (mg.getMap()[j][i] == mg.getOceanChar()) {
          Vector2 test = terrain.tileToWorldPosition(i,j);
          GridPoint2 coord = new GridPoint2(i, mg.getHeight()-j);
          spawnEntityAt(
                  ObstacleFactory.createWall(tileSize - 0.7f, tileSize - 0.7f),
                  new GridPoint2(i, mg.getHeight() - j),
                  true,
                  false
          );
        }
      }
    }

  }

  /**
   * Creates and spawns a new Stone entity.
   */
  private void spawnStone() {
    Entity newStone = StoneFactory.createStone();
    spawnEntityAt(newStone, STONE_SPAWN, true, true);
  }

  /**
   * Creates and spawns a new TreeFactory entity.
   */
  private void spawnTree() {
    Entity newTree = TreeFactory.createTree();
    spawnEntityAt(newTree, TREE_SPAWN, true, true);
  }

  /**
   * Creates and spawns a new Miner unit
   */
  private void spawnMiner(){
    Entity newMiner = MinerFactory.createMiner();
    spawnEntityAt(newMiner, MINER_SPAWN, true, true);
  }

  /**
   * Creates a new Worker unit.
   */
  private void spawnWorker() {
    Entity newWorker = WorkerFactory.createWorker();
    spawnEntityAt(newWorker, WORKER_SPAWN, true, true);
  }

  private void spawnTrees() {
    GridPoint2 minPos = new GridPoint2(0, 0);
    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);

    Entity tree = ObstacleFactory.createTree();
    spawnEntityAt(tree, new GridPoint2(0, 0), false, false);
  }

  private void spawnWalls(int xPos, int yPos) {

    for (int i=0; i<15; i++) {
      Entity wall = BuildingFactory.createWall();
      wall.setScale(1f, 2f);
      spawnEntityAt(wall, new GridPoint2(xPos+i, yPos), true, true);

    }
    for (int i=0; i<15; i++) {
      Entity wall = BuildingFactory.createWall();
      wall.setScale(1f, 2f);
      spawnEntityAt(wall, new GridPoint2(xPos+i, yPos+7), true, true);

    }
    for (int i=0; i<7; i++) {
      Entity wall = BuildingFactory.createWall();
      wall.setScale(1f, 2f);
      spawnEntityAt(wall, new GridPoint2(xPos+16, yPos+i), true, true);
    }
    for (int i=0; i<7; i++) {
      Entity wall = BuildingFactory.createWall();
      wall.setScale(1f, 2f);
      spawnEntityAt(wall, new GridPoint2(xPos, yPos+i), true, true);
    }
  }

  private void spawnTownHall(GridPoint2 position) {
    Entity townHall = BuildingFactory.createTownHall();
    townHall.setScale(largeScale, largeScale);
    spawnEntityAt(townHall, position, true, true);
  }

//  private void spawnBarracks(GridPoint2 position) {
//    Entity barracks = BuildingFactory.createBarracks(terrainFactory);
//    barracks.setScale(mediumScale, mediumScale);
//    spawnEntityAt(barracks, position, true, true);
//  }

  private Entity spawnPlayer() {

    Entity newPlayer = PlayerFactory.createPlayer();
    spawnEntity(newPlayer);

    return newPlayer;
  }



//  private void spawnGhosts() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    for (int i = 0; i < NUM_GHOSTS; i++) {
//      GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//      Entity ghost = EnemyFactory.createGhost(player);
//      spawnEntityAt(ghost, randomPos, true, true);
//    }
//  }

//  private void spawnGhostKing() {
//    GridPoint2 minPos = new GridPoint2(0, 0);
//    GridPoint2 maxPos = terrain.getMapBounds(0).sub(2, 2);
//
//    GridPoint2 randomPos = RandomUtils.random(minPos, maxPos);
//    Entity ghostKing = EnemyFactory.createGhostKing(player);
//    spawnEntityAt(ghostKing, randomPos, true, true);
//  }

  private void playMusic() {
    //Music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);
    //music.setLooping(true);
    //music.setVolume(0.3f);
    //music.play();
  }

  private void loadAssets() {
    logger.debug("Loading assets");
    ResourceService resourceService = ServiceLocator.getResourceService();
    resourceService.loadTextures(forestTextures);
    resourceService.loadTextureAtlases(forestTextureAtlases);
    //resourceService.loadSounds(forestSounds);
    //resourceService.loadMusic(forestMusic);

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
    //resourceService.unloadAssets(forestSounds);
    //resourceService.unloadAssets(forestMusic);
  }

  @Override
  public void dispose() {
    super.dispose();
    //ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class).stop();
    this.unloadAssets();
  }
}
