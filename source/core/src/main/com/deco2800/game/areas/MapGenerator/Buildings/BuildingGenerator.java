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
    private final int CITY_BUFFER = 3;
    private final int WALL_BUFFER = 1;
    private final int numRows;
    /**
     * Height of tallest building
     */
    private int maxHeight = 0;
    private final List<BuildingSpecification> buildings;
    private final List<CityRow> cityRows;
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
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        //Find city width in tiles
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;

        //Determine number of rows to place buildings in
        numRows = (cityHeight - WALL_BUFFER) / (maxHeight + CITY_BUFFER);

        //Allocate an array of rows based on numRows
        cityRows = new LinkedList<>();
        for (int i = 0; i < numRows; i++) {
            cityRows.add(new CityRow(cityWidth, CITY_BUFFER, WALL_BUFFER, i));
        }

        //Place buildings in their rows
        placeBuildings();

        writeCity(cityHeight, cityWidth);
    }

    public void writeCity(int cityHeight, int cityWidth) {
        //Test - write to output file
        char[][] city = new char[cityHeight][cityWidth];
        for (int i = 0; i < cityHeight; i++) {
            for (int j = 0; j < cityWidth; j++) {
                city[i][j] = '*';
            }
        }
        int cityMinX = cityDetails.get("NW").getX();
        int cityMinY = cityDetails.get("NW").getY();

        for (BuildingSpecification b : buildings) {
            System.out.println(b.getName()+b.getPlacements());
            for (Coordinate p : b.getPlacements()) {
                char buildingChar = b.getName().charAt(0);
                //Write each coordinate
                for (int i = 0; i < b.getWidth(); i++) {
                    for (int j = 0; j < b.getHeight(); j++) {
                        int translatedX = p.getX() - cityMinX + i;
                        int translatedY = (p.getY() - cityMinY) + j;
                        city[translatedY][translatedX] = buildingChar;
                    }
                }
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
                int cityMinY = cityDetails.get("NW").getY();

                //Store index of the row
                int index = currentRow.getIndex();

                //Add coordinate to place this building
                currentBuilding.addPlacement(new Coordinate(xPlacement + cityMinX, ((maxHeight + CITY_BUFFER) * index) + WALL_BUFFER + cityMinY));
                //If it was a corner building, update the corner placements
                if ((cornerBuildings < 4) && (index == 0 || index == numRows - 1)) {
                    cornerBuildings++;
                }

                //If no buildings remain to be placed, remove from list
                if (currentBuilding.numRemaining() == 0) {
                    remainingBuildings.remove(currentBuilding);
                }
            }

        }
    }

    public CityRow chooseRow(int seed) {
        //Initialise new LinkedList of cityRows
        LinkedList<CityRow> cityRowsCopy = new LinkedList<>(cityRows);

        //Special case - place buildings in row 0 or numRows - 1
        if (cornerBuildings < 4 && seed < 4) {
            return cornerBuildings < 2 ? cityRowsCopy.get(0) : cityRowsCopy.get(numRows - 1);
        } else {
            //Sort rows by number of placed buildings
            cityRowsCopy.sort((r1, r2) -> (r1.getTotalPlacements() - r2.getTotalPlacements()));
            return cityRowsCopy.get(seed);
        }
    }
}
