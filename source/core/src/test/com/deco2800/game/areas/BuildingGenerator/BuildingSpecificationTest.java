package com.deco2800.game.areas.BuildingGenerator;
import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingSpecification;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

@ExtendWith(GameExtension.class)
public class BuildingSpecificationTest {

    /**
     * Ensures BuildingSpecification returns accurate values
     */
    @Test
    public void getBuildingSpecificationAttributesTest() {
        BuildingSpecification testSpec = new BuildingSpecification("Barracks", 3, 5, "2,3", 2);
        assertEquals("Barracks", testSpec.getName());
        assertEquals(3, testSpec.getWidth());
        assertEquals(5, testSpec.getHeight());
        assertEquals(new GridPoint2(2,3), testSpec.getDoor());
    }

    /**
     * Ensures building placement can be tracked, and it can be wiped in the case where building placement has to be redone
     */
    @Test
    public void placeResetBuildingTest() {
        BuildingSpecification testSpec = new BuildingSpecification("Barracks", 3, 5, "2,3", 2);
        assertEquals(2, testSpec.numRemaining());
        testSpec.incNumPlaced();
        assertEquals(1, testSpec.numRemaining());
        testSpec.incNumPlaced();
        assertEquals(0, testSpec.numRemaining());
        testSpec.resetPlacements();
        assertEquals(2, testSpec.numRemaining());
    }

}
