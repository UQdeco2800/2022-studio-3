package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create a building entity with predefined components.
 * <p> Each building entity type should have a creation method that returns a corresponding entity.
 * <p> Predefined buildings properties are loaded from a config stored as a json file and should have
 * the properties stored in 'BuildingConfigs'.
 */
public class BuildingFactory {
    // Default physical collider of buildings made through building factory
    private static final float COLLIDER_SCALE = 0.9f;
    private static final BuildingConfigs configs =
            FileLoader.readClass(BuildingConfigs.class, "configs/buildings.json");

    /**
     * @return a new Entity with universal building components
     */
    public static Entity createBaseBuilding() {
        return new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchPlayerInputComponent());
    }

    /**
     * Creates entity, adds and configures TownHall components
     * @return TownHall Entity
     */
    public static Entity createTownHall() {
        Entity townHall = createBaseBuilding();
        TownHallConfig config = configs.townHall;

        townHall.addComponent(new TextureRenderComponent("images/base.png"))
                .addComponent(new BuildingActions(config.type, config.level));

        townHall.scaleWidth(5f);
        PhysicsUtils.setScaledColliderCentered(townHall, 0.5f, 0.5f);

        townHall.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
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
                .addComponent(new BuildingActions(config.type, config.level));

        barracks.scaleWidth(2f);
        PhysicsUtils.setScaledCollider(barracks, COLLIDER_SCALE, COLLIDER_SCALE);

        barracks.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
        return barracks;
    }

    /**
     * Creates entity, adds and configures MedievalBarracks components
     * @return MedievalBarracks Entity
     */
    public static Entity createBarracksMedieval() {
        Entity barracks = createBaseBuilding();
        BarracksConfig config = configs.barracks;

        barracks.addComponent(new TextureRenderComponent("images/barracks medieval.png"))
                .addComponent(new BuildingActions(config.type, config.level));

        barracks.scaleWidth(2f);
        PhysicsUtils.setScaledCollider(barracks, COLLIDER_SCALE, COLLIDER_SCALE);

        barracks.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
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
            .addComponent(new BuildingActions(config.type, config.level));

        wall.scaleWidth(2.2f);
        PhysicsUtils.setScaledColliderCentered(wall, .2f, .2f);

        wall.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
        return wall;
    }


    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
