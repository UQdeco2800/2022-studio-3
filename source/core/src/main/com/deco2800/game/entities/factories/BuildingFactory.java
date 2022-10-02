package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.BuildingUIDataComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.UnitSpawningComponent;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.tasks.EnemyMovement;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
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

    private static final String HALF_HEALTH = "50-idle";
    private static final String HALF_HEALTH_TRANSITION = "50";
    private static final String FULL_HEALTH = "100";
    private static final String FULL_ATTACKED = "attacked";
    private static final String COLLAPSE = "collapse";
    private static final String REBUILD = "reconstruction";

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
        // TODO: Replace town hall with new design.
        final float TH_SCALE = 7f;
        Entity townHall = createBaseBuilding();
        TownHallConfig config = configs.townHall;

        townHall.addComponent(new TextureRenderComponent("images/base.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal))
                .addComponent(new BaseComponent())
                .addComponent(new HighlightedTextureRenderComponent("images/Base_Highlight.png"))
                .addComponent(new BuildingUIDataComponent());

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
        // TODO: Change barracks from static texture to animation.
        // TODO: Add spawning component
        barracks.addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new HighlightedTextureRenderComponent("images/barracks_level_1.0_Highlight.png"))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new BuildingUIDataComponent());

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
     * Creates a titan shrine entity, a titan shrine is an enemy building
     * that spawns titan's.
     * @return Titan Shrine Building Entity
     */
    public static Entity createTitanShrine() {
        final float TITANSHRINE_SCALE = 10f;
        Entity titanShrine = createBaseBuilding();
        TitanShrineConfig config = configs.titanShrine;

        AnimationRenderComponent animator =
                new AnimationRenderComponent(ServiceLocator.getResourceService()
                                                           .getAsset("images/titanshrine.atlas",
                                                                        TextureAtlas.class));

        animator.addAnimation(REBUILD, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(FULL_ATTACKED, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(FULL_HEALTH, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(HALF_HEALTH, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(HALF_HEALTH_TRANSITION, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation("default", 0.1f, Animation.PlayMode.NORMAL);

        titanShrine
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new BuildingUIDataComponent())
                .addComponent(animator);

        // Setting Isometric Collider
        // Points (in pixels) on the texture to set the collider to
        float[] points = new float[]{
                605f, 1111,    // Vertex 0        3
                1100f, 870f,     // Vertex 1    4 /   \ 2
                1100f, 800f,     // Vertex 2     |     |
                605f, 581f,     // Vertex 3    5 \   / 1
                100f, 800f,     // Vertex 4        0
                100f, 874f      // Vertex 5
        };

        Texture titanShrineTexture = ServiceLocator.getResourceService()
                                                   .getAsset("images/titanshrine-default.png",
                                                             Texture.class);
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

        // TODO: Make component to spawn titan.
        titanShrine.getComponent(AnimationRenderComponent.class).startAnimation("default");
        titanShrine.getComponent(AnimationRenderComponent.class).scaleEntity();
        titanShrine.scaleWidth(TITANSHRINE_SCALE);

        // TODO: Set isometric colliders

        return titanShrine;
    }

    /**
     * Creates a ship entity, a ship shrine is an enemy building that transports
     * enemy entities from sea to the shores of the island.
     * that spawns titan's.
     * @return Ship Building Entity
     */
    public static Entity createShip() {
        final float SHIP_SCALE = 5f;
        Entity ship = createBaseBuilding();
        ShipConfig config = configs.ship;

        // TODO: Change sprite to ship when its done.
        AnimationRenderComponent animator =
                new AnimationRenderComponent(ServiceLocator.getResourceService()
                        .getAsset("images/titanshrine.atlas",
                                TextureAtlas.class));

        animator.addAnimation(REBUILD, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(FULL_ATTACKED, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(FULL_HEALTH, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(HALF_HEALTH, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(HALF_HEALTH_TRANSITION, 0.1f, Animation.PlayMode.NORMAL);

        ship
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new PhysicsMovementComponent());

        // TODO: Make component to spawn enemy units.

        ship.scaleWidth(SHIP_SCALE);

        // TODO: Set isometric colliders

        return ship;
    }

    /**
     * Creates a trebuchet entity, a trebuchet is a friendly building
     * that defends the shores of atlantis, it primarily attacks enemy
     * transport ships.
     * that spawns titan's.
     * @return Trebuchet building entity.
     */
    public static Entity createTrebuchet() {
        final float SHIP_SCALE = 5f;
        Entity trebuchet = createBaseBuilding();
        ShipConfig config = configs.ship;

        // TODO: Change sprite to ship when its done.

        AnimationRenderComponent animator =
                new AnimationRenderComponent(ServiceLocator.getResourceService()
                        .getAsset("images/titanshrine.atlas",
                                    TextureAtlas.class));

        animator.addAnimation(REBUILD, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(FULL_ATTACKED, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(FULL_HEALTH, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(HALF_HEALTH, 0.1f, Animation.PlayMode.NORMAL);
        animator.addAnimation(HALF_HEALTH_TRANSITION, 0.1f, Animation.PlayMode.NORMAL);

        trebuchet
            .addComponent(new BuildingActions(config.type, config.level))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));

        // TODO: Make component to spawn enemy units.

        trebuchet.scaleWidth(SHIP_SCALE);

        // TODO: Set isometric colliders

        return trebuchet;
    }

    /**
     * Creates a wall entity, adds and configures Wall components
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


    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
