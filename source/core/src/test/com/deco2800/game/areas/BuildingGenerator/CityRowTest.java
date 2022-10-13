package com.deco2800.game.areas.BuildingGenerator;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.MapGenerator.Buildings.Building;
import com.deco2800.game.areas.MapGenerator.Buildings.CityRow;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

@ExtendWith(GameExtension.class)
public class CityRowTest {
    //Helper method for tests to instantiate a new CityRow
    private CityRow makeCityRow() {
        return new CityRow(44,3,3,4,0);
    }

    /**
     * Ensures cityRow getters function as intended
     */
    @Test
    public void getCityRowAttributesTest() {
        CityRow testRow = makeCityRow();
        assertEquals(44, testRow.getRowWidth());
        assertEquals(0, testRow.getHeight());
        assertEquals(0, testRow.getIndex());
        assertEquals(34, testRow.getSpaceRemaining());
        assertEquals(0, testRow.getBuildings().size());
    }

    /**
     * Ensures buildings can be added to CityRow successfully
     */
    @Test
    public void addBuildingTest() {
        CityRow testRow = makeCityRow();
        Building building = new Building(3,3, "Colosseum", new GridPoint2(2,2));
        Boolean outcome = testRow.addBuilding(building, false);
        //Building should fit
        assertEquals(true, outcome);
        //Correct space taken up
        assertEquals(28, testRow.getSpaceRemaining());
        //Added Building can be successfully retrieved
        assertEquals("Colosseum", testRow.getBuildings().get(0).getName());
        //Building placement can be retrieved - x is shifted by WALL_BUFFER
        assertEquals(new Coordinate(3,0), testRow.getBuildings().get(0).getPlacement());

        Building giantBuilding = new Building(26,50, "Giant Building", new GridPoint2(25,49));
        //Cannot add a building that is too big for the current space (26 + city buffer of 3 is too large to fit into 28 remaining spaces)
        assertEquals(false, testRow.addBuilding(giantBuilding, false));

        //Row can be emptied appropriately
        testRow.resetRow();
        assertEquals(34, testRow.getSpaceRemaining());
    }
}
