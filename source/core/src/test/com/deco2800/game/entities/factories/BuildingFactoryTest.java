package com.deco2800.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.components.friendlyunits.MouseInputComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BarracksConfig;
import com.deco2800.game.entities.configs.BuildingConfigs;
import com.deco2800.game.entities.configs.TownHallConfig;
import com.deco2800.game.entities.configs.WallConfig;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputService;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.resources.ResourceConfig;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Tests the ability of BuildingFactory to create and add component to make entity a working building in game
 * Collider testing is not rigorous as it also relies on visual testing - see in game that the bounding box encapsulates
 * the texture
 */
@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BuildingFactoryTest {

    // Mocked as Graphics is needed for input component
    @Mock MouseInputComponent inputComponent;

    private static final BuildingConfigs configs =
            FileLoader.readClass(BuildingConfigs.class, "configs/buildings.json");
    private static final ResourceConfig stats =
            FileLoader.readClass(ResourceConfig.class, "configs/Base.json");

    /**
     * Called before each test
     */
    @BeforeEach
    void setUp() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        String[] textures = new String[]{
                // TownHall
                "images/base.png",
                // Barracks
                "images/barracks_level_1.0.png",
                // Walls
                "images/wooden_wall.png",
                // Farms
                "images/farm.png",
                // Library
                "images/library.png",
                // Blacksmiths
                "images/blacksmith.png"
        };
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);
        resourceService.loadTextures(textures);
        resourceService.loadAll();

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerMapService(new MapService());
    }

    /**
     * Test focuses on adding the non-specific basic components to an entity that all buildings will require
     */
    @Test
    void shouldCreateBaseBuilding() {
        Entity building = new Entity();
        building
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new SelectableComponent())
                .addComponent(inputComponent)
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        assertInstanceOf(Entity.class, building);
        // Check base building has correct components
        assertInstanceOf(PhysicsComponent.class, building.getComponent(PhysicsComponent.class));
        assertInstanceOf(SelectableComponent.class, building.getComponent(SelectableComponent.class));
        assertInstanceOf(MouseInputComponent.class, building.getComponent(MouseInputComponent.class));
        assertInstanceOf(ColliderComponent.class, building.getComponent(ColliderComponent.class));
    }

    /**
     * Test focuses on adding required components for a TownHall entity and the usage of configs to construct these
     * components. Also tests the setting of new shaped collider. This new collider is also visually tested using the
     * debug terminal to show hit-boxes in game
     */
    @Test
    void shouldCreateTownHall() {
        final float TH_SCALE = 7f;
        Entity townHall = new Entity();
        TownHallConfig config = configs.townHall;

        townHall
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/base.png"));

        townHall.scaleWidth(TH_SCALE);
        // Setting Isometric Collider
        // Points (in pixels) on the texture to set the collider to
        float[] points = new float[] {      // Four vertices
                31f, 607f,      // Vertex 0       3--2
                499f, 835f,     // Vertex 1      /  /
                958f, 515f,     // Vertex 2     /  /
                486f, 289f      // Vertex 3    0--1
        };
        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/base.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(TH_SCALE);
        }
        PolygonShape boundingBox = new PolygonShape();  // Collider shape
        boundingBox.set(vertices);
        townHall.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        // We don't know the expected vertices for the bounding box so this is something we will have to test visually
        assertInstanceOf(Entity.class, townHall);
        // Check TownHall has correct components
        assertInstanceOf(ColliderComponent.class, townHall.getComponent(ColliderComponent.class));
        assertInstanceOf(TextureRenderComponent.class, townHall.getComponent(TextureRenderComponent.class));

        assertEquals(4, boundingBox.getVertexCount());
    }

    /**
     * Test focuses on adding required components for a barracks entity and the usage of configs to construct these
     * components. Also tests the setting of new shaped collider. This new collider is also visually tested using the
     * debug terminal to show hit-boxes in game
     */
    @Test
    void createBarracks() {
        final float BARRACKS_SCALE = 5f;
        Entity barracks = new Entity();
        BarracksConfig config = configs.barracks;

        barracks
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));

        barracks.scaleWidth(BARRACKS_SCALE);
        // Setting Isometric Collider
        // Points (in pixels) on the texture to set the collider to
        float[] points = new float[]{
                605f, 1036f,    // Vertex 0        3
                982f, 889f,     // Vertex 1    4 /   \ 2
                982f, 761f,     // Vertex 2     |     |
                605f, 581f,     // Vertex 3    5 \   / 1
                222f, 736f,     // Vertex 4        0
                222f, 874f      // Vertex 5
        };
        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/barracks_level_1.0.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(BARRACKS_SCALE);
        }
        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        barracks.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        // We don't know the expected vertices for the bounding box so this is something we will have to test visually
        assertInstanceOf(Entity.class, barracks);
        // Check barracks has correct components
        assertInstanceOf(ColliderComponent.class, barracks.getComponent(ColliderComponent.class));
        assertInstanceOf(TextureRenderComponent.class, barracks.getComponent(TextureRenderComponent.class));
        assertInstanceOf(BuildingActions.class, barracks.getComponent(BuildingActions.class));
        assertInstanceOf(CombatStatsComponent.class, barracks.getComponent(CombatStatsComponent.class));

        assertEquals(6, boundingBox.getVertexCount());
    }

    /**
     * Test focuses on adding required components for a wall entity and the usage of configs to construct these
     * components. Also tests the setting of new shaped collider. This new collider is also visually tested using the
     * debug terminal to show hit-boxes in game
     */
    @Test
    void shouldCreateWall() {
        Entity wall = new Entity();
        WallConfig config = configs.wall;

        wall
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/wooden_wall.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));

        wall.scaleWidth(2.2f);

        PolygonShape boundingBox = new PolygonShape();
        Vector2 center = wall.getCenterPosition(); // Collider to be set around center of entity
        boundingBox.setAsBox(center.x * 0.25f, center.y * 0.25f, center, (float) (60 * Math.PI / 180));
        wall.getComponent(ColliderComponent.class).setShape(boundingBox);

        assertInstanceOf(Entity.class, wall);
        // Check base building has correct components
        assertInstanceOf(ColliderComponent.class, wall.getComponent(ColliderComponent.class));
        assertInstanceOf(TextureRenderComponent.class, wall.getComponent(TextureRenderComponent.class));
        assertInstanceOf(BuildingActions.class, wall.getComponent(BuildingActions.class));
        assertInstanceOf(CombatStatsComponent.class, wall.getComponent(CombatStatsComponent.class));

        assertEquals(4, boundingBox.getVertexCount());
    }

    /**
     * Tests whether a farm building entity can be created.
     */
    @Test
    void shouldCreateFarm() {
        Entity farm = new Entity();
        final float FARM_SCALE = 5f;

        Vector2 leftPoint = new Vector2(0f, 220f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(207f, 322f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(215f, 107f); //NW edge

        farm.addComponent(new TextureRenderComponent("images/farm.png"))
            .addComponent(new MapComponent())
            .addComponent(new TextureScaler(leftPoint, maxX, maxY))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        assertInstanceOf(ColliderComponent.class, farm.getComponent(ColliderComponent.class));
        assertInstanceOf(MapComponent.class, farm.getComponent(MapComponent.class));
        assertInstanceOf(TextureRenderComponent.class, farm.getComponent(TextureRenderComponent.class));
        assertInstanceOf(TextureScaler.class, farm.getComponent(TextureScaler.class));

        farm.getComponent(TextureScaler.class).setPreciseScale(FARM_SCALE, true);

        // Methodology sourced from BuildingFactory.java:createTownHall()
        float[] points = new float[] {
            33f, 199f,
            113f, 240f,
            253f, 173f,
            169f, 137f
        };
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/farm.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(FARM_SCALE);
        }
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.set(vertices);
        farm.getComponent(ColliderComponent.class).setShape(boundingBox);

        assertEquals(4, boundingBox.getVertexCount());
    }

    /**
     * Tests whether a library building entity can be created
     */
    @Test
    void shouldCreateLibrary() {
        Entity library = new Entity();
        final float LIBRARY_SCALE = 5f;

        Vector2 leftPoint = new Vector2(69f, 351f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(280f, 457f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(281f, 260f); //NW edge


        library.addComponent(new TextureRenderComponent("images/library.png"))
               .addComponent(new MapComponent())
               .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
               .addComponent(new TextureScaler(leftPoint, maxX, maxY));

        assertInstanceOf(ColliderComponent.class, library.getComponent(ColliderComponent.class));
        assertInstanceOf(MapComponent.class, library.getComponent(MapComponent.class));
        assertInstanceOf(TextureRenderComponent.class, library.getComponent(TextureRenderComponent.class));
        assertInstanceOf(TextureScaler.class, library.getComponent(TextureScaler.class));

        library.getComponent(TextureScaler.class).setPreciseScale(LIBRARY_SCALE, true);

        // Methodology sourced from BuildingFactory.java:createTownHall()
        float[] points = new float[] {      // Six vertices
            81f, 354f,
            191f, 411f,
            276f, 370f,
            361f, 420f,
            446f, 375f,
            247f, 273f
        };
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/library.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(library.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.set(vertices);
        library.getComponent(ColliderComponent.class).setShape(boundingBox);

        assertEquals(5, boundingBox.getVertexCount());
    }

    /**
     * Tests whether a blacksmith building entity can be created
     */
    @Test
    void shouldCreateBlacksmith() {
        Entity bs = new Entity();
        final float BLACKSMITH_SCALE = 5f;

        Vector2 leftPoint = new Vector2(5f, 176f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(123f, 251f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(115f, 143f); //NW edge

        bs.addComponent(new TextureRenderComponent("images/blacksmith.png"))
          .addComponent(new MapComponent())
          .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
          .addComponent(new TextureScaler(leftPoint, maxX, maxY));

        assertInstanceOf(ColliderComponent.class, bs.getComponent(ColliderComponent.class));
        assertInstanceOf(MapComponent.class, bs.getComponent(MapComponent.class));
        assertInstanceOf(TextureRenderComponent.class, bs.getComponent(TextureRenderComponent.class));
        assertInstanceOf(TextureScaler.class, bs.getComponent(TextureScaler.class));

        bs.getComponent(TextureScaler.class).setPreciseScale(BLACKSMITH_SCALE, true);

        // Methodology sourced from BuildingFactory.java:createTownHall()
        float[] points = new float[] {      // Four vertices
            5f, 176f,
            123f, 251f,
            244f, 192f,
            126f, 117f
        };
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/blacksmith.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(bs.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.set(vertices);
        bs.getComponent(ColliderComponent.class).setShape(boundingBox);
        
        assertEquals(4, boundingBox.getVertexCount());
    }
}