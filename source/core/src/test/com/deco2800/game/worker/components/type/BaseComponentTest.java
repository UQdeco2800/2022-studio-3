package com.deco2800.game.worker.components.type;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.BeforeAll;
import com.deco2800.game.extensions.GameExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
class BaseComponentTest {

    private static BaseComponent baseComponent;

    @BeforeAll
    static void BeforeAll(){
        baseComponent = new BaseComponent();
    }

    @Test
    void getIsBase() {
        assertEquals(1, baseComponent.getIsBase());
    }

    @Test
    void getWoodDefined(){
        assertEquals(0, baseComponent.getWood());
    }

    @Test
    void getMetalDefined(){
        assertEquals(0, baseComponent.getMetal());
    }

    @Test
    void getStoneDefined(){
        assertEquals(0, baseComponent.getStone());
    }

    @Test
    void updateBaseStatsDefined(){
        baseComponent.updateBaseStats(1, 1, 1);
        assertEquals(1, baseComponent.getWood());
        assertEquals(1, baseComponent.getMetal());
        assertEquals(1, baseComponent.getStone());
    }
}