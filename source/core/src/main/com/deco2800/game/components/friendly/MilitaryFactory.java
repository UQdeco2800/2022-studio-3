package com.deco2800.game.components.friendly;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.ServiceLocator;


public class MilitaryFactory {
    private static final BaseUnitConfig stats =
            FileLoader.readClass(BaseUnitConfig.class, "configs/baseunitconfig.json");    

    public static Entity createMilitary() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForWorker();
        Entity militaryUnit = new Entity().addComponent(inputComponent)
                                       .addComponent(new CombatStatsComponent(0, 0, 0));

        militaryUnit.getComponent(TouchAttackComponent.class);
        militaryUnit.getComponent(ColliderComponent.class).setDensity(1.0f);

        PhysicsUtils.setScaledCollider(militaryUnit, 1.0f, 1.0f);
        return militaryUnit;
    }
}
