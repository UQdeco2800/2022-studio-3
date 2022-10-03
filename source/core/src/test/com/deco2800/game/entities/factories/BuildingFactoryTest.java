package com.deco2800.game.entities.factories;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.BuildingUIDataComponent;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.building.AttackListener;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.building.damageAnimation;
import com.deco2800.game.components.friendlyunits.MouseInputComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.components.tasks.rangedAttackTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
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
                // Trebuchet
                "images/Trebuchet-lv1-north.png"
        };
        String[] atlasFiles = new String[] {
                // Titan Shrine
                "images/titanshrine.atlas",
                // Ship
                "images/ship2.atlas"
        };
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);
        resourceService.loadTextures(textures);
        resourceService.loadTextureAtlases(atlasFiles);

        resourceService.loadAll();

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerInputService(new InputService());
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
     * Test focuses on adding required components for a titan shrine entity and the usage of configs to construct these
     * components. Also tests the setting of new shaped collider. This new collider is also visually tested using the
     * debug terminal to show hit-boxes in game
     */
    @Test
    void createTitanShrine() {
        final float TITANSHRINE_SCALE = 10f;
        Entity titanShrine = new Entity();
        TitanShrineConfig config = configs.titanShrine;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(ServiceLocator.getResourceService()
                        .getAsset("images/titanshrine.atlas",
                                TextureAtlas.class));

        // Animations aren't tested here, that requires visiual testing.

        titanShrine
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new damageAnimation())
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new BuildingUIDataComponent())
                .addComponent(animator);


        // Setting Isometric Collider
        // Points (in pixels) on the texture to set the collider to
        float[] points = new float[]{
                605f, 1111,      // Vertex 0        3
                1100f, 870f,     // Vertex 1    4 /   \ 2
                1100f, 800f,     // Vertex 2     |     |
                605f, 581f,      // Vertex 3    5 \   / 1
                100f, 800f,      // Vertex 4        0
                100f, 874f       // Vertex 5
        };

        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/barracks_level_1.0.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(TITANSHRINE_SCALE);
        }
        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        titanShrine.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider


        // We don't know the expected vertices for the bounding box so this is something we will have to test visually
        assertInstanceOf(Entity.class, titanShrine);
        // Check barracks has correct components
        assertInstanceOf(ColliderComponent.class, titanShrine.getComponent(ColliderComponent.class));
        assertInstanceOf(AnimationRenderComponent.class, titanShrine.getComponent(AnimationRenderComponent.class));
        assertInstanceOf(BuildingActions.class, titanShrine.getComponent(BuildingActions.class));
        assertInstanceOf(CombatStatsComponent.class, titanShrine.getComponent(CombatStatsComponent.class));
        assertInstanceOf(damageAnimation.class, titanShrine.getComponent(damageAnimation.class));
        assertInstanceOf(BuildingUIDataComponent.class, titanShrine.getComponent(BuildingUIDataComponent.class));

        assertEquals(6, boundingBox.getVertexCount());
    }

    /**
     * Test focuses on adding required components for a titan shrine entity and the usage of configs to construct these
     * components. Also tests the setting of new shaped collider. This new collider is also visually tested using the
     * debug terminal to show hit-boxes in game
     */
    @Test
    void createShip() {
        final float SHIP_SCALE = 5f;
        Entity ship = new Entity();
        ShipConfig config = configs.ship;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(ServiceLocator.getResourceService()
                        .getAsset("images/ship2.atlas", TextureAtlas.class));

        animator.addAnimation("default", 0.1f, Animation.PlayMode.NORMAL);

        ship
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new PhysicsMovementComponent())
                .addComponent(animator);

        ship.getComponent(AnimationRenderComponent.class).startAnimation("default");
        ship.getComponent(AnimationRenderComponent.class).scaleEntity();
        ship.scaleWidth(SHIP_SCALE);

        assertInstanceOf(Entity.class, ship);
        // Check barracks has correct components
        assertInstanceOf(ColliderComponent.class, ship.getComponent(ColliderComponent.class));
        assertInstanceOf(AnimationRenderComponent.class, ship.getComponent(AnimationRenderComponent.class));
        assertInstanceOf(BuildingActions.class, ship.getComponent(BuildingActions.class));
        assertInstanceOf(CombatStatsComponent.class, ship.getComponent(CombatStatsComponent.class));
        assertInstanceOf(PhysicsMovementComponent.class, ship.getComponent(PhysicsMovementComponent.class));
    }

    /**
     * Test focuses on adding required components for a trebuchet entity and the usage of configs to construct these
     * components.
     */
    @Test
    void createTrebuchet() {
        final float Trebuchet_SCALE = 3f;
        Entity trebuchet = new Entity();

        // To make the tests work.
        Entity target = new Entity();
        GameArea gameArea = new AtlantisGameArea(new AtlantisTerrainFactory(new CameraComponent()));

        TrebuchetConfig config = configs.trebuchet;
        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new rangedAttackTask(target, 4, 10, 2000f));

        trebuchet
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/Trebuchet-lv1-north.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(aiComponent)
                .addComponent(new AttackListener(target, gameArea));
        trebuchet.scaleHeight(Trebuchet_SCALE);

        // We don't know the expected vertices for the bounding box so this is something we will have to test visually
        assertInstanceOf(Entity.class, trebuchet);
        // Check barracks has correct components
        assertInstanceOf(ColliderComponent.class, trebuchet.getComponent(ColliderComponent.class));
        assertInstanceOf(TextureRenderComponent.class, trebuchet.getComponent(TextureRenderComponent.class));
        assertInstanceOf(BuildingActions.class, trebuchet.getComponent(BuildingActions.class));
        assertInstanceOf(CombatStatsComponent.class, trebuchet.getComponent(CombatStatsComponent.class));
        assertInstanceOf(AITaskComponent.class, trebuchet.getComponent(AITaskComponent.class));
        assertInstanceOf(AttackListener.class, trebuchet.getComponent(AttackListener.class));
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
}