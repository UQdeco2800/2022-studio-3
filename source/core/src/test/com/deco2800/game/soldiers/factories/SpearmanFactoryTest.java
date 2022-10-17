package com.deco2800.game.soldiers.factories;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.soldiers.type.SpearmanComponent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@ExtendWith(GameExtension.class)
public class SpearmanFactoryTest {
    private static Entity spearman;

    @BeforeAll
    void BeforeAll(){
        spearman = SpearmanFactory.createSpearman();
    }

    @Test
    static void checkHopliteComponent(){
        assertNotNull(spearman.getComponent(SpearmanComponent.class));
        assertTrue(spearman.getComponent(SpearmanComponent.class).getIsSpearman() == 1);
    }
}
