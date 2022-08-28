package com.deco2800.game.worker.components.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ForagerComponentTest {

    @Test
    void getIsForager() {
        ForagerComponent foragerComponent = new ForagerComponent();

        assertEquals(1, foragerComponent.getIsForager());
    }
}