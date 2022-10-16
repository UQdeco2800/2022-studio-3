package com.deco2800.game.soldiers.factories;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.EntityType;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.movement.SoldierIdleTask;

public class SoldierFactory {

    private static final UnitConfigs stats = FileLoader.readClass(UnitConfigs.class, "configs/units.json");

    public static Entity createSoldier() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForFriendlyUnit();
        AITaskComponent aiComponent = new AITaskComponent().addTask(new SoldierIdleTask());
        Entity soldier =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SOLDIER))
                        .addComponent(new FriendlyComponent())
                        .addComponent(new SelectableComponent())
                        .addComponent(new HealthBarComponent(EntityType.FRIENDLY))
                        .addComponent(aiComponent)
                        .addComponent(inputComponent)
                        .addComponent(new TouchAttackComponent(PhysicsLayer.NPC, 1.5f, 20));

        PhysicsUtils.setScaledCollider(soldier, 0.6f, 0.3f);
        soldier.getComponent(ColliderComponent.class).setDensity(1.5f);
        return soldier;
    }

    private SoldierFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
