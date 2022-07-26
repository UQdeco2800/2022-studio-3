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
import com.deco2800.game.components.UnitSpawningComponent;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.components.buildingmenu.BuildingMenuDisplay;
import com.deco2800.game.components.floodtimer.FloodTimerDisplay;
import com.deco2800.game.components.friendlyunits.GestureDisplay;
import com.deco2800.game.components.friendlyunits.MouseInputComponent;
import com.deco2800.game.components.friendlyunits.gamearea.GameAreaDisplay;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.components.maingame.DialogueBoxActions;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import com.deco2800.game.components.maingame.Explosion;
import com.deco2800.game.components.soldiermenu.SoldierMenuDisplay;
//import com.deco2800.game.components.gamearea.GameAreaDisplay;
import com.deco2800.game.components.maingame.SpellUI;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UnitType;
import com.deco2800.game.entities.factories.*;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.factories.ArcherFactory;
import com.deco2800.game.soldiers.factories.HopliteFactory;
import com.deco2800.game.soldiers.factories.SpearmanFactory;
import com.deco2800.game.soldiers.factories.SwordsmanFactory;
import com.deco2800.game.utils.math.Vector2Utils;
import com.deco2800.game.worker.WorkerBaseFactory;
import com.deco2800.game.worker.resources.MiningCampFactory;
import com.deco2800.game.worker.resources.TreeFactory;
import com.deco2800.game.worker.type.BuilderFactory;
import com.deco2800.game.worker.type.ForagerFactory;
import com.deco2800.game.worker.type.MinerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Atlantis game area for creating the map the game is played in
 */
public class AtlantisGameArea extends GameArea {
    private static final Logger logger = LoggerFactory.getLogger(AtlantisGameArea.class);
    public static final String[] forestTextures = {
            "test/files/dummyTexture.png",
            "test/files/dummyOcean.png",
            "test/files/cityMinimap.png",
            "images/Ocean.png",
            "images/Sand.png",
            "images/sand_shell.png",
            "images/sand_starfish.png",
            "images/Grass.png",
            "images/city_tile.png",
//            "images/box_boy_leaf.png",
//            "images/box_boy.png",
            "images/Base_Highlight.png",
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
            "images/flash_1.png",
            "images/flash_2.png",
            "images/flash_3.png",
            "images/flash_4.png",
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
            //Features
            "images/waterFeatureDefault.png",
            "images/lampDefault.png",
            "images/bush.png",
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
            //ship
            "images/ship_default.png",
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
            "images/Base_Highlight",
            "images/level_1_town_hall_Highlight.png",
            "images/stone.png",
            "images/archerstatic.png",
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
            "images/static_trebuchet.png",
            "images/bullet.png",
            "images/arrow.png",
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
            "images/wooden_wall_red.png",
    };
    public static final String[] forestTextureAtlases = {
            "images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/forager.atlas", "images/miner.atlas", "images/builder.atlas",
            "images/duration-bar.atlas", "images/archer.atlas", "images/swordsman.atlas",
            "images/hoplite.atlas", "images/spearman.atlas", "images/blue_joker.atlas",
            "images/snake.atlas", "images/wolf.atlas", "images/snake2.0.atlas", "images/titan.atlas",
            "images/newwolf.atlas", "images/ns_gate.atlas", "images/ew_gate.atlas",
            "images/spell.atlas", "images/waterfeature.atlas", "images/lamp.atlas",
            "images/tree_.atlas",
            "images/spell.atlas", "images/titanshrine.atlas", "images/ship2.atlas",
            "images/barracks.atlas", "images/Trebuchet.atlas"
    };
    public static final String[] soldierMenuTextures = {
            "images/character-selection-menu.png"
    };

    public static final String[] buildingMenuTextures = {
            "images/building-selection-menu.png"
    };
    public static final String[] atlantisSounds = {"sounds/Impact4.ogg", "sounds/spell_sound.wav", "sounds/menuclicking.mp3"};

    Music music = Gdx.audio.newMusic(Gdx.files.internal("sounds/in-game-v3.wav"));

    private final AtlantisTerrainFactory terrainFactory;

    private DialogueBoxDisplay dialogueBoxDisplay;

    private Entity townHall;
    private Entity ship;
    private Entity titan;
    private FloodingGenerator floodingGenerator;
    private EventHandler gameAreaEventHandle;
    private Entity terrainMapAndMiniMap;
    private BuildingGenerator buildingGenerator;
    // Random instance extracted as per sonarcloud
    private final Random random = new Random();



    public AtlantisGameArea(AtlantisTerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
        gameAreaEventHandle = new EventHandler();
        this.floodingGenerator = new FloodingGenerator(this.terrainFactory, this);
        MapGenerator mg = terrainFactory.getMapGenerator();
        this.buildingGenerator = new BuildingGenerator(mg);
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
        gameAreaEventHandle.addListener("spawnUnit",this::spawnUnit);

//        loadAssets();
        displayUI();
        spawnTerrain();
        centreCameraOnCity();

        spawnForager();
        spawnForager();


//        spawnMiner();
//        spawnMiner();
//        spawnMiner();

        //playMusic();
        centreCameraOnCity();

        // Spawn Buildings in the city
        spawnCityWalls();

        // spawnBuildings();

        spawnForager();
        spawnMiner();
        //spawnBuilder();
        spawnCity();
        //spawnForager();
        spawnHoplite();
        spawnSpearman();
        spawnSwordsman();


        spawnResources();

        spawnTitanShrine();
        spawnShip();
        spawnTrebuchet(ship, this);
        spawnArcher(titan, this);

        // spawnWorkerBase();
        // spawnResources();

        // spawnWorkerBase();
        // spawnMiner();

        //spawnBlueJokers();

        // spawnWolf();


        //spawnTitan();
        //spawnSnakes();

        // spawnTrees();
        //spawnStone();
        //spawnMiner();

        spawnExplosion((new Explosion()).getEntity());
        ServiceLocator.registerGameArea(this);
        startFlooding();

    //    spawnSoldierMenu();
//        spawnBuildingMenu();
    }

    // private void spawnSoldierMenu() {
    //     Entity shopBox = new Entity();
    //     shopBox.addComponent(new SoldierMenuDisplay());
    //     spawnEntity(shopBox);
    // }

    private void spawnBuildingMenu() {
        Entity buildingBox = new Entity();
        buildingBox.addComponent(new BuildingMenuDisplay());
        spawnEntity(buildingBox);
    }

    /**
     * Starts the map flooding event.
     */
    public void startFlooding() {
        // Create Flooding Event
        Entity floodingEntity = new Entity();
        floodingEntity.addComponent(new FloodingGenerator(this.terrainFactory, this));
        ServiceLocator.getEntityService().register(floodingEntity);

        // Create Flooding Timer Display
        Entity floodTimerDisplay = new Entity();
        floodTimerDisplay.addComponent(new FloodTimerDisplay(this.floodingGenerator));
        ServiceLocator.getEntityService().register(floodTimerDisplay);
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


//        Entity infoUi = new Entity();
//        infoUi.addComponent(new InfoBoxDisplay());
////        infoUi.addComponent(new SpellUI());
//        spawnEntity(infoUi);


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
//        spawnIslandBounds();

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
        // BuildingGenerator bg = new BuildingGenerator(mg);
        spawnBuildings(this.buildingGenerator,mg.getHeight());
        spawnPaths(this.buildingGenerator, mg.getHeight(), mg.getCityDetails().get("NW").getY(), mg.getCityDetails().get("SW").getX());
        spawnFeatures(this.buildingGenerator);
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
                    //System.out.print("\n\nLibrary position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createLibrary();
                } else if (building.getName().equals("Smith")) {
                    // System.out.print("\n\nTH position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createBlacksmith();
                } else if (building.getName().equals("Barracks")) {
                    buildingEntity = BuildingFactory.createBarracks(); //.addComponent(new UnitSpawningComponent(gameAreaEventHandle));
                } else if (building.getName().equals("Farm")) {
                    // System.out.print("\n\nTH position: " + spawn + "\n\n");
                    buildingEntity = BuildingFactory.createFarm();
                } else {
                    // avoid null pointer exception
                    continue;
                }
                buildingEntity.getComponent(TextureScaler.class).setSpawnPoint(spawn, terrain);
                TextureScaler ts = buildingEntity.getComponent(TextureScaler.class);
                spawnEntity(buildingEntity);
            }
        }
    }

    /**
     * Randomly spawns a number of city features around the map
     */
    private void spawnFeatures(BuildingGenerator bg) {
        int numFeatures = 3; //Total number of features in game
        int currentFeature = 0; //Int corresponding to the next feature to be placed
        int featureOffset = 2; //Space to leave between building and feature
        MapGenerator mg = terrainFactory.getMapGenerator();

        List<CityRow> cityRows = bg.getCityRows();
        for (CityRow cr : cityRows) {
            List<Building> buildings = cr.getBuildings();
            for (int i = 0; i < buildings.size() - 1; i++) {
                //Iterate through row of buildings, excluding the last entry
                //Roll to see if a feature is being placed

                if (random.nextInt(100) <= 30) {
                    continue;
                }
                //A feature is being placed - determine where
                Building building = buildings.get(i);
                Coordinate placement = building.getPlacement();
                GridPoint2 spawn = new GridPoint2(placement.getX() + building.getWidth()
                        + featureOffset, mg.getHeight() - 1 - placement.getY() - (building.getHeight() / 2));
                Entity feature;
                //Ensures feature distribution is even
                switch (currentFeature % numFeatures) {
                    case 0:
                        //Place a water feature
                        feature = CityFeatureFactory.createWaterFeature();
                        break;
                    case 1:
                        //Place a bush
                        feature = CityFeatureFactory.createCityBush();
                        break;
                    default:
                        //Place a lamp
                        feature = CityFeatureFactory.createLamp();
                }
                currentFeature++;
                //Set spawn point and spawn feature
                feature.getComponent(TextureScaler.class).setSpawnPoint(spawn, terrain);
                spawnEntity(feature);

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
    }

    /**
     * Spawns a titan shrine
     */
    private void spawnTitanShrine() {
        int range = 20;

        // To get spawn point
        GridPoint2 spawnPoint = RandomPointGenerator.getRandomPointInSea(terrainFactory, 100);
        MapComponent mc1 = new MapComponent();
        mc1.display();
        mc1.setDisplayColour(Color.DARK_GRAY);
        titan = BuildingFactory.createTitanShrine();
        spawnEntityAt((titan.addComponent(mc1))
                        .addComponent(new UnitSpawningComponent(gameAreaEventHandle)),
                       spawnPoint, true, false);
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
        Vector2 target = terrain.tileToWorldPosition(RandomPointGenerator.getRandomPointInIsland(terrainFactory, range));
        ship = BuildingFactory.createShip(terrainFactory);
        ship.addComponent(mc).addComponent(new UnitSpawningComponent(gameAreaEventHandle));
        spawnEntityAt(ship, spawnPoint, false, false);
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
            Vector2 connectorOffset = new Vector2(0.3f, 0.1f);

            pillarSW.getComponent(TextureScaler.class).setSpawnPoint(swPillarSpawn, terrain);
            spawnEntity(pillarSW);

            connectorSW.getComponent(TextureScaler.class).setSpawnPoint(swConnectorSpawn, terrain, connectorOffset);
            spawnEntity(connectorSW);

            pillarNW.getComponent(TextureScaler.class).setSpawnPoint(nwPillarSpawn, terrain);
            spawnEntity(pillarNW);

            connectorNW.getComponent(TextureScaler.class).setSpawnPoint(nwConnectorSpawn, terrain, connectorOffset);
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

            Vector2 connectorOffset = new Vector2(0.3f, -0.3f);
            connectorNE.getComponent(TextureScaler.class).setSpawnPoint(neConnectorSpawn, terrain, connectorOffset);
            spawnEntity(connectorNE);

            connectorNW.getComponent(TextureScaler.class).setSpawnPoint(nwConnectorSpawn, terrain, connectorOffset);
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
        GridPoint2 corner1 = RandomPointGenerator.getRescaledTopLeftCorner(terrainFactory, 1f);
        corner1.x += mg.getCityWidth();
        GridPoint2 corner2 = RandomPointGenerator.getRescaledTopLeftCorner(terrainFactory, 1f);
        spawnEntityAt((BuildingFactory.createTrebuchet(target, gameArea))
                        .addComponent(new UnitSpawningComponent(gameAreaEventHandle)), corner1,
                true, true);
        spawnEntityAt((BuildingFactory.createTrebuchet(titan, gameArea))
                        .addComponent(new UnitSpawningComponent(gameAreaEventHandle)), corner2,
                true, true);
    }

    /**
     * @return the Atlantis Game Area event handler
     */
    public EventHandler getGameAreaEventHandler() {
        return this.gameAreaEventHandle;
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

    private void spawnArcher(Entity target, GameArea gameArea) {
        int offset = 20;
        MapGenerator mg = terrainFactory.getMapGenerator();
        char[][] map = mg.getMap();
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        spawnEntityAt((ArcherFactory.createArcher(target, gameArea)), spawn, true, true);
    }

    private void spawnSwordsman() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        Entity newSwordsman = SwordsmanFactory.createSwordsman().addComponent(mapComponent);
        spawnEntityAt(newSwordsman, spawn, true, true);
    }

    private void spawnSpearman() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        Entity newSpearman = SpearmanFactory.createSpearman().addComponent(mapComponent);
        spawnEntityAt(newSpearman, spawn, true, true);
    }

    private void spawnHoplite() {
        GridPoint2 spawn = RandomPointGenerator.getRandomPointInRange(terrainFactory, 0.25);
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        Entity newHoplite = HopliteFactory.createHoplite().addComponent(mapComponent);
        spawnEntityAt(newHoplite, spawn, true, true);

    }
    /**
     * Overloaded method used for spawning units from the Shop UI
     */
    private void spawnUnit(UnitType type) {
        if (type == UnitType.SWORDSMAN) {
            spawnSwordsman();
        } else if (type == UnitType.SPEARMAN) {
            spawnSpearman();
        } else if (type == UnitType.HOPLITE) {
            spawnHoplite();
        } else if (type == UnitType.ARCHER) {
            spawnArcher(titan, this);
        }
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
            for (Coordinate placement : rs.getPlacements()) {
                GridPoint2 spawn = new GridPoint2(placement.getX(), mg.getHeight() - 1 - placement.getY());
                if (rs.getName().equals("Tree")) {
                    //Spawn a Tree entity
                    Entity tree = TreeFactory.createTree();
                    tree.getComponent(TextureScaler.class).setSpawnPoint(spawn, terrain);
                    spawnEntity(tree);
                } else if (rs.getName().equals("Stone")) {
                    //Spawn a Stone entity
                    //spawnEntityAt(StoneFactory.createStone().addComponent(mapComponent), spawn, false, false);
                    Entity camp = MiningCampFactory.createMiningCamp();
                    camp.getComponent(TextureScaler.class).setSpawnPoint(spawn, terrain);
                    spawnEntity(camp);
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
        spawnPaths(this.buildingGenerator, mg.getHeight(), mg.getCityDetails().get("NW").getY(), mg.getCityDetails().get("SW").getX());
        MinimapComponent minimapComponent = new MinimapComponent(terrain.getMap(), (OrthographicCamera) terrainFactory.getCameraComponent().getCamera());
        // allow access to minimap via UI for dynamic resizing/positioning
        this.dialogueBoxDisplay.setMinimap(minimapComponent);
        this.terrainMapAndMiniMap = new Entity().addComponent(terrain).addComponent(minimapComponent);
        spawnEntity(terrainMapAndMiniMap);
    }
}
