package com.deco2800.game.areas.MapGenerator.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.MapGenerator.ResourceSpecification;
import com.deco2800.game.components.building.Building;

import java.util.*;

public class BuildingGenerator {
    private final MapGenerator mg;
    private final Map<String, Coordinate> cityDetails;
    private final char BUILDING_CHAR = 'b';
    private final int CITY_BUFFER = 2;
    private final int WALL_BUFFER = 1;
    private final int numRows;
    /**
     * Height of tallest building
     */
    private int maxHeight = 0;
    private final List<BuildingSpecification> buildings;
    private final CityRow[] cityRows;
    /**
     * Total number of buildings that have been placed at the corners
     */
    private int cornerBuildings = 0;

    public BuildingGenerator(MapGenerator mg) {
        //Get MapGenerator details
        this.mg = mg;
        this.cityDetails = mg.getCityDetails();
        //Initialise list of buildings
        this.buildings = new ArrayList<>();

        //Find buildings from config file
        //Read in BuildingSpecification data from configs/BuildingSpecifications.json
        /* For each BuildingSpecification object defined under the "buildings" header in
           BuildingSpecifications.json, add a building object to the buildingslist.
         */
        JsonReader json = new JsonReader();
        JsonValue buildingsJson = json.parse(Gdx.files.internal("configs/buildingSpecifications.json")).get("buildings");

        for (JsonValue b : buildingsJson.iterator()) {
            //Define object
            BuildingSpecification building = new BuildingSpecification(b.getString("name"),
                    b.getInt("width"),b.getInt("height"),b.getInt("num"));

            //Add object to list
            buildings.add(building);
        }

        //Find max height of buildings
        for (BuildingSpecification b : buildings) {
            if (b.getHeight() > maxHeight) {
                maxHeight = b.getHeight();
            }
        }

        //Find city height in tiles
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY();
        //Find city width in tiles
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getY();

        //Determine number of rows to place buildings in
        numRows = (cityHeight - WALL_BUFFER) / (maxHeight + CITY_BUFFER);

        //Allocate an array of rows based on numRows
        cityRows = new CityRow[numRows];
        for (int i = 0; i < numRows; i++) {
            cityRows[i] = new CityRow(cityWidth, CITY_BUFFER, WALL_BUFFER);
        }

        //Place buildings in their rows
        placeBuildings();

        //Test - write to output file
        char[][] city = new char[cityHeight][cityWidth];
        for (int i = 0; i < cityHeight; i++) {
            for (int j = 0; j < cityWidth; j++) {
                city[i][j] = '*';
            }
        }
        for (BuildingSpecification b : buildings) {
            for (Coordinate p : b.getPlacements()) {
                //Write each coordinate
                
            }
        }
        MapGenerator.writeMap(city, "E:\\Sprint 3\\city.txt", cityWidth, cityHeight);
    }

    /**
     * Returns a copy of this BuildingGenerator's buildingspec list
     * @return List of BuildingSpecifications
     */
    public List<BuildingSpecification> getBuildings() {
        return new ArrayList<>(buildings);
    }
    public void placeBuildings() {
        //Add all buildings remaining to a list of BuildingSpecification to randomly select from
        List<BuildingSpecification> remainingBuildings = new ArrayList<>();
        for (BuildingSpecification b : buildings) {
            if (b.numRemaining() > 0) {
                remainingBuildings.add(b);
            }
        }
        //Place buildings while any remaining buildings that haven't been placed exist
        while (remainingBuildings.size() > 0) {
            //Select a building to place
            BuildingSpecification currentBuilding = remainingBuildings.get(new Random().nextInt(remainingBuildings.size()));
            //While a row can't be found or until the max rows have been exhausted
            int seed = 0;
            int xPlacement = -1;
            CityRow currentRow = null;
            while (seed < numRows && xPlacement < 0) {
                currentRow = chooseRow(seed);
                xPlacement = currentRow.addBuilding(currentBuilding.getWidth(), currentBuilding.getHeight());
                seed++; //Choose the next best spot for the building
            }
            if (xPlacement < 0) {
                //No placement could be found for this building within any of the city rows - fail
            } else {
                //Place the building
                //Find minX of city
                int cityMinX = cityDetails.get("NW").getX();
                //Find index of row
                int index = 0;
                for (int i = 0; i < numRows; i++) {
                    if (cityRows[i].equals(currentRow)) {
                        index = i;
                    }
                }
                currentBuilding.addPlacement(new Coordinate(xPlacement + cityMinX, ((maxHeight + CITY_BUFFER) * index) + WALL_BUFFER));
                //If it was a corner building, update the corner p[lacements
                if (index == 0 || index == numRows - 1) {
                    cornerBuildings++;
                }
                //If no buildings remain, remove from list
                if (currentBuilding.numRemaining() == 0) {
                    remainingBuildings.remove(currentBuilding);
                }
            }

        }
    }

    public CityRow chooseRow(int seed) {
        //Initialise LinkedList of cityRows
        LinkedList<CityRow> rows = new LinkedList<>();
        //First and last elements are first considered
        rows.add(cityRows[0]);
        rows.add(cityRows[numRows - 1]);
        for (int i = 1; i < numRows - 1; i++) {
            rows.add(cityRows[i]);
        }

        //Special case - place buildings in row 0 or numRows - 1
        if (cornerBuildings < 4 && seed < 4) {
            return seed < 2 ? cityRows[0] : cityRows[numRows - 1];
        } else {
            //Sort rows by number of placed buildings
            rows.sort((r1, r2) -> (r1.getTotalPlacements() - r2.getTotalPlacements()));
            return rows.get(seed);
        }
    }
}
