package com.deco2800.game.areas.BuildingGenerator;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.MapGenerator.Buildings.Building;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

@ExtendWith(GameExtension.class)
public class BuildingTest {
    /**
     * Ensures getter methods are working as intended
     */
    @Test
    public void getBuildingAttributesTest() {
        Building testBuilding = new Building(2,3, "Coloseum", new GridPoint2(1,2));
        assertEquals(2, testBuilding.getWidth());
        assertEquals(3, testBuilding.getHeight());
        assertEquals("Coloseum", testBuilding.getName());
        assertEquals(1, testBuilding.getDoor().x);
        assertEquals(2, testBuilding.getDoor().y);
    }

    /**
     * Ensures buildings can have a placement added to them
     */
    @Test
    public void getSetBuildingPlacements() {
        Coordinate testPlacement = new Coordinate(2,23);
        Building testBuilding = new Building(2,3, "Coloseum", new GridPoint2(1,2));
        testBuilding.setPlacement(testPlacement);
        assertEquals(testPlacement, testBuilding.getPlacement());
    }
}
