package com.deco2800.game.soldiers.factories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.soldiers.type.HopliteComponent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(GameExtension.class)
public class HopliteFactoryTest {

    private static Entity hoplite;

    @BeforeAll
    void BeforeAll(){
        hoplite = HopliteFactory.createHoplite();
    }

    @Test
    static void checkHopliteComponent(){
        assertNotNull(hoplite.getComponent(HopliteComponent.class));
        assertTrue(hoplite.getComponent(HopliteComponent.class).getIsHoplite() == 1);
    }
}
