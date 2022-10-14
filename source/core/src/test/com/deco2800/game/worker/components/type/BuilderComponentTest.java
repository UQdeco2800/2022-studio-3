package com.deco2800.game.worker.components.type;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderComponentTest {
    @Test
    void getIsBuilder() {
        BuilderComponent builderComponent = new BuilderComponent();
        assertNotNull(builderComponent);
        assertEquals(1, builderComponent.getIsBuilder());
    }
}