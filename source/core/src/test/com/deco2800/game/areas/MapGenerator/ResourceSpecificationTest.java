package com.deco2800.game.areas.MapGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.extensions.GameExtension;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class ResourceSpecificationTest {
    /**
     * Attempts to construct a typical resource
     */
    @Test
    public void constructValidResourceTest() {
        try {
            ResourceSpecification resource = new ResourceSpecification("Wood", 5, 5, 2, 5, 10);
            ResourceSpecification resource2 = new ResourceSpecification("Stone", 1, 5, 2, 10, 10);
        } catch (Exception e) {
            fail(); //No exception should occur
        }
    }

    /**
     * Attempts to construct an invalid resource - should throw an IllegalArgumentException
     */
    @Test
    public void constructInvalidResourceTest() {
        //Negative dimensions test
        try {
            ResourceSpecification resource = new ResourceSpecification("Wood", -1, 5, 2, 5, 10);
            fail();
        } catch (IllegalArgumentException squashed) {

        } catch (Exception e) {
            fail(); //Wrong exception
        }
        //Negative minAmount
        try {
            ResourceSpecification resource = new ResourceSpecification("Wood", 5, 5, -2, 5, 10);
            fail();
        } catch (IllegalArgumentException squashed) {

        } catch (Exception e) {
            fail(); //Wrong exception
        }
        //Min amount greater than max amount
        try {
            ResourceSpecification resource = new ResourceSpecification("Wood", 5, 5, 6, 2, 10);
            fail();
        } catch (IllegalArgumentException squashed) {

        } catch (Exception e) {
            fail(); //Wrong exception
        }
    }

    /**
     * Ensures that the correct data is being returned from ResourceSpecification getters
     */
    @Test
    public void getterTests() {
        ResourceSpecification resource = new ResourceSpecification("dummyResource", 5,7,8,10,11);
        assertEquals("dummyResource", resource.getName());
        assertEquals(5, resource.getWidth());
        assertEquals(7, resource.getHeight());
        assertEquals(8, resource.getMinAmount());
        assertEquals(10, resource.getMaxAmount());
        assertEquals(11, resource.getPreferredDistance());
    }

    /**
     * Checks to ensure placements can be added to the ResourcesSpecification, and they can be cleared
     */
    @Test
    public void addClearPlacementTest() {
        ResourceSpecification resource = new ResourceSpecification("dummyResource", 5,7,8,10,11);
        Coordinate newPoint = new Coordinate(1,1);
        resource.addPlacement(newPoint);
        List<Coordinate> placements = resource.getPlacements();
        //Assert that the new placement has been added/returned properly
        assertEquals(newPoint, placements.get(0));
        //Test updating potential placements
        resource.updatePotentialPlacements(placements);
        List<Coordinate> potentialPlacements = resource.getPotentialPlacements();
        assertEquals(1, potentialPlacements.size());
        assertEquals(newPoint, potentialPlacements.get(0));
        //Check to ensure rest placements empties both placement lists
        resource.resetPlacements();
        placements = resource.getPlacements();
        potentialPlacements = resource.getPotentialPlacements();
        assertEquals(0, placements.size());
        assertEquals(0, potentialPlacements.size());
    }
}
