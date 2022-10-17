package com.deco2800.game.soldiers.factories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.factories.SoldierFactory;

import static org.junit.Assert.assertNotNull;

@ExtendWith(GameExtension.class)
public class SoldierFactoryTest {

    private static Entity soldier;

    @BeforeAll
    void BeforeAll(){
        soldier = SoldierFactory.createSoldier();
    }

    @Test
    static void shouldDetectSoldierComponents() {
        assertNotNull(soldier.getComponent(FriendlyComponent.class));
        assertNotNull(soldier.getComponent(TouchAttackComponent.class));
        assertNotNull(soldier.getComponent(PhysicsComponent.class));
        assertNotNull(soldier.getComponent(SelectableComponent.class));
        assertNotNull(soldier.getComponent(HealthBarComponent.class));
    }
}
