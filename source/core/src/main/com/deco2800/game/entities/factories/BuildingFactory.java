package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.components.building.GateCollider;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.HighlightedTextureRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.resources.ResourceConfig;

/**
 * Factory to create a building entity with predefined components.
 * <p> Each building entity type should have a creation method that returns a corresponding entity.
 * <p> Predefined buildings properties are loaded from a config stored as a json file and should have
 * the properties stored in 'BuildingConfigs'.
 */
public class BuildingFactory {
    private static final BuildingConfigs configs =
            FileLoader.readClass(BuildingConfigs.class, "configs/buildings.json");
    private static final ResourceConfig stats =
            FileLoader.readClass(ResourceConfig.class, "configs/base.json");

    /**
     * Width in tiles of a wall pillar entity
     */
    public static final float CORNER_SCALE = 2f;

    /**
     * Width in tiles of wall connectors and gates - do not change as they are contingent on CORNER_SCALE
     */
    public static final float CONNECTOR_SCALE = 2f * CORNER_SCALE;
    public static final float GATE_SCALE = (2f * CORNER_SCALE) + CONNECTOR_SCALE;

    /**
     * Use this as a base entity for creating buildings
     * @return a new Entity with universal building components
     */
    public static Entity createBaseBuilding() {
        return new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new SelectableComponent())
                .addComponent(ServiceLocator.getInputService().getInputFactory().createForFriendlyUnit())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    }

    /**
     * Creates entity, adds and configures TownHall components
     * @return TownHall Entity
     */
    public static Entity createTownHall() {
        final float TH_SCALE = 7f;
        Entity townHall = createBaseBuilding();
        TownHallConfig config = configs.townHall;

        townHall.addComponent(new TextureRenderComponent("images/base.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal))
                .addComponent(new BaseComponent())
                .addComponent(new HighlightedTextureRenderComponent("images/Base_Highlight.png"));

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

        return townHall;
    }

    /**
     * Creates entity, adds and configures Barracks components
     * @return Barracks Entity
     */
    public static Entity createBarracks() {
        final float BARRACKS_SCALE = 5f;
        Entity barracks = createBaseBuilding();
        BarracksConfig config = configs.barracks;

        barracks.addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new HighlightedTextureRenderComponent("images/barracks_level_1.0_Highlight.png"))
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

        return barracks;
    }

    /**
     * Creates entity, adds and configures Wall components
     * @return Barracks Entity
     */
    public static Entity createWall() {
        Entity wall = createBaseBuilding();
        WallConfig config = configs.wall;

        wall.addComponent(new TextureRenderComponent("images/wooden_wall.png"))
            .addComponent(new BuildingActions(config.type, config.level))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));

        wall.scaleWidth(2.2f);
        // Setting Isometric Collider (Normal collider rotated 60 degrees)
        PolygonShape boundingBox = new PolygonShape();
        Vector2 center = wall.getCenterPosition(); // Collider to be set around center of entity
        boundingBox.setAsBox(center.x * 0.25f, center.y * 0.25f, center, (float) (60 * Math.PI / 180));
        wall.getComponent(ColliderComponent.class).setShape(boundingBox);

        return wall;
    }

    public static Entity createCornerWall() {
        Entity cornerWall = createBaseBuilding();
        //Set up building points for texture scaling
        Vector2 leftPoint = new Vector2(88f, 153f); //Bottom leftmost edge in pixels
        Vector2 rightPoint = new Vector2(120f, 134f); //Bottom rightmost edge in pixels

        //Set up building points for isometric collider
        float[] points = new float[] {
                88f, 153f,
                120f, 170f,
                152f, 152f,
                155, 62f,
                119f, 44f,
                88f, 61f,
        };

        cornerWall.addComponent(new TextureRenderComponent("images/wall_pillar.png"))
                .addComponent(new TextureScaler(leftPoint, rightPoint));

        //Scale edge wall precisely
        cornerWall.getComponent(TextureScaler.class).setPreciseScale(CORNER_SCALE);

        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(cornerWall.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        cornerWall.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return cornerWall;
    }

    /**
     * Creates a north/south facing wall connector entity
     * @return north/south facing wall connector
     */
    public static Entity createNSConnector() {
        Entity connector = createBaseBuilding();

        //Set up building points for texture scaling
        Vector2 leftPoint = new Vector2(79f, 131f); //Bottom leftmost edge in pixels
        Vector2 rightPoint = new Vector2(138f, 162f); //Bottom rightmost edge in pixels

        //Set up building points for isometric collider
        float[] points = new float[] {
                79f, 131f,
                138f, 162f,
                146f, 156f,
                146, 77f,
                89f, 48f,
                78f, 53f,
        };

        connector.addComponent(new TextureRenderComponent("images/connector_ns.png"))
                .addComponent(new TextureScaler(leftPoint, rightPoint));

        //Scale connector precisely
        connector.getComponent(TextureScaler.class).setPreciseScale(CONNECTOR_SCALE);

        //Set isometric collider
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(connector.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        connector.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return connector;
    }

    /**
     * Creates a north/south facing gate entity, that allows friendly units to leave and enter the city
     * @return North/South Gate Entity
     */
    public static Entity createNSGate() {
        Entity gate = createBaseBuilding();

        //Create animation component
        TextureAtlas gateAnimationAtlas = ServiceLocator.getResourceService().getAsset("images/ns_gate.atlas", TextureAtlas.class);
        AnimationRenderComponent gateARC = new AnimationRenderComponent(gateAnimationAtlas);
        gateARC.addAnimation("open_gate", 0.1f, Animation.PlayMode.NORMAL);
        gateARC.addAnimation("close_gate", 0.1f, Animation.PlayMode.NORMAL);

        //Set up building points
        Vector2 leftPoint = new Vector2(37f, 125f); //Bottom leftmost edge in pixels
        Vector2 rightPoint = new Vector2(170f, 196f); //Bottom rightmost edge in pixels

        //Add all components
        gate.addComponent(new TextureRenderComponent("images/gate_ns_closed.png"))
            .addComponent(new GateCollider())
            .addComponent(gateARC)
            .addComponent(new TextureScaler(leftPoint, rightPoint))
            .addComponent(new BuildingActions(Building.GATE_NS, 1));
        
        //Scale building precisely
        gate.getComponent(TextureScaler.class).setPreciseScale(GATE_SCALE);


        // Setting Isometric Collider (Normal collider rotated 60 degrees)
        PolygonShape boundingBox = new PolygonShape();
        Vector2 center = gate.getCenterPosition(); // Collider to be set around center of entity
        boundingBox.setAsBox(center.x * 0.5f, center.y * 0.5f, center, (float) (60 * Math.PI / 180));
        gate.getComponent(ColliderComponent.class).setShape(boundingBox);

        return gate;
    }

    /**
     * Creates a east/west facing gate entity, that allows friendly units to leave and enter the city
     * @return East/West facing Gate Entity
     */
    public static Entity createEWGate() {
        Entity gate = createBaseBuilding();

        //Create animation component
        TextureAtlas gateAnimationAtlas = ServiceLocator.getResourceService().getAsset("images/ew_gate.atlas", TextureAtlas.class);
        AnimationRenderComponent gateARC = new AnimationRenderComponent(gateAnimationAtlas);
        gateARC.addAnimation("open_gate", 0.1f, Animation.PlayMode.NORMAL);
        gateARC.addAnimation("close_gate", 0.1f, Animation.PlayMode.NORMAL);

        //Set up building points
        Vector2 leftPoint = new Vector2(37f, 178f); //Bottom leftmost edge in pixels
        Vector2 rightPoint = new Vector2(170f, 113f); //Bottom rightmost edge in pixels

        //Add all components
        gate.addComponent(new TextureRenderComponent("images/gate_ew_closed.png"))
                .addComponent(new GateCollider())
                .addComponent(gateARC)
                .addComponent(new TextureScaler(leftPoint, rightPoint))
                .addComponent(new BuildingActions(Building.GATE_EW, 1));

        //Scale building precisely
        gate.getComponent(TextureScaler.class).setPreciseScale(GATE_SCALE);

        // Setting Isometric Collider (Normal collider rotated 60 degrees)
        PolygonShape boundingBox = new PolygonShape();
        Vector2 center = gate.getCenterPosition(); // Collider to be set around center of entity
        boundingBox.setAsBox(center.x * 0.5f, center.y * 0.5f, center, (float) (60 * Math.PI / 180));
        gate.getComponent(ColliderComponent.class).setShape(boundingBox);

        return gate;
    }

    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
