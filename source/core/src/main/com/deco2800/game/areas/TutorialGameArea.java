package com.deco2800.game.areas;

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
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.components.gamearea.GameAreaDisplay;

import java.util.Map;
import java.util.Vector;

public class TutorialGameArea extends GameArea {

    /** dialogue box */
    private DialogueBoxDisplay dialogueBoxDisplay;

    private final AtlantisTerrainFactory terrainFactory;

    private AtlantisGameArea area;
    private Entity player;

    /** textures needed to load */
    private String[] tutorialTextures = {
            "images/dialogue_box_pattern2_background.png",
            "images/dialogue_box_image_default.png",
            "images/exit-button.PNG",
            "images/dialogue_box_background_Deep_Sea.png",
            "test/files/dummyTexture.png",
            "test/files/dummyOcean.png",
            "images/Ocean.png",
            "images/Sand.png",
            "images/Grass.png",
            "images/box_boy_leaf.png",
            "images/box_boy.png",
            "images/Base_Highlight.png",
            "images/box_boy_highlight.png",
            "images/tree.png",
            "images/ghost_king.png",
            "images/ghost_1.png",
            "images/grass_1.png",
            "images/grass_2.png",
            "images/grass_3.png",
            "images/sea_1.png",
            "images/sea_2.png",
            "images/sea_3.png",
            "images/sea_4.png",
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
            "mining_levelone_sketch.png",
            "mining_leveltwo_sketch.png",
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
            "images/hoplite.png",
            "images/spearman.png",
            "images/simpleman.png"
    };

    public TutorialGameArea(AtlantisTerrainFactory terrainFactory) {
        super();
        this.terrainFactory = terrainFactory;
    }


    /**
     * On init load needed textures
     */
    private void loadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(this.tutorialTextures);
        resourceService.loadAll();
    }

    /**
     * On exit clean-up loaded textures
     */
    private void unloadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(this.tutorialTextures);
    }

    /**
     * Create and spawn entities to this area
     */
    private void displayUI() {

        Entity dialogueBox = new Entity();
        this.dialogueBoxDisplay = new DialogueBoxDisplay();
        dialogueBox.addComponent(this.dialogueBoxDisplay)
                .addComponent(new DialogueBoxActions(this.dialogueBoxDisplay));

        Entity ui = new Entity();
        ui.addComponent(new GameAreaDisplay("tutorial area"));

        Entity infoUi = new Entity();
        infoUi.addComponent(new InfoBoxDisplay());

        spawnEntity(dialogueBox);
        spawnEntity(ui);
        spawnEntity(infoUi);
    }

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
        this.dialogueBoxDisplay.setTitle("Tutorial");
    }

    private void spawnTerrain() {
        MapGenerator mg = terrainFactory.getMapGenerator();
        terrainFactory.createAtlantisTerrainComponent();
        MinimapComponent minimapComponent = new MinimapComponent(terrain.getMap(), (OrthographicCamera) terrainFactory.getCameraComponent().getCamera());
        spawnEntity(new Entity().addComponent(terrain).addComponent(minimapComponent));
        terrainFactory.getCameraComponent().getEntity().getComponent(CameraInputComponent.class)
                .setMapDetails(terrain.getTileSize(), mg.getWidth(), mg.getHeight());
    }

    @Override
    public void create() {

        this.loadAssets();
        this.displayUI();
        spawnBuildings();
        spawnTerrain();
    }

    @Override
    public void dispose() {

        super.dispose();
        this.unloadAssets();
    }
}
