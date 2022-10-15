package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.areas.MapGenerator.Buildings.Building;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.CityRow;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.FloodingGenerator;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.MapGenerator.ResourceSpecification;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.areas.terrain.MinimapComponent;
import com.deco2800.game.components.UnitSpawningComponent;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.components.buildingmenu.BuildingMenuDisplay;
import com.deco2800.game.components.friendlyunits.GestureDisplay;
import com.deco2800.game.components.friendlyunits.MouseInputComponent;
import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.areas.terrain.MinimapComponent;
import com.deco2800.game.areas.terrain.TerrainTile;
import com.deco2800.game.components.maingame.DialogueBoxActions;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import com.deco2800.game.components.maingame.Explosion;
import com.deco2800.game.components.maingame.InfoBoxDisplay;
import com.deco2800.game.components.soldiermenu.SoldierMenuDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UnitType;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.ObstacleFactory;
import com.deco2800.game.entities.factories.PlayerFactory;
import com.deco2800.game.entities.factories.UnitFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.WorkerBaseFactory;
import com.deco2800.game.worker.resources.MiningCampFactory;
import com.deco2800.game.worker.resources.TreeFactory;
import com.deco2800.game.worker.type.BuilderFactory;
import com.deco2800.game.worker.type.ForagerFactory;
import com.deco2800.game.worker.type.MinerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Target;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Atlantis game area for creating the map the game is played in
 */
public class AtlantisGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(ForestGameArea.class);
    private static final int NUM_TREES = 5;
    private static final int NUM_STONE = 10;
    public static final String[] forestTextures = {
            "test/files/dummyTexture.png",
            "test/files/dummyOcean.png",
            "images/Ocean.png",
            "images/Sand.png",
            "images/Grass.png",
            "images/city_tile.png",
//            "images/box_boy_leaf.png",
//            "images/box_boy.png",
//            "images/Base_Highlight.png",
//            "images/box_boy_highlight.png",
            "images/tree.png",
            "images/iso_grass_1.png",
//            "images/ghost_king.png",
//            "images/ghost_1.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/sea_1.png",
            "images/sea_2.png",
            "images/sea_3.png",
            "images/sea_4.png",
            "images/Waves tile/wave_1.png",
            "images/Waves tile/wave_2.png",
            "images/Waves tile/wave_3.png",
            "images/Waves tile/wave_4.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_1.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/Information_Box_Deepsea.png",
            "images/TransBox.png",
            "images/white.png",
            "images/stone.png",
            "images/bullet1.png",
            /* Building assets */
            // TownHall
            "images/base.png",
            "images/level 1 town hall.png",
            // Barracks
            "images/barracks_level_1.0.png",
            "images/barracks_level_1.0_Highlight.png",
            "images/barracks_level_1.1.png",
            "images/barracks_level_1.2.png",
            "images/barracks_level_2.0.png",
            "images/barracks_level_2.0_Highlight.png",
            //trebuchet
            "images/trebuchet.png",
            "images/Trebuchet-lv1-north.png",
            //Gate
            "images/gate_ew_closed.png",
            "images/gate_ew_open.png",
            "images/gate_ns_closed.png",
            "images/gate_ns_open.png",
            // Mine
            "mining_levelone_sketch.png",
            "mining_leveltwo_sketch.png",
            "images/mining_camp_level_one.png",
            // Walls
            "images/forager_avatar.png",
            "images/miner_avatar.png",
            "images/connector_ns.png",
            "images/connector_ew.png",
            "images/wall_pillar.png",
            "images/SpellIndicator/spelliso.png",
            "images/wooden_wall.png",
            "images/wooden_wall_2.png",
            "images/wooden_wall_3.png",
            "images/stone_wall.png",
            "images/stone_wall_2_.png",
            "images/stone_wall_3.png",
            "images/Base_Highlight",
            "images/level_1_town_hall_Highlight.png",
            "images/stone.png",
            "images/archer.png",
            "images/swordsman.png",
            "images/hoplite.png",
            "images/spearman.png",
            "images/simpleman.png",
            "images/titanshrine-default.png",
            "images/simpleman.png",
            // city buildings
            "images/highlightedBlacksmith.png",
            "images/blacksmith.png",
            "images/library.png",
            "images/rightFacingLibrary.png",
            "images/highlightedLibrary.png",
            "images/highlightedLeftFacingLibrary.png",
            "images/farm.png",
            "images/highlightedFarm.png",
            "images/pathTile.png",
            "images/spellbox-zeus.png",
            "images/spell-btn-unclickable.png",
            "images/spell-btn.png",
            "images/health bar_6.png",
            // Soldier display
            "images/archer_avatar.png",
            "images/swordsman_avatar.png",
            "images/spearman_avatar.png",
            "images/hoplite_avatar.png",
            "images/archer_sprite_sheet.png",

    };

    /* TODO: remove unused textures wasting precious resources */
    public static final String[] uiTextures = {
            "images/dialogue_box_pattern2_background.png",
            "images/dialogue_box_image_default.png",
            "images/exit-button.PNG",
            "images/dialogue_box_background_Deep_Sea.png"
    };

    public static final String[] buildingPlacementTextures = {
            "images/barracks_highlight_red.png",
            "images/barracks_highlight_green.png",
            "images/wooden_wall_green.png",
            "images/wooden_wall_red.png"
    };
    public static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/forager.atlas", "images/miner.atlas", "images/builder.atlas",
            "images/duration-bar.atlas", "images/archer.atlas", "images/swordsman.atlas",
            "images/hoplite.atlas", "images/spearman.atlas", "images/blue_joker.atlas",
            "images/snake.atlas", "images/wolf.atlas", "images/snake2.0.atlas", "images/titan.atlas",
            "images/newwolf.atlas", "images/ns_gate.atlas", "images/ew_gate.atlas",
            "images/newwolf.atlas", "images/forager.atlas","images/tree_.atlas",
            "images/spell.atlas", "images/titanshrine.atlas", "images/ship2.atlas"
    };
    public static final String[] soldierMenuTextures = {
            "images/character-selection-menu.png"
    };

    public static final String[] buildingMenuTextures = {
            "images/building-selection-menu.png"
    };
    public static final String[] atlantisSounds = {"sounds/Impact4.ogg"};

    Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/in-game-v3.wav"));

    private final AtlantisTerrainFactory terrainFactory;

    private DialogueBoxDisplay dialogueBoxDisplay;

    private Entity player;

    private Entity townHall;
    private Entity ship;
    private Entity titan;
    private FloodingGenerator floodingGenerator;
    private EventHandler gameAreaEventHandle;
    private Entity terrainMapAndMiniMap;

    public AtlantisGameArea(AtlantisTerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        gameAreaEventHandle = new EventHandler();
        this.floodingGenerator = new FloodingGenerator(this.terrainFactory, this);
    }

    /** Create the game area, including terrain, static entities (resources), dynamic entities (player) */
    @Override
    public void create() {
        gameAreaEventHandle.addListener("spawnSnake", this::spawnSnakes);
        gameAreaEventHandle.addListener("spawnHoplite", this::spawnHoplite);
        gameAreaEventHandle.addListener("spawnArcher", this::spawnArcher);
        gameAreaEventHandle.addListener("spawnSpearmint", this::spawnSpearman);
        gameAreaEventHandle.addListener("spawnTitan", this::spawnTitan);
        gameAreaEventHandle.addListener("spawnBlueJoker", this::spawnBlueJokers);

//        loadAssets();
        displayUI();
        spawnTerrain();
//        player = spawnPlayer();
        centreCameraOnCity();

//        spawnForager();
//        spawnForager();


//        spawnMiner();
//        spawnMiner();
//        spawnMiner();

        //playMusic();
//        player = spawnPlayer();
        centreCameraOnCity();

        // Spawn Buildings in the city
//        spawnTownHall();
//        spawnBarracks();
        //spawnWalls();
        spawnCityWalls();

        // spawnBuildings();

//        spawnForager();
//        spawnMiner();
//        spawnBuilder();
        spawnCity();

        spawnResources();

          spawnTitanShrine();
          spawnShip();
        spawnTrebuchet(titan, this);

        // spawnWorkerBase();
        // spawnResources();

        // spawnWorkerBase();
        // spawnMiner();

        // spawnExampleUnit();
        //spawnBlueJokers();

        spawnWolf();


        //spawnTitan();
        //spawnSnakes();

//        spawnUnit(UnitType.ARCHER, new GridPoint2(8,8));
//        spawnUnit(UnitType.SPEARMAN, new GridPoint2(-8,-8));
//        spawnUnit(UnitType.SWORDSMAN, new GridPoint2(8, -8));
//        spawnUnit(UnitType.HOPLITE, new GridPoint2(-8, 8));
        // spawnTrees();
        //spawnStone();
        //spawnMiner();

        spawnExplosion((new Explosion()).getEntity());
        ServiceLocator.registerGameArea(this);
        startFlooding();

//        spawnSoldierMenu();
//        spawnBuildingMenu();
    }

    private void spawnSoldierMenu() {
        Entity shopBox = new Entity();
        shopBox.addComponent(new SoldierMenuDisplay());
        spawnEntity(shopBox);
    }

    private void spawnBuildingMenu() {
        Entity buildingBox = new Entity();
        buildingBox.addComponent(new BuildingMenuDisplay());
        spawnEntity(buildingBox);
    }

    public void startFlooding() {
        Entity floodingEntity = new Entity();
        floodingEntity.addComponent(new FloodingGenerator(this.terrainFactory, this));
        ServiceLocator.getEntityService().register(floodingEntity);
    }

    /**
     * Spawns Blue Joker enemy entities
     */
    private void spawnBlueJokers(Vector2 spawnPoint) {
        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.BLUE);
        Entity blueJoker = EnemyFactory.createBlueJoker(terrainFactory).addComponent(mc);
        spawnEntityAt(blueJoker, spawnPoint, true, true);
    }

    /**
     * Spawns Snake enemy entities
     */
    public void spawnSnakes(Vector2 spawnPoint) {
        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.GREEN);
        Entity snake = EnemyFactory.createSnake(terrainFactory).addComponent(mc);
        System.out.println("Snake spawned at: " + spawnPoint.toString());
        spawnEntityAt(snake, spawnPoint, true, true);
    }

    public void spawnExplosion(Entity entity) {
        MapGenerator mg = terrainFactory.getMapGenerator();
        //Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Store centre of city
        Coordinate centre = cityDetails.get("Centre");
        //Spawn player at centre of city
        GridPoint2 spawn = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());

        MapComponent mapComponent = new MapComponent();
//        mapComponent.display();
//        mapComponent.setDisplayColour(Color.PURPLE);
//        entity.addComponent(mapComponent);
//        entity.setEnabled(false);
        spawnEntityAt(entity, spawn, true, true);
    }

    /**
     * Spawns Titan enemy entities
     */
    private void spawnTitan(Vector2 spawnPoint) {
        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.RED);
        Entity titan = EnemyFactory.createTitan(terrainFactory).addComponent(mc);
        titan.setEntityName("titan");
        spawnEntityAt(titan, spawnPoint, true, true);
    }

    /**
     * Spawns Wolf enemy entities
     */
    private void spawnWolf() {
        for (int i = 0; i < 10; i++) {
            GridPoint2 spawnPoint = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.9);
            MapComponent mc = new MapComponent();
            mc.display();
            mc.setDisplayColour(Color.WHITE);
            Entity wolf = EnemyFactory.createWolf(terrainFactory).addComponent(mc);
            wolf.setEntityName("wolf");
            spawnEntityAt(wolf, spawnPoint, true, true);
        }
    }

    private void displayUI() {
        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("Atlantis' Legacy"));
        spawnEntity(ui);


        Entity infoUi = new Entity();
        infoUi.addComponent(new InfoBoxDisplay());
//        infoUi.addComponent(new SpellUI());
        spawnEntity(infoUi);


        Entity spellsUi = new Entity();
        spellsUi.addComponent(new SpellUI());
        spellsUi.addComponent(new MouseInputComponent());
        spawnEntity(spellsUi);


        Entity gestureDisplay = new Entity();
        gestureDisplay.addComponent(new MouseInputComponent());
        gestureDisplay.addComponent(new GestureDisplay());
        spawnEntity(gestureDisplay);

        Entity dialogueBox = new Entity();
        this.dialogueBoxDisplay = new DialogueBoxDisplay();
        dialogueBox.addComponent(this.dialogueBoxDisplay);
        dialogueBox.addComponent(new DialogueBoxActions(this.dialogueBoxDisplay));

        //spawnEntity(dialogueBox);
    }

    private void spawnTerrain() {
        MapGenerator mg = terrainFactory.getMapGenerator();

        //Create map
        terrain = terrainFactory.createAtlantisTerrainComponent();
        // register map details with MapService (TODO: move somewhere nicer)
        ServiceLocator.getMapService().registerMapDetails(mg.getHeight(), mg.getWidth(), terrain.getTileSize());
        //Add minimap component
        MinimapComponent minimapComponent = new MinimapComponent(terrain.getMap(), (OrthographicCamera) terrainFactory.getCameraComponent().getCamera());
        // allow access to minimap via UI for dynamic resizing/positioning
        this.dialogueBoxDisplay.setMinimap(minimapComponent);
        this.terrainMapAndMiniMap = new Entity().addComponent(terrain).addComponent(minimapComponent);
        spawnEntity(terrainMapAndMiniMap);
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
                            ObstacleFactory.createWall(tileSize / 4f,
                                    tileSize / 4f),
                            new GridPoint2(i, mg.getHeight() - j - 1),
                            true,
                            false);
                }
            }
        }
    }

    /**
     * Lets us check whether a tile is in the ocean
     *
     * <p> Will be useful if changes from cleanup branch are adopted
     * @param tile the tile to check
     * @return whether it is ocean or not
     */
    public boolean isOcean(GridPoint2 tile) {
        return terrainFactory.getMapGenerator().getMap()[tile.y][tile.x]
                == terrainFactory.getMapGenerator().getOceanChar();
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

        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        mapComponent.setDisplayColour(Color.BLACK);
        Entity newPlayer = PlayerFactory.createPlayer().addComponent(mapComponent);
        spawnEntityAt(newPlayer, spawn, true, true);
        return newPlayer;
    }

    /**
     * Moves the camera to the centre of the city on game startup
     */
    private void centreCameraOnCity() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        //Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Store centre of city
        Coordinate centre = cityDetails.get("Centre");
        //Store location as GridPoint2
        GridPoint2 centrePoint = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());
        //Convert to world coordinates
        Vector2 centreWorld = terrain.tileToWorldPosition(centrePoint.x, centrePoint.y);
        //Move  camera
        terrainFactory.getCameraComponent().getEntity().setPosition(centreWorld);
    }

    /**
     * Spawns TownHall in city center
     */
    private void spawnTownHall() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate centre = mg.getCityDetails().get("Centre");
        // Get GridPoint for the city centre
        GridPoint2 spawn = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());
        MapComponent mapComponent = new MapComponent();
        mapComponent.setName("Town Hall");
        mapComponent.display();
        mapComponent.setDisplayColour(Color.BROWN);
        townHall = BuildingFactory.createTownHall().addComponent(mapComponent);
        spawnEntityAt(townHall, spawn.add(0, 2), true, true);
    }

    /**
     * Spawns the buildings and paths of the city.
     */
    private void spawnCity() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        BuildingGenerator bg = new BuildingGenerator(mg);
        spawnBuildings(bg,mg.getHeight());
        spawnPaths(bg, mg.getHeight(), mg.getCityDetails().get("NW").getY(), mg.getCityDetails().get("SW").getX());
    }

    /**
     * Generates the paths between city buildings.
     * @param bg
     */
    private void spawnPaths(BuildingGenerator bg, int height, int yOffset, int xOffset) {
        Texture t = ServiceLocator.getResourceService().getAsset("images/pathTile.png", Texture.class);
        TextureRegion tr = new TextureRegion(t);
        TerrainTile path = new TerrainTile(tr);
        char[][] city = bg.getPathGenerator().getCity();
        TiledMapTileLayer layer = (TiledMapTileLayer) terrain.getMap().getLayers().get(0);
        terrain.getMap().getLayers().remove(0);

        for (int y = 0; y < city.length; y++) {
            for (int x = 0; x < city[y].length; x++) {
                Cell cell = new Cell();
                if (city[y][x] == bg.getPathGenerator().getPathTile()) {
                    cell.setTile(path);
                    layer.setCell(x + xOffset, height - yOffset - 1 - y, cell);
                }
            }
        }

        terrain.getMap().getLayers().add(layer);
    }

    /**
     * Spawns all buildings in game
     */
    private void spawnBuildings(BuildingGenerator bg, int height) {
        List<CityRow> cityRows = bg.getCityRows();
        for (CityRow cr : cityRows) {
            List<Building> buildings = cr.getBuildings();
            for (Building building : buildings) {
                Coordinate placement = building.getPlacement();

                GridPoint2 spawn = new GridPoint2(placement.getX(), height - 1 - placement.getY() - building.getHeight());
                Entity buildingEntity = null;

                if (building.getName().equals("Town Hall")) {
                    // System.out.print("\n\nTH position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createTownHall();
                } else if (building.getName().equals("Library")) {
                    // System.out.print("\n\nTH position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createLibrary();
                } else if (building.getName().equals("Smith")) {
                    // System.out.print("\n\nTH position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createBlacksmith();
                } else if (building.getName().equals("Barracks")) {
                    buildingEntity = BuildingFactory.createBarracks().addComponent(new UnitSpawningComponent(gameAreaEventHandle));
                } else if (building.getName().equals("Farm")) {
                    // System.out.print("\n\nTH position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createFarm();
                } else {
                    // avoid null pointer exception
                    continue;
                }
                buildingEntity.getComponent(TextureScaler.class).setSpawnPoint(spawn, terrain);
                spawnEntity(buildingEntity);
            }
        }
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
        MapComponent mc1 = new MapComponent();
        mc1.display();
        mc1.setDisplayColour(Color.BROWN);
        MapComponent mc2 = new MapComponent();
        mc2.display();
        mc2.setDisplayColour(Color.BROWN);

//        spawnEntityAt((BuildingFactory.createBarracks().addComponent(mc1)).addComponent(new UnitSpawningComponent(gameAreaEventHandle)), spawn1, true, true);
//        spawnEntityAt((BuildingFactory.createBarracks().addComponent(mc2)).addComponent(new UnitSpawningComponent(gameAreaEventHandle)), spawn2, true, true);
    }

    /**
     * Spawns a titan shrine
     */
    private void spawnTitanShrine() {
        int range = 20;

        // To get spawn point
        GridPoint2 spawnPoint = RandomPointGenerator.getRandomPointInIsland(terrainFactory, range);

        MapComponent mc1 = new MapComponent();
        mc1.display();
        mc1.setDisplayColour(Color.DARK_GRAY);
        titan = BuildingFactory.createTitanShrine();
        spawnEntityAt((titan.addComponent(mc1))
                        .addComponent(new UnitSpawningComponent(gameAreaEventHandle)),
                       spawnPoint, false, false);
    }

    /**
     * Spawn Ship
     */
    private void spawnShip() {
        int range = 10;
        MapGenerator mg = terrainFactory.getMapGenerator();
        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.CORAL);

        GridPoint2 spawnPoint = RandomPointGenerator.getRandomPointInSea(terrainFactory, range);

        ship = BuildingFactory.createShip();
        ship.addComponent(mc).addComponent(new UnitSpawningComponent(gameAreaEventHandle));
        spawnEntityAt(ship, spawnPoint, false, false);
    //     spawnEntityAt(BuildingFactory.createBarracks(), spawn1, true, true);
    //     spawnEntityAt(BuildingFactory.createBarracks(), spawn2, true, true);
    }

    /**
     * Spawns corner bounding walls for the city
     * Sets texture accordingly to which direction (positive x or y) the wall should be pointing
     */
    private void spawnWalls() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate corner;
        GridPoint2 position;
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Find city height in tiles
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        //Find city width in tiles
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;

        int yLength = (cityHeight / 2) - 5; // Amount of walls to spawn in y direction
        int xLength = (cityWidth / 2) - 5; // Amount of walls to spawn in x direction
        String[] cityCorners = {"NW", "NE", "SW", "SE"}; // Four corner locations to spawn walls in
        int direction = 1; // Spawning direction

        // Spawns walls in positive x direction from 2 left corners, and negative x direction from 2 right corners
        for (int n = 0; n < 4; n++) {
            corner = mg.getCityDetails().get(cityCorners[n]); // nth corner
            position = new GridPoint2(corner.getX(), mg.getHeight() - corner.getY() - 1); // position of nth corner



            // Absolute corner walls will have default wall texture (doesn't point in any direction)
            Entity wall = BuildingFactory.createCornerWall();
            wall.getComponent(TextureScaler.class).setSpawnPoint(position, terrain);
            spawnEntity(wall);

            for (int i = 0; i < xLength; i++) {
                wall = BuildingFactory.createWall();
                wall.addComponent(new MapComponent());
                // Sets wall texture which points in positive x direction
                wall.getComponent(BuildingActions.class).addLevel();
                wall.getComponent(BuildingActions.class).setWallNE();
                spawnEntityAt(wall, position.add(direction, 0), true, true);
            }
            if (direction == 1) {
                //Spawn gate in the middle of the city edge - North/South orientation
                Entity gate = BuildingFactory.createNSGate();
                //Determine tile point to spawn gate
                GridPoint2 tileSpawn = position.add(direction, 0);
                //Set the spawn point of the gate
                gate.getComponent(TextureScaler.class).setSpawnPoint(tileSpawn, terrain);
                //Spawn the gate
                spawnEntity(gate);
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
                // Sets wall texture which points in negative y direction
                wall.getComponent(BuildingActions.class).addLevel();
                wall.getComponent(BuildingActions.class).setWallSE();
                spawnEntityAt(wall, position.add(0, direction), true, true);
            }

            if (direction == 1) {
                //Spawn east/west Gate
                Entity gate = BuildingFactory.createEWGate();
                GridPoint2 tileSpawn = position.add(0, direction);
                gate.getComponent(TextureScaler.class).setSpawnPoint(tileSpawn, terrain);
                spawnEntity(gate);
            }

        }
    }

    /**
     * Spawns ordered walls around the city at the correct locations
     */
    private void spawnCityWalls() {
        MapGenerator mg = terrainFactory.getMapGenerator();

        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Find city height in tiles
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        //Find city width in tiles
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;

        int gateLength = (int) BuildingFactory.GATE_SCALE;
        int pillarLength = (int) BuildingFactory.CORNER_SCALE;
        int wallLength = (int) BuildingFactory.CONNECTOR_SCALE;


        //Amount of joiners needed between gates
        //int nsJoinersNeeded = (cityWidth - minimumSideLength) / (2 * (pillarLength + wallLength));
        int nsJoinersNeeded = (cityWidth - gateLength) / (pillarLength + wallLength);
        int ewJoinersNeeded = (cityHeight - gateLength) / (pillarLength + wallLength);


        boolean direction = true;

        //Spawn NS gate entities
        GridPoint2 swSpawn = new GridPoint2(cityDetails.get("SW").getX(), mg.getHeight() - 1 - cityDetails.get("SW").getY());
        GridPoint2 nwSpawn = new GridPoint2(cityDetails.get("NW").getX(), mg.getHeight() - cityDetails.get("NW").getY() - pillarLength);
        //Spawn edge pillars on NW and SW edge of city
        for (int i = 1; i <= nsJoinersNeeded; i++) {
            //SW pillar
            Entity pillarSW = BuildingFactory.createCornerWall();
            GridPoint2 swPillarSpawn;
            //SW connector
            Entity connectorSW = BuildingFactory.createNSConnector();
            GridPoint2 swConnectorSpawn;
            //NW pillar
            Entity pillarNW = BuildingFactory.createCornerWall();
            GridPoint2 nwPillarSpawn;
            //NW connector
            Entity connectorNW = BuildingFactory.createNSConnector();
            GridPoint2 nwConnectorSpawn;

            if (direction) {
                swPillarSpawn = swSpawn.cpy();
                swConnectorSpawn = swSpawn.cpy().add(pillarLength, 0);
                nwPillarSpawn = nwSpawn.cpy();
                nwConnectorSpawn = nwSpawn.cpy().add(pillarLength, 0);
            } else {
                swPillarSpawn = swSpawn.cpy().add(wallLength, 0);
                swConnectorSpawn = swSpawn.cpy();
                nwPillarSpawn = nwSpawn.cpy().add(wallLength, 0);
                nwConnectorSpawn = nwSpawn.cpy();
            }

            pillarSW.getComponent(TextureScaler.class).setSpawnPoint(swPillarSpawn, terrain);
            spawnEntity(pillarSW);

            connectorSW.getComponent(TextureScaler.class).setSpawnPoint(swConnectorSpawn, terrain);
            spawnEntity(connectorSW);

            pillarNW.getComponent(TextureScaler.class).setSpawnPoint(nwPillarSpawn, terrain);
            spawnEntity(pillarNW);

            connectorNW.getComponent(TextureScaler.class).setSpawnPoint(nwConnectorSpawn, terrain);
            spawnEntity(connectorNW);

            swSpawn.add(pillarLength + wallLength, 0);
            nwSpawn.add(pillarLength + wallLength, 0);

            //Check to see if gate needs to be added
            if (i == nsJoinersNeeded / 2) {
                //Add SW gate
                Entity gateSW = BuildingFactory.createNSGate();
                gateSW.getComponent(TextureScaler.class).setSpawnPoint(swSpawn, terrain);
                //Spawn the gate
                spawnEntity(gateSW);
                swSpawn.add(gateLength, 0);

                //Add NW gate
                Entity gateNW = BuildingFactory.createNSGate();
                gateNW.getComponent(TextureScaler.class).setSpawnPoint(nwSpawn, terrain);
                //Spawn the gate
                spawnEntity(gateNW);
                nwSpawn.add(gateLength, 0);

                //Invert direction
                direction = !direction;
            }
        }

        //Spawn East/West gate entities
        direction = true;
        GridPoint2 neSpawn = new GridPoint2(cityDetails.get("NE").getX() - pillarLength + 1, mg.getHeight() - 1 - cityDetails.get("NE").getY() + 1 - pillarLength);
        nwSpawn = new GridPoint2(cityDetails.get("NW").getX(), mg.getHeight() - cityDetails.get("NW").getY() - pillarLength);
        for (int i = 1; i <= ewJoinersNeeded; i++) {
            //NE pillar
            Entity pillarNE = BuildingFactory.createCornerWall();
            GridPoint2 nePillarSpawn;
            //NE connector
            Entity connectorNE = BuildingFactory.createEWConnector();
            GridPoint2 neConnectorSpawn;
            //NW pillar
            Entity pillarNW = BuildingFactory.createCornerWall();
            GridPoint2 nwPillarSpawn;
            //NW connector
            Entity connectorNW = BuildingFactory.createEWConnector();
            GridPoint2 nwConnectorSpawn;

            if (direction) {
                nePillarSpawn = neSpawn.cpy();
                neConnectorSpawn = neSpawn.cpy().add(0, -wallLength);
                nwPillarSpawn = nwSpawn.cpy();
                nwConnectorSpawn = nwSpawn.cpy().add(0, -wallLength);
            } else {
                nePillarSpawn = neSpawn.cpy().add(0, -pillarLength - wallLength);
                neConnectorSpawn = neSpawn.cpy().add(0, -wallLength);
                nwPillarSpawn = nwSpawn.cpy().add(0, -pillarLength - wallLength);
                nwConnectorSpawn = nwSpawn.cpy().add(0, -wallLength);
            }

            //Don't add first or last pillar, as it already exists
            if (!(i == 1 || i == ewJoinersNeeded)) {
                pillarNE.getComponent(TextureScaler.class).setSpawnPoint(nePillarSpawn, terrain);
                spawnEntity(pillarNE);

                pillarNW.getComponent(TextureScaler.class).setSpawnPoint(nwPillarSpawn, terrain);
                spawnEntity(pillarNW);
            }

            connectorNE.getComponent(TextureScaler.class).setSpawnPoint(neConnectorSpawn, terrain);
            spawnEntity(connectorNE);

            connectorNW.getComponent(TextureScaler.class).setSpawnPoint(nwConnectorSpawn, terrain);
            spawnEntity(connectorNW);

            neSpawn.add(0, - (pillarLength + wallLength));
            nwSpawn.add(0, -(pillarLength + wallLength));

            //Check to see if gate needs to be added
            if (i == ewJoinersNeeded / 2) {
                //Add NE gate
                Entity gateNE = BuildingFactory.createEWGate();
                neSpawn.add(0, -gateLength + pillarLength);
                gateNE.getComponent(TextureScaler.class).setSpawnPoint(neSpawn, terrain);
                //Spawn the gate
                spawnEntity(gateNE);


                //Add NW gate
                Entity gateNW = BuildingFactory.createEWGate();
                nwSpawn.add(0, -gateLength + pillarLength);
                gateNW.getComponent(TextureScaler.class).setSpawnPoint(nwSpawn, terrain);
                //Spawn the gate
                spawnEntity(gateNW);


                //Invert direction
                direction = !direction;
            }
        }
    }

    private void spawnTrebuchet(Entity target, GameArea gameArea) {
        int offset = 20;
        MapGenerator mg = terrainFactory.getMapGenerator();
        char[][] map = mg.getMap();
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInIsland(terrainFactory, 10);
        spawnEntityAt((BuildingFactory.createTrebuchet(target, gameArea))
                        .addComponent(new UnitSpawningComponent(gameAreaEventHandle)), spawn,
                true, true);
    }

    /**
     * Spawns forager at the centre of the Atlantean city
     *
     * @return entity corresponding to the spawned forager
     */
    private Entity spawnForager() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        mapComponent.setDisplayColour(Color.GREEN);
        Entity newForager = ForagerFactory.createForager().addComponent(mapComponent);
        spawnEntityAt(newForager, spawn, true, true);
        return newForager;
    }

    /**
     * Creates and spawns a new Miner unit
     */
    private Entity spawnMiner() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        mapComponent.setDisplayColour(Color.YELLOW);
        Entity newMiner = MinerFactory.createMiner().addComponent(mapComponent);
        spawnEntityAt(newMiner, spawn, true, true);
        return newMiner;
    }

    private Entity spawnBuilder() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        mapComponent.setDisplayColour(Color.BLUE);
        Entity newBuilder = BuilderFactory.createBuilder().addComponent(mapComponent);
        spawnEntityAt(newBuilder, spawn, true, true);
        return newBuilder;
    }

    /**
     * Creates units for demonstration purposes
     *
     * Spawns them relative to city centre for convenience
     * @param type Which unit are we spawning? (see unit wiki)
     * @param location offset from centre of city
     */
    private void spawnUnit(UnitType type, GridPoint2 location) {
        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.GRAY);
        Entity unit = UnitFactory.createUnit(type).addComponent(mc);
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate cityCentre = mg.getCityDetails().get("Centre");
        spawnEntityAt(unit, new GridPoint2(cityCentre.getX(),
                mg.getHeight() - cityCentre.getY()).add(location.x, location.y)
                , true, false);
    }

    /**
     * Creates units for demonstration purposes
     *
     * Spawns them relative to city centre for convenience
     * @param type Which unit are we spawning? (see unit wiki)
     * @param location offset from centre of city
     */
    private void spawnUnit(UnitType type, Vector2 location) {
        Entity unit = UnitFactory.createUnit(type);
        MapGenerator mg = terrainFactory.getMapGenerator();
        Coordinate cityCentre = mg.getCityDetails().get("Centre");
        spawnEntityAt(unit, location, true, false);
    }

    private void spawnArcher(Vector2 location) {
        spawnUnit(UnitType.ARCHER, location);
    }

    private void spawnSwordsman(Vector2 location) {
        spawnUnit(UnitType.SWORDSMAN, location);
    }

    private void spawnSpearman(Vector2 location) {
        spawnUnit(UnitType.SPEARMAN, location);
    }

    private void spawnHoplite(Vector2 location) {
        spawnUnit(UnitType.HOPLITE, location);
    }

    /**
     * Randomly spawns a worker base on the map
     */
    private void spawnWorkerBase() {
        GridPoint2 randomPos = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        mapComponent.setDisplayColour(Color.BLUE);
        Entity workerBase = WorkerBaseFactory.createWorkerBase().addComponent(mapComponent);
        spawnEntityAt(workerBase, randomPos, false, false);
    }

    /**
     * Spawns all resources procedurally placed by ResourceGenerator
     */
    private void spawnResources() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        List<ResourceSpecification> resources = mg.getResourcePlacements();
        //Place each resource in the list
        for (ResourceSpecification rs : resources) {
            //Add a Minimap tracking component
            MapComponent mapComponent = new MapComponent();
            mapComponent.display();
            for (Coordinate placement : rs.getPlacements()) {
                GridPoint2 spawn = new GridPoint2(placement.getX(), mg.getHeight() - 1 - placement.getY());
                if (rs.getName().equals("Tree")) {
                    //Spawn a Tree entity
                    mapComponent.setDisplayColour(Color.FOREST);
                    spawnEntityAt(TreeFactory.createTree().addComponent(mapComponent), spawn, false, false);
                } else if (rs.getName().equals("Stone")) {
                    //Spawn a Stone entity
                    mapComponent.setDisplayColour(Color.DARK_GRAY);
                    //spawnEntityAt(StoneFactory.createStone().addComponent(mapComponent), spawn, false, false);
                    spawnEntityAt(MiningCampFactory.createMiningCamp().addComponent(mapComponent), spawn, false, false);
                }
            }
        }
    }

    private void playMusic() {
        //Music music = ServiceLocator.getResourceService().getAsset(backgroundMusic, Music.class);

        music.setLooping(false);
        music.setVolume(0.5f);
        music.play();
    }

    /**
     * Gets the entity(ies) in the area which match the given id.
     *
     * Used primarily for debugging purposes
     * @param id the id to search for
     * @return the entities matching the given ID
     */
    public List<Entity> getEntityByID(int id) {
        return areaEntities.stream().filter(x -> x.getId() == id).collect(Collectors.toList());
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextures(uiTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(atlantisSounds);
        resourceService.loadTextures(buildingPlacementTextures);
        resourceService.loadTextures(soldierMenuTextures);
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
        resourceService.unloadAssets(soldierMenuTextures);
    }

    @Override
    public void dispose() {
        super.dispose();
        music.stop();
        this.unloadAssets();
    }

    /**
     * Check that a shape would not collide with any placed entities
     *
     * <p>Assumes a convex polygon
     * @param shape the shape to test for collisions
     * @return true if there are no colliders in region, false otherwise
     */
    public boolean isRegionClear(PolygonShape shape) {
        for (Entity entity: areaEntities) {
            if(entity.getComponent(ColliderComponent.class) != null) {
                for (int i = 0; i < shape.getVertexCount(); i++) {
                    Vector2 vertex = new Vector2();
                    shape.getVertex(i, vertex);
                    if (entity.getComponent(ColliderComponent.class).getFixture().testPoint(vertex))
                        return false;
                }
            }
        }
        return true;
    }

    public void flood() {
        this.terrainMapAndMiniMap.dispose();
        MapGenerator mg = terrainFactory.getMapGenerator();
        terrain = terrainFactory.createAtlantisTerrainComponent();
        ServiceLocator.getMapService().registerMapDetails(mg.getHeight(), mg.getWidth(), terrain.getTileSize());
        MinimapComponent minimapComponent = new MinimapComponent(terrain.getMap(), (OrthographicCamera) terrainFactory.getCameraComponent().getCamera());
        // allow access to minimap via UI for dynamic resizing/positioning
        this.dialogueBoxDisplay.setMinimap(minimapComponent);
        this.terrainMapAndMiniMap = new Entity().addComponent(terrain).addComponent(minimapComponent);
        spawnEntity(terrainMapAndMiniMap);
//
//        spawnEntity(new Entity().addComponent(terrain).addComponent(minimapComponent));
    }
}
