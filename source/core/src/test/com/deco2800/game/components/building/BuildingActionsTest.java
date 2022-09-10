package com.deco2800.game.components.building;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingActionsTest {

    @Test
    void getLevel() {
        assertEquals(new BuildingActions(Building.WALL, 5).getLevel(), 5);
    }

    @Test
    void addLevel() {
        BuildingActions ba = new BuildingActions(Building.WALL, 1);
        ba.addLevel();
        assertEquals(ba.getLevel(), 2);
        ba.addLevel();
        assertEquals(ba.getLevel(), 3);
    }

    @Test
    void getType() {
        assertEquals(new BuildingActions(Building.WALL, 5).getType(), Building.WALL);
        assertEquals(new BuildingActions(Building.TOWNHALL, 5).getType(), Building.TOWNHALL);
        assertEquals(new BuildingActions(Building.BARRACKS, 5).getType(), Building.BARRACKS);
    }
}