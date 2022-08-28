package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.CombatStatsComponent;
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

public class BuildingFactory {
    // Default physical collider of buildings made through building factory
    private static final float COLLIDER_SCALE = 0.9f;
    private static final BuildingConfigs configs =
            FileLoader.readClass(BuildingConfigs.class, "configs/buildings.json");

    public static Entity createBaseBuilding() {
        return new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    }

    public static Entity createTownHall() {
        Entity townHall = createBaseBuilding();
        TownHallConfig config = configs.townHall;

        townHall.addComponent(new TextureRenderComponent("images/Base.png"))
                .addComponent(new TouchPlayerInputComponent())
                .addComponent(new BuildingActions(config.level));
        townHall.getComponent(TextureRenderComponent.class).scaleEntity();
        townHall.scaleWidth(4f);
        PhysicsUtils.setScaledCollider(townHall, COLLIDER_SCALE, COLLIDER_SCALE);

        townHall.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
        return townHall;
    }

    public static Entity createBarracks() {
        Entity barracks = createBaseBuilding();
        BarracksConfig config = configs.barracks;

        barracks.addComponent(new TextureRenderComponent("images/barracks atlantis.png"))
                .addComponent(new BuildingActions(config.level));

        barracks.getComponent(TextureRenderComponent.class).scaleEntity();
        barracks.scaleWidth(2f);
        PhysicsUtils.setScaledCollider(barracks, COLLIDER_SCALE, COLLIDER_SCALE);

        barracks.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
        return barracks;
    }

    public static Entity createBarracksMedieval() {
        Entity barracks = createBaseBuilding();
        BarracksConfig config = configs.barracks;

        barracks.addComponent(new TextureRenderComponent("images/barracks medieval.png"))
                .addComponent(new BuildingActions(config.level));

        barracks.getComponent(TextureRenderComponent.class).scaleEntity();
        barracks.scaleWidth(2f);
        PhysicsUtils.setScaledCollider(barracks, COLLIDER_SCALE, COLLIDER_SCALE);

        barracks.addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));
        return barracks;
    }

    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
