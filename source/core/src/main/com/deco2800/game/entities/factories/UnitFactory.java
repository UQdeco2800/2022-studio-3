package com.deco2800.game.entities.factories;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.friendly.TroopContainerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

import java.util.ArrayList;

public class UnitFactory {
    private static final BaseUnitConfig stats =
            FileLoader.readClass(BaseUnitConfig.class, "configs/ExampleUnit");


    public static Entity createExampleUnit() {
        ArrayList<Entity> troops = new ArrayList<>();
        for (int i = 0; i < stats.troops; i++) {
            //TODO: add propagation of animations to this construction
            Entity troop = new Entity().addComponent(new TextureRenderComponent(
                            "images/simpleman.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                    .addComponent(new CombatStatsComponent(stats.health,
                            stats.baseAttack, stats.baseDefence));
            PhysicsUtils.setScaledCollider(troop, 0.8f, 1f);
            troop.getComponent(ColliderComponent.class).setDensity(1f);
            troop.getComponent(TextureRenderComponent.class).scaleEntity();
            troops.add(troop);
        }
        // TODO: add control to unit entity
        Entity unit =
                new Entity().addComponent(new TroopContainerComponent(troops,
                        50f));
        return unit;
    }
}
