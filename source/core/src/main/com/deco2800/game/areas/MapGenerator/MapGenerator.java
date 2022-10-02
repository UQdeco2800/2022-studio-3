package com.deco2800.game.areas.MapGenerator;

import com.deco2800.game.areas.MapGenerator.Buildings.BuildingGenerator;
import com.deco2800.game.areas.MapGenerator.pathBuilding.PathGenerator;
import com.deco2800.game.entities.factories.BuildingFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * MapGenerator builds a procedurally generated map of desired mapWidth, mapHeight, including
 * an island of islandSize width containing a city of citySize square length. Access map through
 * getMap() function.
 */
public class MapGenerator {
    /**
     * The width of the game map in which to generate an island (in tiles)
     */
    private final int mapWidth;
    /**
     * The height of the game map in which to generate an island (in tiles)
     */
    private final int mapHeight;
    /**
     * The side width of the city (in tiles)
     */
    private final int cityWidth;

    /**
     * The side height of the city (in tiles)
     */
    private final int cityHeight;

    /**
     * The total distance of the island from side to side horizontally (in tiles)
     */
    private final int islandSize;
    /**
     * The array holding chars representing each tile of the map
     */
    private char[][] map;
    /**
     * Map holding relevant details to the extremities of the city
     */
    private Map<String, Coordinate> cityDetails;

    /**
     * Tile buffer that must exist between the city and the edge of the map in order to fit the
     * island. Must be > islandBuffer
     */
    private final int landBuffer = 3;

    /**
     * Tile buffer that must exist between the island and the edge of the map
     */
    private final int islandBuffer = 1;

    /**
     * Char denoting an ocean tile on the map - for purposes of output
     */
    private final char oceanChar = '*';
    /**
     * Char denoting an island tile on the map - for purposes of output
     */
    private final char islandChar = 'I';

    /**
     * Char denoting a city tile on the map = for purposes of output
     */
    private final char cityChar = 'c';

    /**
     * List of resourceSpecification objects that contain the placements of each resource
     */
    private List<ResourceSpecification> resourcePlacements;

    /**
     * Map containing only the vertices around the island - used for testing purposes
     */
    private char[][] outlineMap;

    /**
     * Map containing the two edges of the island - used by ResourceGenerator
     * @return list containing the two edge points of the island (x co-ords wise)
     */
    private Map<String, Coordinate> islandEdges;


    /**
     * Initiates a new instance of MapGenerator, with a map width, height, citySize and islandSize
     * @param mapWidth width of the map being generated in tiles
     * @param mapHeight height of the map being generated in tiles
     * @param cityWidth  width of city size in tiles
     * @param cityHeight height of city size in tiles
     * @param islandSize total length of the island in tiles
     */
    public MapGenerator(int mapWidth, int mapHeight, int cityWidth, int cityHeight, int islandSize)
            throws IllegalArgumentException {
        if (islandSize < 2 || mapWidth <= 0 || mapHeight <= 0 || cityWidth < 1 || cityHeight < 1) {
            throw new IllegalArgumentException("Invalid construction parameters for MapGenerator");
        }

        //Set variables
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.cityWidth = setValidCityDimension(cityWidth);
        this.cityHeight = setValidCityDimension(cityHeight);
        this.islandSize = islandSize;
        this.map = new char[mapHeight][mapWidth];
        this.cityDetails = new HashMap<>();
        this.islandEdges = new HashMap<>();
        //Generate map
        generateMap();

        //Add resources
        ResourceGenerator rg = new ResourceGenerator(this);
        resourcePlacements = rg.getResources();
    }

    /**
     * Adds the ocean and island tiles to the MapGenerator's map
     */
    public void generateMap() {
        //Fill with ocean cells as they are the default cell
        for (int i = 0; i < mapHeight; i++) {
            for (int j = 0; j < mapWidth; j++) {
                this.map[i][j] = oceanChar;
            }
        }
        //Define and add city location to the map
        placeCity();
        //Add island to map
        makeIsland();
    }

    /**
     * Returns the valid dimensions of the city to fit an aligned wall around the outsides
     * @param dimension width or height of the city in tiles
     * @return the minimum closest dimension necessary to fit a wall around the outskirts of the city
     */
    public int setValidCityDimension(int dimension) {
        //Minimum side length of a city
        float minDimension =  (2 * BuildingFactory.CORNER_SCALE) + 2 * (BuildingFactory.CONNECTOR_SCALE) + BuildingFactory.GATE_SCALE;
        if (dimension <= minDimension) {
            return (int) minDimension;
        }
        //Remaining distance for connectors
        int remainingTiles = (int) (dimension - minDimension);
        //Distance occupied by one wall pillar and one connecting wall
        int wallConnectorDistance =  (int) (BuildingFactory.CORNER_SCALE + BuildingFactory.CONNECTOR_SCALE);
        int tileDifference = remainingTiles % (2 * wallConnectorDistance);
        if (tileDifference == 0) {
            //Perfect size
            return dimension;
        } else {
            //Add on the necessary difference to complete wall around city
            return dimension + (2 * wallConnectorDistance - tileDifference);
        }
    }

    /**
     * Returns a copy of this MapGenerator's map array
     * @return map containing characters denoting island/city/ocean tiles
     */
    public char[][] getMap() {
        return copyMap(map, mapWidth, mapHeight);
    }

    /**
     * Returns a copy of this MapGenerator's outline map
     * @return map containing all the edges of the island
     */
    public char[][] getOutlineMap() {
        return copyMap(outlineMap, mapWidth, mapHeight);
    }

    /**
     * Returns the current map width
     * @return map width
     */
    public int getWidth() {
        return this.mapWidth;
    }

    /**
     * Returns the current map height
     * @return map height
     */
    public int getHeight() {
        return this.mapHeight;
    }

    /**
     * Returns the island size associated with the current map
     * @return the island size within the map
     */
    public int getIslandSize() {
        return this.islandSize;
    }

    /**
     * Returns a map containing the coordinates of each island edge
     * @return the coordinates of the island edges of the map
     */
    public Map<String, Coordinate> getIslandEdges() {
        return this.islandEdges;
    }

    /**
     * Returns the current char representing an ocean tile
     * @return ocean char
     */
    public char getOceanChar() {
        return this.oceanChar;
    }

    /**
     * Returns the current char representing an island tile
     * @return island char
     */
    public char getIslandChar() {
        return this.islandChar;
    }

    /**
     * Returns the current char representing a city tile
     * @return city char
     */
    public char getCityChar() {
        return this.cityChar;
    }

    /**
     * Returns details about the city's extremities
     * @return Map holding the vertices and centre of the city
     */
    public Map<String, Coordinate> getCityDetails() {
        return this.cityDetails;
    }

    /**
     * Returns the list of resourceSpecification, holding the placements of each placed resource
     * @return list of resourceSpecification
     */
    public List<ResourceSpecification> getResourcePlacements() {
        return this.resourcePlacements;
    }

    /**
     * Places a city of the desired size within the bounds of the map at a random location
     * @throws IllegalArgumentException if the city cannot be placed at a valid location
     */
    private void placeCity() throws IllegalArgumentException {
        //Check to ensure valid input parameters
        if (mapWidth < cityWidth + 2 * landBuffer || mapHeight < cityHeight + 2 * landBuffer) {
            //invalid position - throw exception
            throw new IllegalArgumentException("City is too big for map");
        }

        Coordinate cityPlacement;
        Random rand = new Random();
        do {
            //Pick random coordinates for city until it is a passable location
            cityPlacement = new Coordinate(rand.nextInt(mapWidth), rand.nextInt(mapHeight));
        } while (!(isValidCityPlacement(cityPlacement)));

        //Define short hands for city (x,y)
        int x = cityPlacement.getX();
        int y = cityPlacement.getY();

        //Define bounds for city - citySize %2 term added for odd sized cities
        int minX = x - cityWidth / 2;
        int maxX = x + cityWidth % 2 + cityWidth / 2 - 1;
        int minY = y - cityHeight / 2;
        int maxY = y + cityHeight % 2 + cityHeight / 2 - 1;

        //Add city positions to map
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                map[j][i] = cityChar;
            }
        }

        //Store relevant details about the city
        cityDetails.put("Centre", cityPlacement);
        cityDetails.put("NW", new Coordinate(minX, minY));
        cityDetails.put("NE", new Coordinate(maxX, minY));
        cityDetails.put("SW", new Coordinate(minX, maxY));
        cityDetails.put("SE", new Coordinate(maxX, maxY));
    }

    /**
     * Determines if a set of (x,y) coordinates correlating to the map array is a valid place
     * for the centre of Atlantis city.
     * A centre tile is invalid if half the city size + a buffer of 3 tiles cannot fit within
     * the map bounds
     * @param placement coordinate denoting the city position to be tested for validity
     * @return true if it is valid, false otherwise
     */
    private boolean isValidCityPlacement(Coordinate placement) {
        int x = placement.getX();
        int y = placement.getY();
        int sideLengthHorizontal = (cityWidth / 2) + landBuffer;
        int sideLengthVertical = (cityHeight / 2) + landBuffer;

        return x - sideLengthHorizontal >= 0 && x + sideLengthHorizontal < mapWidth
                && y - sideLengthVertical >= 0
                && y + sideLengthVertical < mapHeight;
    }

    /**
     * Writes the contents of the map to the text file specified
     * @param path the path of the text file to be written to
     */
    public void writeMap(String path) {
        File outFile = new File(path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (int i = 0; i < mapHeight; i++) {
                for (int j = 0; j < mapWidth; j++) {
                    bw.write(map[i][j]);
                }
                bw.write("| " + i); //Write array numbers of rows for debugging
                bw.write('\n');
            }
            //Write array numbers of columns at bottom for debugging
            for (int i = 0; i < mapWidth; i++) {
                if (i % 10 == 0 || i == mapWidth - 1) {
                    bw.write(String.valueOf(i));
                    //Write empty spaces between characters, unless it had multiple digits...
                } else if (!(i % 10 == 1 && i >= 10) && !(i % 10 == 2 && i >= 100)) {
                    bw.write(' ');
                }
            }
            bw.close();
        } catch (IOException squashed) {
            //Squashed
        }
    }

    /**
     * Writes the contents of the map to the text file specified
     * @param map 2d char array to write
     * @param path the path of the text file to be written to
     */
    public static void writeMap(char[][] map, String path, int mapWidth, int mapHeight) {
        File outFile = new File(path);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(outFile));
            for (int i = 0; i < mapHeight; i++) {
                for (int j = 0; j < mapWidth; j++) {
                    bw.write(map[i][j]);
                }
                bw.write("| " + i); //Write array numbers of rows for debugging
                bw.write('\n');
            }
            //Write array numbers of columns at bottom for debugging
            for (int i = 0; i < mapWidth; i++) {
                if (i % 10 == 0 || i == mapWidth - 1) {
                    bw.write(String.valueOf(i));
                    //Write empty spaces between characters, unless it had multiple digits...
                } else if (!(i % 10 == 1 && i >= 10) && !(i % 10 == 2 && i >= 100)) {
                    bw.write(' ');
                }
            }
            bw.close();
        } catch (IOException squashed) {
            //Squashed
        }
    }

    /**
     * Adds the island tiles to the map array through procedural generation
     */
    private void makeIsland() {
        //Define and store edges of island
        defineIslandEdges();
        Coordinate leftEdge = islandEdges.get("Left");
        Coordinate rightEdge = islandEdges.get("Right");
        addPoint(leftEdge);
        addPoint(rightEdge);

        //Iterate from right side of map to left side of map, placing vertices of the island above
        //and beside the city
        Coordinate reference = rightEdge;
        for (int i = rightEdge.getX(); i >= leftEdge.getX(); i--) {
            List<Coordinate> legalMoves = legalMoves(reference, i, true);
            Coordinate chosenMove = choosePoint(legalMoves);
            addPoint(chosenMove);
            reference = chosenMove;
        }
        //Set the reference for the next iteration to be the left edge of the map
        reference = leftEdge;
        //Iterate left to right side of map, placing vertices of the island underneath and beside
        //the city
        for (int i = leftEdge.getX(); i <= rightEdge.getX(); i++) {
            List<Coordinate> legalMoves = legalMoves(reference, i, false);
            Coordinate chosenMove = choosePoint(legalMoves);
            addPoint(chosenMove);
            reference = chosenMove;
        }
        //Store a copy of the outline map
        this.outlineMap = copyMap(map, mapWidth, mapHeight);
        //Fill between vertices of map to complete island
        fillMap(rightEdge, leftEdge);
        //Fill all edges around city to be island tiles
        fillAroundCity();
    }

    /**
     * Adds a coordinate to the map as an island tile
     * @param position the position of the island tile to be added
     */
    private void addPoint(Coordinate position) {
        map[position.getY()][position.getX()] = islandChar;
    }

    /**
     * Adds tiles around the city to allow the entire map to be accessible
     */
    private void fillAroundCity() {
        //Find city height in tiles
        int cityHeight = cityDetails.get("SE").getY() - cityDetails.get("NE").getY() + 1;
        //Find city width in tiles
        int cityWidth = cityDetails.get("NE").getX() - cityDetails.get("NW").getX() + 1;
        //Find min and max X values of city
        int cityMinX = cityDetails.get("NW").getX();
        int cityMinY = cityDetails.get("NW").getY();
        int cityMaxX = cityDetails.get("SE").getX();
        int cityMaxY = cityDetails.get("SE").getY();

        //Fill left and right sides of city
        for (int i = cityMinY - 1; i <= cityMaxY + 1; i++) {
            if (map[i][cityMinX - 1] == oceanChar) {
                map[i][cityMinX - 1] = islandChar;
            }
            if (map[i][cityMaxX + 1] == oceanChar) {
                map[i][cityMaxX + 1] = islandChar;
            }
        }
        //Fill top and bottom sides of city
        for (int j = cityMinX - 1; j <= cityMaxX + 1; j++) {
            if (map[cityMinY - 1][j] == oceanChar) {
                map[cityMinY - 1][j]= islandChar;
            }
            if (map[cityMaxY + 1][j] == oceanChar) {
                map[cityMaxY + 1][j]= islandChar;
            }
        }
    }

    /**
     * Fills in the vertices of an outlined map. Iterates between map edges, and for each
     * "column" in the array: fills between vertices of the map -
     */
    private void fillMap(Coordinate rightEdge, Coordinate leftEdge) {
        //Fill in between points - for whole island function
        for (int i = rightEdge.getX(); i >= leftEdge.getX(); i--) {
            boolean filling = false;
            for (int j = mapHeight - 1; j >= 0; j--) {
                //If the coordinate at this point has a tile at this point, invert filling
                Coordinate currentTile = new Coordinate(i, j);
                if (map[j][i] == islandChar
                        && (!(currentTile.equals(leftEdge)))
                        && (!(currentTile.equals(rightEdge)))) {
                    filling = !filling;
                } else if (filling && !(coordInCity(currentTile))) {
                    addPoint(currentTile);
                }
            }
        }
    }

    /**
     * Chooses the two edge tiles for the island by spreading the length evenly across each side
     * of the city, if map space permits
     * @return 2D array of coordinate with entry [0] holding the left edge and [1] holding the right
     */
    private void defineIslandEdges() throws IllegalArgumentException {
        if (mapWidth < islandSize + cityWidth + 2 * islandBuffer) {
            throw new IllegalArgumentException("Map too small for island of size " + islandSize);
        }
        Coordinate centre = cityDetails.get("Centre");

        int leftPivot = centre.getX() - cityWidth / 2 - 1;
        int rightPivot = centre.getX() + cityWidth / 2 + cityWidth % 2;
        //Pick ends of island
        for (int i = 0; i < islandSize; i++) {
            if (i % 2 == 0 && leftPivot - 1 >= islandBuffer) {
                leftPivot--;
            } else if (rightPivot + 1 < mapWidth - islandBuffer) {
                rightPivot++;
            }
        }
        islandEdges.put("Left", new Coordinate(leftPivot, centre.getY()));
        islandEdges.put("Right", new Coordinate(rightPivot, centre.getY()));
    }


    /**
     * Returns whether a given coordinate falls inside the city
     * @param coord coord to be tested
     * @return true if coord in city, false otherwise
     */
    private boolean coordInCity(Coordinate coord) {
        Coordinate nw = cityDetails.get("NW");
        Coordinate ne = cityDetails.get("NE");
        Coordinate se = cityDetails.get("SE");
        int maxX = ne.getX();
        int minX = nw.getX();
        int minY = nw.getY();
        int maxY = se.getY();
        return ((coord.getY() >= minY && coord.getY() <= maxY)
                && (coord.getX() <= maxX && coord.getX() >= minX));
    }


    /**
     * Determines a list of legal moves based on the previous tile in the island
     * @param reference previous tile
     * @param x value of tile to be evaluated
     * @param direction which way we are moving around the city (right>left = true,
     *                  left>right =false)
     * @return list of locations acceptable for next tile
     */
    private List<Coordinate> legalMoves(Coordinate reference, int x, boolean direction) {
        /* Rules
        1. Height must be equal to or below centre coordinate height - 1
        2. Coordinate must not be placed out of bounds of map
        3. IF x between city bounds, height must be above city (overrides 6)
        4. Cannot move through another coordinate
        5. IF there are no available moves go to next tile along (stepback case)
        6. Height must only be +-2 different from last time
        7. Tile cannot be placed one square from the map edge
        */
        Coordinate centre = cityDetails.get("Centre");
        List<Coordinate> moves = new ArrayList<>();
        List<Coordinate> potentialTiles = new ArrayList<>();

        //Add neutral move (y stays same)
        potentialTiles.add(new Coordinate(x, reference.getY()));

        //Iterate through potential moves above and below to see if they are occupied by existing
        //tiles (rules 6, 4 & 2)
        boolean upperBlocked = false;
        boolean lowerBlocked = false;
        for (int i = 1; i <= 2; i++) {
            Coordinate upperCoord = new Coordinate(x, reference.getY() + i);
            if (upperCoord.inBounds(this)
                    && map[upperCoord.getY()][x] == oceanChar && !lowerBlocked) {
                potentialTiles.add(new Coordinate(x, reference.getY() + i));
            } else {
                lowerBlocked = true;
            }
            Coordinate lowerCoord = new Coordinate(x, reference.getY() - i);
            if (lowerCoord.inBounds(this)
                    && map[lowerCoord.getY()][x] == oceanChar && !upperBlocked) {
                potentialTiles.add(new Coordinate(x, reference.getY() - i));
            } else {
                upperBlocked = true;
            }
        }

        //Iterate through any of the (up to) 5 potential tiles to evaluate legality
        for (Coordinate c : potentialTiles) {
            //if it is legal, add it to list of legal moves
            if (c.inBounds(this)
                    && (map[c.getY()][c.getX()] == oceanChar)                                   //Rule 4
                    && !(coordInCity(c))                                                        //Rule 3
                    && !((c.getY() < islandBuffer) || (c.getY() > mapHeight - islandBuffer - 1) //Rule 7
                    || (c.getX() < islandBuffer) || (c.getX() > mapWidth - islandBuffer - 1))   //Rule 7
                    && ((direction && c.getY() < centre.getY() + 1)                             //Rule 1
                    || (!(direction) && c.getY() > centre.getY() - 1))) {                       //Rule 1
                moves.add(c);
            }
        }

        Coordinate ne = cityDetails.get("NE");
        Coordinate sw = cityDetails.get("SW");
        //If there are no moves and we are placing a tile next to a city, place it below or above
        //depending on the direction we are moving around the island
        if (moves.size() == 0
                && ((direction && x == ne.getX())
                || (!(direction) && x == sw.getX()))) {
            moves.add(direction ? new Coordinate(x, ne.getY() - 1)
                    : new Coordinate(x, sw.getY() + 1));
        }
        return moves;
    }

    /**
     * Chooses a point randomly from a list of legal moves based on given weightings
     * @param moves list of legal moves
     * @return a single coordinate which will be added to the island
     */
    private Coordinate choosePoint(List<Coordinate> moves) throws IllegalArgumentException {
        if (moves.size() == 0) {
            throw new IllegalArgumentException("Map creation failed");
        }

        int totalCount = 0;
        TreeMap<Integer, Coordinate> weightMap = new TreeMap<>();
        for (Coordinate c : moves) {
            weightMap.put(totalCount, c);
            totalCount += weightPoint(c);
        }
        Random rand = new Random();
        //Return the object correlating to the weight returned - in this case the lowest key
        //closest to the random number rolled correlates to the move chosen
        return weightMap.get(weightMap.floorKey(rand.nextInt(totalCount)));
    }

    /**
     * Assigns a coordinate a weight based on how much space is adjacent to it - higher weight
     * means it has a higher chance of being chosen. Higher weights are assigned to tiles with
     * less occupied adjacent tiles, as they represent more open space. Promoting movement into
     * open space will generate maps of larger area - a desirable trait.
     * @param point Move being assigned a weight
     * @return the weight of the point based on how strong a move it is
     */
    private int weightPoint(Coordinate point) {
        int adjacentTileCount = 0;
        //Iterate through tiles somewhat adjacent to this potential move
        for (int i = point.getX() - 2; i <= point.getX() + 2; i++) {
            for (int j = point.getY() - 2; j <= point.getY() + 2; j++) {
                //If the character is not an ocean tile or the point being evaluated,
                //it is considered occupied
                Coordinate adjacent = new Coordinate(i, j);
                if (adjacent.inBounds(this) && !(point.equals(adjacent))
                        && map[j][i] != oceanChar) {
                    adjacentTileCount++;
                }
            }
        }
        //20 tiles at most may be adjacent within these conditions -> weighting of 1
        return 21 - adjacentTileCount;
    }

    /**
     * Copies the 2D map array into a new char[][] array
     * @param map the map to be copied
     * @return a new reference to an identical map array of char
     */
    public static char[][] copyMap (char[][] map, int mapWidth, int mapHeight) {
        char [][] newMap = new char[mapHeight][mapWidth];
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                newMap[j][i] = map[j][i];
            }
        }
        return newMap;
    }
}