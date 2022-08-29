package com.deco2800.game.worker.components.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseComponentTest {

    @Test
    void getIsBase() {
        BaseComponent baseComponent = new BaseComponent();
        assertEquals(1, baseComponent.getIsBase());
    }
}