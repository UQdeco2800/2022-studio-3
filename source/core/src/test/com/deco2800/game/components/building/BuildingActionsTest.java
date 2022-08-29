package com.deco2800.game.components.building;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingActionsTest {

    @Test
    void getLevel() {
        assertEquals(new BuildingActions(5).getLevel(), 5);
    }

    @Test
    void addLevel() {
        BuildingActions ba = new BuildingActions(1);
        ba.addLevel();
        assertEquals(ba.getLevel(), 2);
        ba.addLevel();
        assertEquals(ba.getLevel(), 3);
    }
}