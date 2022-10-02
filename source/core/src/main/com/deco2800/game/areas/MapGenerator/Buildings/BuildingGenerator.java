package com.deco2800.game.areas.MapGenerator.Buildings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.MapGenerator.pathBuilding.PathGenerator;

import java.util.*;

public class BuildingGenerator {
    /**
     * The game's MapGenerator
     */
    private final MapGenerator mg;
    /**
     * The CityDetails as generated by MapGenerator
     */
    private final Map<String, Coordinate> cityDetails;
    /**
     * Distance (in tiles) to have between each building in the city
     */
    private final int CITY_BUFFER = 3;
    /**
     * Distance (in tiles) to have between each row
     */
    private final int ROW_BUFFER = 3;
    /**
     * Distance (in tiles) to allow for the walls around the city
     */
    private final int WALL_BUFFER = 3;

    /**
     * Distance in tiles between the right edge of the city and the last building
     */
    private final int RIGHT_BUFFER = 4;


    /**
     * Attempts to place each building in the city
     */
    private int generationAttempts = 0;
    /**
     * Maximum attempts the algorithm will make to attempt placing the city
     */
    private final int MAX_GENERATION_ATTEMPTS = 5;
    /**
     * Number of rows of buildings in the city
     */
    private final int numRows;
    /**
     * Height of tallest building
     */
    private int maxHeight = 0;
    /**
     * List of each building loaded from buildingSpecifications.json
     */
    private final List<BuildingSpecification> buildings;
    /**
     * List of rows that the buildings will be organised into
     */
    private final List<CityRow> cityRows;
    /**
     * Total number of buildings that have been placed at the corners
     */
    private int cornerBuildings = 0;
    /**
     * The height of the city in tiles.
     */
    private int cityHeight;
    /**
     * The width of the city in tiles.
     */
    private int cityWidth;
    /**
     * Character that symbolises a door position in the char map.
     */
    private char doorSymbol = 'D';
    /**
     * 
     */
    private PathGenerator pg;

    public BuildingGenerator(MapGenerator mg) {
        //Get MapGenerator details
        this.mg = mg;
        this.cityDetails = mg.getCityDetails();
        //Initialise list of buildings
        this.buildings = new ArrayList<>();

        //Find buildings from config file
        //Read in BuildingSpecification data from configs/BuildingSpecifications.json
        /* For each BuildingSpecification object defined under the "buildings" header in
           BuildingSpecifications.json, add a building object to the buildings list.
         */
        JsonReader json = new JsonReader();
        JsonValue buildingsJson = json.parse(Gdx.files.internal("configs/buildingSpecifications.json")).get("buildings");

        for (JsonValue b : buildingsJson.iterator()) {
            //Define object
            BuildingSpecification building = new BuildingSpecification(b.getString("name"),
                    b.getInt("width"),b.getInt("height"),b.getString("door"),b.getInt("num"));

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
        this.cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        //Find city width in tiles
        this.cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;

        //Determine number of rows to place buildings in
        numRows = (cityHeight - (2 * WALL_BUFFER)) / (maxHeight + ROW_BUFFER);

        //Allocate a list of CityRow based on numRows
        cityRows = new LinkedList<>();
        for (int i = 0; i < numRows; i++) {
            cityRows.add(new CityRow(cityWidth, CITY_BUFFER, WALL_BUFFER, RIGHT_BUFFER, i));
        }

        //Place buildings in their rows
        placeBuildings();

        //Add Paths
        this.pg = new PathGenerator(this);

        //Test output
        //writeCity(cityHeight, cityWidth);
    }

    /**
     * Returns the PathGenerator instance.
     * @return PathGenerator instance
     */
    public PathGenerator getPathGenerator() {
        return this.pg;
    }

    /**
     * Gets the city height.
     * 
     * @return city height
     */
    public int getCityHeight() {
        return this.cityHeight;
    }

    /**
     * Gets the city width.
     * 
     * @return city width
     */
    public int getCityWidth() {
        return this.cityWidth;
    }

    /**
     * Returns a copy of this BuildingGenerator's buildingspec list
     * @return List of BuildingSpecifications
     */
    public List<BuildingSpecification> getBuildings() {
        return new ArrayList<>(buildings);
    }

    /**
     * Writes the contents of the city to a human readable text file
     * @param cityHeight height of the city in tiles
     * @param cityWidth width of the city in tiles
     */
    public void writeCity(int cityHeight, int cityWidth) {
        //Test - write to output file
        char[][] city = getCharMap();
        //Change or comment path as needed to test
        MapGenerator.writeMap(city, "E:\\Sprint 3\\city.txt", cityWidth, cityHeight);
    }

    /**
     * Writes the contents of the city to a human readable text file
     * @param cityHeight height of the city in tiles
     * @param cityWidth width of the city in tiles
     * @param path path to output file
     */
    public void writeCity(int cityHeight, int cityWidth, String path) {
        //Test - write to output file
        char[][] city = getCharMap();
        //Change or comment path as needed to test
        MapGenerator.writeMap(city, path, cityWidth, cityHeight);
    }

    /**
     * Returns char char array of the current city contents in the CityRows
     * @return 2d char array denoting the current position of each building
     */
    public char[][] getCharMap() {
        //Find city height in tiles
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        //Find city width in tiles
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;
        //Find min and max X values of city
        int cityMinX = cityDetails.get("NW").getX();
        int cityMinY = cityDetails.get("NW").getY();

        //Initiate city array with empty tiles -> '*' character
        char[][] city = new char[cityHeight][cityWidth];
        for (int i = 0; i < cityHeight; i++) {
            for (int j = 0; j < cityWidth; j++) {
                city[i][j] = '*';
            }
        }
        //For each building stored in each city row, write a Width * Height representation on the map
        for (CityRow cr : cityRows) {
            List<Building> buildings = cr.getBuildings();
            for (Building b : buildings) {
                Coordinate placement = b.getPlacement();
                char buildingChar = b.getName().charAt(0);
                for (int i = 0; i < b.getWidth(); i++) {
                    for (int j = 0; j < b.getHeight(); j++) {
                        int translatedX = placement.getX() - cityMinX + i;
                        int translatedY = placement.getY() - cityMinY + j;
                        city[translatedY][translatedX] = buildingChar;
                    }
                }
                GridPoint2 door = b.getDoor();
                city[placement.getY() - cityMinY + door.y][placement.getX() - cityMinX + door.x] = this.doorSymbol;
            }
        }
        return city;
    }

    /**
     * Returns a copy of this BuildingGenerators CityRow list - used to find building placements
     * @return List of CityRows, each containing buildings with appropriate placements
     */
    public List<CityRow> getCityRows() {
        return new LinkedList<CityRow>(cityRows);
    }

    /**
     * Places each building in a row of the city
     */
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

            //Make a building object to mirror this
            Building building = new Building(currentBuilding.getWidth(), currentBuilding.getHeight(), currentBuilding.getName(), currentBuilding.getDoor());

            //While a row can't be found or until the max rows have been exhausted
            int seed = 0;
            boolean placed = false;
            CityRow currentRow = null;

            while (seed < numRows && !placed) {
                currentRow = chooseRow(seed);
                placed = currentRow.addBuilding(building,currentRow.getBuildings().size() == 1);
                seed++; //Choose the next best spot for the building
            }

            if (!placed) {
                //No placement could be found for this building within any of the city rows - fail
                if (generationAttempts++ >= MAX_GENERATION_ATTEMPTS) {
                    //A number of attempts have failed to fit the buildings in the city - throw exception
                    throw new IllegalArgumentException("Cannot fit buildings within City Size");
                } else {
                    //Attempt to try again
                    resetBuildings();
                    placeBuildings();
                }
                return;
            } else {
                //Place the building
                int index = currentRow.getIndex();

                //If it was a corner building, update the corner placements
                if ((cornerBuildings < 4) && (index == 0 || index == numRows - 1)) {
                    cornerBuildings++;
                }

                //If no buildings remain to be placed, remove from list
                //Increase the count of buildings that have been placed
                currentBuilding.incNumPlaced();
                if (currentBuilding.numRemaining() == 0) {
                    remainingBuildings.remove(currentBuilding);
                }
            }

        }
        //Set height of each city row and the buildings within
        //Find minX of city to determine map offsets
        int cityMinX = cityDetails.get("NW").getX();
        int cityMinY = cityDetails.get("NW").getY();
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;


        int spaceRemaining = (cityHeight - (2 * WALL_BUFFER)) - ((maxHeight + ROW_BUFFER) * numRows);
        int baseOffset = spaceRemaining / (numRows - 1);
        int conditionalOffset = spaceRemaining % (numRows - 1);
        int usedOffsets = 0;
        int offset = 0;
        for (CityRow c : cityRows) {
            int index = c.getIndex();
            //Determine centred height of city
            if (numRows > 2 && index != 0) {
                offset += baseOffset;
                if (usedOffsets < conditionalOffset) {
                    offset++;
                    usedOffsets++;
                }
            }
            int rowYPosition = ((maxHeight + ROW_BUFFER) * index) + WALL_BUFFER + cityMinY + offset;

            c.setBuildingCoordinates(cityMinX, rowYPosition);
            c.centreRow();
        }
    }

    public void resetBuildings() {
        //Empty out list of buildings
        for (BuildingSpecification b : buildings) {
            b.resetPlacements();
        }
        //Empty out city rows
        for (CityRow c : cityRows) {
            c.resetRow();
        }
    }

    public CityRow chooseRow(int seed) {
        //Initialise new LinkedList of cityRows
        LinkedList<CityRow> cityRowsCopy = new LinkedList<>(cityRows);

        //Special case - place buildings in row 0 or last row (numRows - 1)
        if (cornerBuildings < 4 && seed < 4) {
            return cornerBuildings < 2 ? cityRowsCopy.get(0) : cityRowsCopy.get(numRows - 1);
        } else {
            //Sort rows by number of placed buildings
            cityRowsCopy.sort((r1, r2) -> (r1.getTotalPlacements() - r2.getTotalPlacements()));
            return cityRowsCopy.get(seed);
        }
    }
}
