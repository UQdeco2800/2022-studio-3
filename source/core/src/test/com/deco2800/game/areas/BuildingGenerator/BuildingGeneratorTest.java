package com.deco2800.game.areas.BuildingGenerator;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.areas.MapGenerator.Buildings.Building;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingSpecification;
import com.deco2800.game.areas.MapGenerator.Buildings.CityRow;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.extensions.GameExtension;
import net.dermetfan.gdx.physics.box2d.PositionController;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@ExtendWith(GameExtension.class)
public class BuildingGeneratorTest {
    //Helper method to make a BuildingGenerator based off current map dimensions being used in game
    private BuildingGenerator makeBuildingGenerator() {
        return new BuildingGenerator(AtlantisTerrainFactory.makeMapGenerator());
    }

    @Test
    public void getBGAttributesTest() {
        MapGenerator mg = AtlantisTerrainFactory.makeMapGenerator();
        BuildingGenerator bg;
        try {
            bg = new BuildingGenerator(mg);
        } catch (Exception e) {
            fail("BuildingGenerator run failed for current map/city/island size");
            return;
        }

        Map<String, Coordinate> cityDetails = mg.getCityDetails();

        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;

        assertEquals(cityHeight, bg.getCityHeight());
        assertEquals(cityWidth, bg.getCityWidth());
    }

    @Test
    public void configReadTest() {
        //Read config file
        List<BuildingSpecification> buildings = new ArrayList<>();

        JsonReader json = new JsonReader();
        JsonValue buildingsJson = json.parse(Gdx.files.internal("configs/buildingSpecifications.json")).get("buildings");

        for (JsonValue b : buildingsJson.iterator()) {
            //Define object
            BuildingSpecification building = new BuildingSpecification(b.getString("name"),
                    b.getInt("width"),b.getInt("height"),b.getString("door"),b.getInt("num"));

            //Add object to list
            buildings.add(building);
        }

        BuildingGenerator bg = makeBuildingGenerator();
        List<BuildingSpecification> bgBuildings = bg.getBuildings();
        //Ensure lists match
        for (BuildingSpecification bs : buildings) {
            boolean match = false;
            for (BuildingSpecification bsbg : bgBuildings) {
                if (bs.equals(bsbg)) {
                    match = true;
                }
            }
            if (!match) {
                fail("BuildingSpecification list incorrectly loaded");
            }
        }
    }

    /**
     * Ensures buildings are placed within city bounds or over the top of each other
     */
    @Test
    public void buildingPlacementTest() {
        MapGenerator mg = AtlantisTerrainFactory.makeMapGenerator();
        BuildingGenerator bg = new BuildingGenerator(mg);
        Map<String, Coordinate> cityDetails = mg.getCityDetails();

        int minCityX = cityDetails.get("NW").getX();
        int minCityY = cityDetails.get("NW").getY();
        int maxCityX = cityDetails.get("SE").getX();
        int maxCityY = cityDetails.get("SE").getY();

        int totalBuildingsPlaced = 0;

        List<CityRow> cityRows = bg.getCityRows();
        for (CityRow cr : cityRows) {
            List<Building> buildings = cr.getBuildings();
            totalBuildingsPlaced += buildings.size();
            for (Building building : buildings) {
                Coordinate placement = building.getPlacement();
                if (placement.getX() < minCityX || placement.getX() > maxCityX
                        || placement.getY() < minCityY || placement.getY() > maxCityY) {
                    fail("Building was placed outside the city");
                }
            }
        }

        //Ensure the correct amount of buildings are placed
        List<BuildingSpecification> bSpecs = bg.getBuildings();
        int numBuildingsToPlace = 0;
        for (BuildingSpecification bs : bSpecs) {
            numBuildingsToPlace +=  bs.getNum();
        }

        assertEquals(numBuildingsToPlace, totalBuildingsPlaced);
    }

    @Test
    public void checkReliableBuildingPlacement() {
        MapGenerator mg = AtlantisTerrainFactory.makeMapGenerator();
        try {
            for (int i = 0; i < 50; i++) {
                BuildingGenerator bg = new BuildingGenerator(mg);
            }
        } catch (Exception e) {
            fail("Over a large number of generation tests, the buildings could not be consistently placed within the city");
        }
    }
}
