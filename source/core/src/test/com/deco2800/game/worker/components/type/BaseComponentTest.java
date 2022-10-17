package com.deco2800.game.worker.components.type;

import com.badlogic.gdx.utils.compression.lzma.Base;
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
        assertEquals(BaseComponent.STARTING_WOOD, baseComponent.getWood());
    }

    @Test
    void getMetalDefined(){
        assertEquals(BaseComponent.STARTING_METAL, baseComponent.getMetal());
    }

    @Test
    void getStoneDefined(){
        assertEquals(BaseComponent.STARTING_STONE, baseComponent.getStone());
    }

    @Test
    void updateBaseStatsDefined(){
        baseComponent.updateBaseStats(1, 1, 1);
        assertEquals(BaseComponent.STARTING_WOOD + 1, baseComponent.getWood());
        assertEquals(BaseComponent.STARTING_METAL + 1,
                baseComponent.getMetal());
        assertEquals(BaseComponent.STARTING_STONE + 1,
                baseComponent.getStone());
    }
}