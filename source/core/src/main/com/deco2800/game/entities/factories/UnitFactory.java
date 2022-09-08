package com.deco2800.game.entities.factories;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.friendly.TroopContainerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

import java.util.ArrayList;

public class UnitFactory {
    private static final UnitConfigs stats =
            FileLoader.readClass(UnitConfigs.class, "configs/units.json");


    public static Entity createExampleUnit() {
        ArrayList<Entity> troops = new ArrayList<>();
        for (int i = 0; i < stats.example.troops; i++) {
            //TODO: add animations to this construction
            Entity troop = new Entity().addComponent(new TextureRenderComponent(
                            "images/simpleman.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ALL))
                    .addComponent(new CombatStatsComponent(stats.example.health,
                            stats.example.baseAttack,
                            stats.example.baseDefence));
            PhysicsUtils.setScaledCollider(troop, 0.2f, 1f);
            troop.getComponent(ColliderComponent.class).setDensity(1f);
            troop.getComponent(TextureRenderComponent.class).scaleEntity();
            troop.setScale(0.5f, 0.5f);
            troops.add(troop);
        }
        // TODO: add control to unit entity
        Entity unit =
                new Entity().addComponent(new TroopContainerComponent(troops,
                        stats.example.movementRadius))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        PhysicsUtils.setRadiusCollider(unit, stats.example.movementRadius);
        return unit;
    }

    private UnitFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
