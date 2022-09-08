package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
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
            FileLoader.readClass(ResourceConfig.class, "configs/Base.json");

    /**
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
        Entity townHall = createBaseBuilding();
        TownHallConfig config = configs.townHall;

        townHall.addComponent(new TextureRenderComponent("images/base.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal))
                .addComponent(new BaseComponent());

        townHall.scaleWidth(7f);
        // Setting Isometric Collider
        PolygonShape boundingBox = new PolygonShape();
        Vector2[] vertices = {new Vector2(0.2f,2.7f), new Vector2(3.4f,1.2f), // Bottom left, Bottom Right
                new Vector2(6.4f,3.5f), new Vector2(3.8f,5f)}; // Top Right, Top Left
        boundingBox.set(vertices);
        townHall.getComponent(ColliderComponent.class).setShape(boundingBox);

        return townHall;
    }

    /**
     * Creates entity, adds and configures Barracks components
     * @return Barracks Entity
     */
    public static Entity createBarracks() {
        Entity barracks = createBaseBuilding();
        BarracksConfig config = configs.barracks;

        barracks.addComponent(new TextureRenderComponent("images/isometric barracks current.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));

        barracks.scaleWidth(5f);
        // Setting Isometric Collider
        PolygonShape boundingBox = new PolygonShape();
        Vector2[] vertices = {new Vector2(0.6f,1.3f), new Vector2(2.5f,0.4f), // Bottom Left, Bottom Mid
                new Vector2(4.3f,1.3f), new Vector2(2.5f,3.5f), // Bottom Right, Top Middle
                new Vector2(0.6f,2.6f), new Vector2(4.3f,2.6f)}; // Top Left, Top Right
        boundingBox.set(vertices);
        barracks.getComponent(ColliderComponent.class).setShape(boundingBox);

        return barracks;
    }

    /**
     * Creates entity, adds and configures Wall components
     * @return Barracks Entity
     */
    public static Entity createWall() {
        Entity wall = createBaseBuilding();
        WallConfig config = configs.wall;

        wall.addComponent(new TextureRenderComponent("images/stone_wall.png"))
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
