package com.deco2800.game.areas.MapGenerator;

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
     * The square side length of the city (in tiles)
     */
    private final int citySize;
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
     * island
     */
    private final int landBuffer = 3;
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
     * Initiates a new instance of MapGenerator, with a map width, height, citySize and islandSize
     * @param mapWidth width of the map being generated in tiles
     * @param mapHeight height of the map being generated in tiles
     * @param citySize square side length of city size in tiles
     * @param islandSize total length of the island in tiles
     * @requires islandSize < (mapWidth - citySize), islandSize < 2, mapWidth > 0, mapHeight > 0,
     * citySize >= 1
     * @ensures there is enough room to fit the island and the city
     */
    public MapGenerator(int mapWidth, int mapHeight, int citySize, int islandSize)
            throws IllegalArgumentException {
        if (islandSize < 2 || mapWidth <= 0 || mapHeight <= 0 || citySize < 1) {
            throw new IllegalArgumentException("Invalid construction parameters for MapGenerator");
        }

        //Set variables
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        this.citySize = citySize;
        this.islandSize = islandSize;
        this.map = new char[mapHeight][mapWidth];
        this.cityDetails = new HashMap<>();
        //Generate map
        generateMap();
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
     * Returns a copy of this MapGenerator's map array
     * @return map containing characters denoting island/city/ocean tiles
     */
    public char[][] getMap() {
        return Arrays.copyOf(map, this.mapWidth * this.mapHeight);
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
     * Places a city of the desired size within the bounds of the map at a random location
     * @throws IllegalArgumentException if the city cannot be placed at a valid location
     */
    private void placeCity() throws IllegalArgumentException {
        //Check to ensure valid input parameters
        if (mapWidth < citySize + 2 * landBuffer || mapHeight < citySize + 2 * landBuffer) {
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
        int minX = x - citySize / 2;
        int maxX = x + citySize % 2 + citySize / 2 - 1;
        int minY = y - citySize / 2;
        int maxY = y + citySize % 2 + citySize / 2 - 1;

        //Add city positions to map
        for (int i = minX; i <= maxX; i++) {
            for (int j = minY; j <= maxY; j++) {
                map[j][i] = 'c';
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
        int sideLength = (citySize / 2) + landBuffer;

        return x - sideLength >= 0 && x + sideLength < mapWidth && y - sideLength >= 0
                && y + sideLength < mapHeight;
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
     * Adds the island tiles to the map array through procedural generation
     */
    private void makeIsland() {
        //Define and store edges of island
        Coordinate[] edges = defineIslandEdges();
        Coordinate leftEdge = edges[0];
        Coordinate rightEdge = edges[1];
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

        //Fill between vertices of map to complete island
        fillMap(rightEdge, leftEdge);
    }

    /**
     * Adds a coordinate to the map as an island tile
     * @param position the position of the island tile to be added
     * @requires position to be within the map bounds
     * @ensures the coordinate can be added without crashing
     */
    private void addPoint(Coordinate position) {
        map[position.getY()][position.getX()] = islandChar;
    }

    /**
     * Fills in the vertices of an outlined map. Iterates between map edges, and for each
     * "column" in the array: fills between vertices of the map -
     */
    private void fillMap(Coordinate rightEdge, Coordinate leftEdge) {
        //Fill in between points - for whole island function
        for (int i = rightEdge.getX(); i >= leftEdge.getX(); i--) {
            boolean filling = false;
            //System.out.println("Filling at x = " +i);
            for (int j = mapHeight - 1; j >= 0; j--) {
                //If the coordinate at this point has a tile at this point, invert filling
                Coordinate currentTile = new Coordinate(i, j);
                //System.out.println("Examining: "+ currentTile.toString());
                if (map[j][i] == islandChar
                        && (!(currentTile.equals(leftEdge)))
                        && (!(currentTile.equals(rightEdge)))) {
                    //System.out.println("Fill switched at: " + currentTile);
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
    private Coordinate[] defineIslandEdges() throws IllegalArgumentException {
        if (mapWidth < islandSize + citySize) {
            throw new IllegalArgumentException("Map too small for island of size " + islandSize);
        }
        Coordinate[] edges = new Coordinate[2];
        Coordinate centre = cityDetails.get("Centre");

        int leftPivot = centre.getX() - citySize / 2 - 1;
        int rightPivot = centre.getX() + citySize / 2 + citySize % 2;
        //Pick ends of island
        for (int i = 0; i < islandSize; i++) {
            if (i % 2 == 0 && leftPivot - 1 >= 0) {
                leftPivot--;
            } else if (rightPivot + 1 < mapWidth) {
                rightPivot++;
            }
        }
        edges[0] = new Coordinate(leftPivot, centre.getY());
        edges[1] = new Coordinate(rightPivot, centre.getY());
        return edges;
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
        2. Height must not be < 0
        3. IF x between city bounds, height must be above city (overrides 6)
        4. Cannot move through another coordinate
        5. IF there are no available moves go to next tile along (stepback case)
        6. Height must only be +-2 different from last time
        */
        Coordinate centre = cityDetails.get("Centre");
        List<Coordinate> moves = new ArrayList<>();
        List<Coordinate> potentialTiles = new ArrayList<>();

        //Add neutral move (y stays same)
        potentialTiles.add(new Coordinate(x, reference.getY()));

        //Iterate through potential moves above and below to see if they are occupied by existing
        //tiles
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
                    && (map[c.getY()][c.getX()] == oceanChar)
                    && !(coordInCity(c))
                    && ((direction && c.getY() < centre.getY() + 1)
                    || (!(direction) && c.getY() > centre.getY() - 1))) {
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
    private Coordinate choosePoint(List<Coordinate> moves) {
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
        return 21 - adjacentTileCount;
    }
}

