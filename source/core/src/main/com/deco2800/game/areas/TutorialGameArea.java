package com.deco2800.game.areas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.areas.terrain.MinimapComponent;
import com.deco2800.game.components.maingame.DialogueBoxActions;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import com.deco2800.game.components.maingame.InfoBoxDisplay;
import com.deco2800.game.components.UnitSpawningComponent;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.friendlyunits.MouseInputComponent;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.components.tutorial.TutorialActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UnitType;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.entities.factories.EnemyFactory;
import com.deco2800.game.entities.factories.UnitFactory;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.friendlyunits.gamearea.GameAreaDisplay;

import java.util.Map;
import java.util.Vector;

public class TutorialGameArea extends GameArea {

    /** dialogue box */
    public DialogueBoxDisplay dialogueBoxDisplay;

    private final AtlantisTerrainFactory terrainFactory;
    private Entity titan;

    private final String[] forestTextures = AtlantisGameArea.forestTextures;

    public static final String[] uiTextures = AtlantisGameArea.uiTextures;
    public static final String[] forestTextureAtlases = AtlantisGameArea.forestTextureAtlases;
    public static final String[] buildingPlacementTextures = AtlantisGameArea.buildingPlacementTextures;


    /** textures needed to load */
    private String[] tutorialTextures = {
            "images/spellbox-zeus.png",
            "images/CogWheel/Esc Menu/CogWheelBG.png",
            "images/spell-btn-unclickable.png",
            "images/dialogue_box_pattern2_background.png",
            "images/dialogue_box_image_default.png",
            "images/exit-button.PNG",
            "images/dialogue_box_background_Deep_Sea.png",
            "test/files/dummyTexture.png",
            "test/files/dummyOcean.png",
            "images/Ocean.png",
            "images/Sand.png",
            "images/Grass.png",
            "images/sand_starfish.png",
            "images/sand_shell.png",
            "images/box_boy_leaf.png",
            "images/box_boy.png",
            "images/Attack_Fred.png",
            "images/Scared_Fred.png",
            "images/Base_Highlight.png",
            "images/Fredmogus.png",
            "images/box_boy_highlight.png",
            "images/Teach_Fred.png",
            "images/Good_Luck_Fred.png",
            "images/tree.png",
            "images/titanshrine-default.png",
            "images/ghost_king.png",
            "images/defense.png",
            "images/ghost_1.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/sea_1.png",
            "images/attack.png",
            "images/sea_2.png",
            "images/sea_3.png",
            "images/sea_4.png",
            "images/hex_grass_1.png",
            "images/hex_grass_2.png",
            "images/hex_grass_3.png",
            "images/iso_grass_2.png",
            "images/iso_grass_3.png",
            "images/Information_Box_Deepsea.png",
            "images/TransBox.png",

            "images/white.png",
            "images/stone.png",
            "images/city_tile.png",
            /* Building assets */
            // TownHall
            "images/base.png",
            // Barracks
            "images/barracks_level_1.0.png",
            "images/barracks_level_1.0_Highlight.png",
            "images/barracks_level_1.1.png",
            "images/barracks_level_1.2.png",
            "images/barracks_level_2.0.png",
            // Mine
            "images/mining_levelone_sketch.png",
            "images/mining_leveltwo_sketch.png",
            // Walls
            "images/wooden_wall.png",
            "images/wooden_wall_2.png",
            "images/wooden_wall_3.png",
            "images/stone_wall.png",
            "images/stone_wall_2_.png",
            "images/stone_wall_3.png",
            "images/Base_Highlight",
            "images/stone.png",
            "images/archer.png",
            "images/swordsman.png",
            "images/SirFred.png",
            "images/hoplite.png",
            "images/spearman.png",
            "images/simpleman.png",
            "test/files/cityMinimap.png",
            "images/spellbox-zeus.png",
            "images/spell-btn-unclickable.png",
            "images/spell-btn.png",
            "images/SpellIndicator/spelliso.png",
    };

    private String[] tutorialTexturesAtlases = {"images/terrain_iso_grass.atlas", "images/ghost.atlas", "images/ghostKing.atlas",
            "images/forager.atlas", "images/miner.atlas", "images/builder.atlas",
            "images/duration-bar.atlas", "images/archer.atlas", "images/swordsman.atlas",
            "images/hoplite.atlas", "images/spearman.atlas", "images/blue_joker.atlas",
            "images/snake.atlas", "images/wolf.atlas", "images/snake2.0.atlas", "images/titan.atlas",
            "images/newwolf.atlas", "images/ns_gate.atlas", "images/ew_gate.atlas",
            "images/newwolf.atlas", "images/forager.atlas","images/tree_.atlas",
            "images/spell.atlas", "images/titanshrine.atlas", "images/ship2.atlas"};
    private EventHandler gameAreaEventHandle;



    public static final String[] sounds = {"sounds/Impact4.ogg", "sounds/spell_sound.wav", "sounds/menuclicking.mp3"};


    public static final String[] atlantisSounds = AtlantisGameArea.atlantisSounds;
    private String[] tutorialSprites = {
        "images/barracks.atlas"
    };

    public TutorialGameArea(AtlantisTerrainFactory terrainFactory, DialogueBoxDisplay display) {
        super();
        this.terrainFactory = terrainFactory;
        this.dialogueBoxDisplay = display;
        gameAreaEventHandle = new EventHandler();
    }


    /**
     * On init load needed textures
     */
    private void loadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(sounds);
        resourceService.loadTextureAtlases(this.tutorialTexturesAtlases);
        resourceService.loadTextures(this.tutorialTextures);

        resourceService.loadTextureAtlases(this.tutorialSprites);


        resourceService.loadTextures(forestTextures);
        resourceService.loadTextures(uiTextures);

        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadTextures(buildingPlacementTextures);

        ServiceLocator.getResourceService().loadAll();



        resourceService.loadTextureAtlases(this.tutorialSprites);
        resourceService.loadAll();
    }

    /**
     * On exit clean-up loaded textures
     */
    private void unloadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(this.tutorialTextures);
        resourceService.unloadAssets(sounds);
        resourceService.unloadAssets(this.forestTextures);
    }

    /**
     * Create and spawn entities to this area
     */
    private void displayUI() {

        Entity dialogueBox = new Entity();
        dialogueBox.addComponent(this.dialogueBoxDisplay)
                .addComponent(new DialogueBoxActions(this.dialogueBoxDisplay));

        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("tutorial area"));

        Entity infoUi = new Entity();
        infoUi.addComponent(new InfoBoxDisplay());

        Entity spellsUi = new Entity();
        spellsUi.addComponent(new SpellUI());
        spellsUi.addComponent(new MouseInputComponent());
        spawnEntity(spellsUi);

        spawnEntity(dialogueBox);
        spawnEntity(ui);
        spawnEntity(infoUi);
    }

    /**
     * spawn tutorial area - needs mining camp
     */
    private void spawnBuildings() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        Coordinate centre = cityDetails.get("Centre");
        GridPoint2 spawn = new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());
        this.terrain = terrainFactory.createAtlantisTerrainComponent();
        MapComponent mapComponent = new MapComponent();
        mapComponent.display();
        mapComponent.setDisplayColour(Color.BROWN);
        Entity townHall = BuildingFactory.createTownHall().addComponent(mapComponent);
        spawnEntityAt(townHall, spawn.add(0, 2), true, true);

        this.terrainFactory.getCameraComponent().getEntity().setPosition(new Vector2());
        Entity barracks = BuildingFactory.createBarracks().addComponent(mapComponent);
        spawnEntityAt(barracks, spawn.add(8, 10), true, true);
        Vector2 centreWorld = this.terrain.tileToWorldPosition(spawn.x, spawn.y);
        this.terrainFactory.getCameraComponent().getEntity().setPosition(centreWorld);
        this.dialogueBoxDisplay.setTitle("Welcome to Atlantis Sinks!");
        this.dialogueBoxDisplay.setDialogue("Press next to get \nstarted or skip to play");
        this.dialogueBoxDisplay.setImage("images/SirFred.png");
    }

    /**
     * spawn map terrain
     */
    private void spawnTerrain() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        terrainFactory.createAtlantisTerrainComponent();
        MinimapComponent minimapComponent = new MinimapComponent(terrain.getMap(), (OrthographicCamera) terrainFactory.getCameraComponent().getCamera());
        spawnEntity(new Entity().addComponent(terrain).addComponent(minimapComponent));
        terrainFactory.getCameraComponent().getEntity().getComponent(CameraInputComponent.class)
                .setMapDetails(terrain.getTileSize(), mg.getWidth(), mg.getHeight());
    }


    public void spawnExplosion(Entity entity) {
        MapGenerator mg = terrainFactory.getMapGenerator();
        //Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Store centre of city
        Coordinate centre = cityDetails.get("Centre");


        //Spawn player at centre of city
        GridPoint2 spawn = new GridPoint2(Gdx.input.getX(), Gdx.input.getY());

        MapComponent mapComponent = new MapComponent();
//        mapComponent.display();
//        mapComponent.setDisplayColour(Color.PURPLE);
//        entity.addComponent(mapComponent);
//        entity.setEnabled(false);
        spawnEntityAt(entity, spawn, true, true);
    }

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

    private void spawnBlueJokers(Vector2 spawnPoint) {
        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.BLUE);
        Entity blueJoker = EnemyFactory.createBlueJoker(terrainFactory).addComponent(mc);
        spawnEntityAt(blueJoker, spawnPoint, true, true);
    }

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

    private void spawnTitan(Vector2 spawnPoint) {
        MapGenerator mg = terrainFactory.getMapGenerator();
        MapComponent mc = new MapComponent();
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        //Store centre of city
        Coordinate centre = cityDetails.get("Centre");
        mc.display();
        mc.setDisplayColour(Color.RED);
        Entity titan = EnemyFactory.createTitan(terrainFactory).addComponent(mc);
        titan.setEntityName("titan");
        spawnEntityAt(titan, spawnPoint, true, true);
    }


    @Override
    public void create() {
        gameAreaEventHandle.addListener("spawnTitan", this::spawnTitan);

        this.loadAssets();
        this.displayUI();
        this.terrain = terrainFactory.createAtlantisTerrainComponent();
        spawnExplosion((new Explosion()).getEntity());
        spawnBuildings();
        spawnTerrain();
        spawnUnit(UnitType.ARCHER, new GridPoint2(8,8));
        spawnUnit(UnitType.SPEARMAN, new GridPoint2(-8,-8));
        spawnUnit(UnitType.SWORDSMAN, new GridPoint2(8, -8));
        spawnUnit(UnitType.HOPLITE, new GridPoint2(-8, 8));
        spawnBlueJokers(new Vector2(50f, 5f));

        spawnTitan(new Vector2(60f, 4f));
    }




    @Override
    public void dispose() {

        super.dispose();
        this.unloadAssets();
    }
}
