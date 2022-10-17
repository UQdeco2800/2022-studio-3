package com.deco2800.game.soldiers.factories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.soldiers.type.SwordsmanComponent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(GameExtension.class)
public class SwordsmanFactoryTest {
    private static Entity swordsman;

    @BeforeAll
    void BeforeAll(){
        swordsman = SwordsmanFactory.createSwordsman();
    }

    @Test
    static void checkHopliteComponent(){
        assertNotNull(swordsman.getComponent(SwordsmanComponent.class));
        assertTrue(swordsman.getComponent(SwordsmanComponent.class).getIsSwordsman() == 1);
    }
}
