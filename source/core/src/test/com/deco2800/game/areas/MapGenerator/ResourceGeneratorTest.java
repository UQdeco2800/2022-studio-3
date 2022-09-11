package com.deco2800.game.areas.MapGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.SerializationException;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class ResourceGeneratorTest {
    /**
     * Creates a MapGenerator with pre-defined values
     * @return map generator with static values for testing
     */
    private static MapGenerator createMapGenerator() {
        return AtlantisTerrainFactory.makeMapGenerator();
    }

    /**
     * Ensures the /configs/resources.json file is correctly read by ResourceGenerator
     */
    @Test
    public void readJsonTest() {
        MapGenerator mg = createMapGenerator();
        ResourceGenerator rg = new ResourceGenerator(mg);
        List<ResourceSpecification> resources = new ArrayList<>();
        //Attempt to read resources json
        try {
            //Read in resourceSpecification data from configs/resources.json
            /* For each resourceSpecification object defined under the "resources" header in
                resources.json, add a resource object to the resources list.
            */
            JsonReader json = new JsonReader();
            JsonValue resourcesJson = json.parse(Gdx.files.internal("configs/resources.json")).get("resources");

            for (JsonValue r : resourcesJson.iterator()) {
                //Define object
                ResourceSpecification resource = new ResourceSpecification(r.getString("name"),
                        r.getInt("width"), r.getInt("height"), r.getInt("minAmount"),
                        r.getInt("maxAmount"), r.getInt("preferredDistance"));
                //Add object to list
                resources.add(resource);
            }
        } catch (SerializationException e) {
            //configs/resources.json should be able to be read every time
            fail("configs/resources.json could not be found or parsed properly");
        }
        List<ResourceSpecification> rgResourceList = rg.getResources();
        //Confirm the ResourceGenerator has read the same resource list
        for (ResourceSpecification r : resources) {
            boolean existsInResourceGenerator = false;
            //For each resource read from the config file, ensure it exists in the ResourceGenerator
            for (ResourceSpecification rgResource : rgResourceList) {
                if (r.getPreferredDistance() == rgResource.getPreferredDistance()
                    && r.getHeight() == rgResource.getHeight()
                    && r.getWidth() == rgResource.getWidth()
                    && r.getName().equals(rgResource.getName())
                    && r.getMaxAmount() == rgResource.getMaxAmount()
                    && r.getMinAmount() == rgResource.getMinAmount()
                    && rgResource.getAmount() >= rgResource.getMinAmount()
                    && rgResource.getAmount() <= rgResource.getMaxAmount()) {
                    existsInResourceGenerator = true;
                }
            }
            assertTrue("Resource: " + r.getName() + " incorrectly read from resources.json", existsInResourceGenerator);
        }
    }

    /**
     * Test to ensure resources are not placed in invalid bounds (i.e. outside of the map, in the ocean
     * or within the city bounds
     */
    @Test
    public void checkValidPlacements() {
        MapGenerator mg = createMapGenerator();
        ResourceGenerator rg = new ResourceGenerator(mg);

        //Get the test game map
        char[][] map = mg.getMap();

        List<ResourceSpecification> resources = rg.getResources();
        //Determine city bounds
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        Coordinate ne = cityDetails.get("NE");
        Coordinate sw = cityDetails.get("SW");
        int minX = sw.getX();
        int maxX = ne.getX();
        int minY = ne.getY();
        int maxY = sw.getY();

        for (ResourceSpecification r : resources) {
            //Get the tiles of each placement of each resource
            List<Coordinate> placements = r.getPlacements();
            //Check each placement for incorrect conditions
            for (Coordinate c : placements) {
                if (!(c.inBounds(mg))) {
                    fail("Resource created out of map bounds");
                } else if (c.getX() >= minX && c.getX() <= maxX && c.getY() >= minY && c.getY() <= maxY) {
                    fail("Resource created in city bounds");
                } else if (map[c.getY()][c.getX()] == mg.getOceanChar()) {
                    fail("Resource created in the ocean");
                }
            }
        }
    }

    /**
     * Checks to ensure the game can reliably place resources given the constraints given by configs/resources.json
     * This is important as, for example, a resource may be generated in a range of {1,1000} creations per run,
     * meaning some runs it will simply place a few resources on the map, but in others it may not have space
     */
    @Test
    public void checkReliableGeneration() {
        int testIterations = 50;
        try {
            for (int i = 0; i < testIterations; i++) {
                MapGenerator mg = createMapGenerator();
                //Creating a ResourceGenerator will automatically allocate space for resources,
                //An IllegalArgumentException will be thrown if it is unable to find space
                ResourceGenerator rg = new ResourceGenerator(mg);
            }
        } catch (Exception e) {
            fail("Resources cannot be consistently placed on map");
        }
    }
}
