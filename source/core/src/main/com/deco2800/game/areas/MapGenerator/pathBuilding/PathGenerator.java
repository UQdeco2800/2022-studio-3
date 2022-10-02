package com.deco2800.game.areas.MapGenerator.pathBuilding;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingSpecification;

/**
 * Generates paths between buildings using a blind search algorithm.
 */
public class PathGenerator {
    private int buildingBuffer;
    private char pathTile = 'P';
    private char[][] city;
    private List<GridPoint2> bufferPositions;

    // Debugging functionality set here

    /* Contains handy debug information to be written to a textfile */
    public static String debugInfo;
    /* Put the directory path to where you would like the debug info stored e.g. "/home/dir/" */
    private String debugFilePath = "/../";
    /* Set to true if you would like the debug output */
    private boolean debug = false;

    /**
     * Contructs a PathGenerator and generates paths between buildings.
     * 
     * @param buildingGenerator an instance of BuildingGenerator
     * @param buildingBuffer the minimum number of tiles away a path should always be from a
     * building
     */
    public PathGenerator(BuildingGenerator buildingGenerator, int buildingBuffer) {
        this.buildingBuffer = buildingBuffer;
        this.city = buildingGenerator.getCharMap();
        bufferPositions = new ArrayList<>();
        debugInfo = "PATH GENERATION DEBUG OUTPUT\n\n";
        List<BuildingSpecification> buildings = buildingGenerator.getBuildings();
        Map<String, BuildingSpecification> symbolToBuilding = new HashMap<>();

        for (BuildingSpecification b : buildings) {

            symbolToBuilding.put(b.getName().substring(0, 1), b);
        }

        /*
         * assume building widths always odd
         * city[row][col] indexing
         */
        List<GridPoint2> toGenerateFrom = new ArrayList<>();

        for (int row = 0; row < this.city.length; row++) {
            for (int col = 0; col < this.city[row].length; col++) {

                BuildingSpecification bs = symbolToBuilding.get((new String(this.city[row]))
                    .substring(col, col + 1));
                // if this is not a building tile or it this building has already been reached, skip
                if (bs == null || this.city[row][col - 1] == this.city[row][col] || 
                    this.city[row - 1][col] == this.city[row][col]) {
                    
                    continue;
                }

                int y = bs.getDoor().y;
                int x = bs.getDoor().x;
                int doorTileRow = row + y;
                int doorTileCol = col + x;

                int dx, dy;
                if (y == 0) {
                    dy = -1;
                    dx = 0;
                } else if (y == bs.getHeight() - 1) {
                    dy = 1;
                    dx = 0;
                } else if (x == bs.getWidth() - 1) {
                    dy = 0;
                    dx = 1;
                } else if (x == 0) {
                    dy = 0;
                    dx = -1;
                } else {
                    dy = 0;
                    dx = 0;
                }

                for (int i = 1; i <= this.buildingBuffer + 1; i++) {

                    if (this.isValid(new GridPoint2(doorTileRow + i * dy, doorTileCol + i * dx))) {

                        this.city[doorTileRow + i * dy][doorTileCol + i * dx] = this.pathTile;
                    }
                }

                int xBuffer = (this.buildingBuffer + 1) * dx;
                int yBuffer = (this.buildingBuffer + 1) * dy;

                if (this.isValid(new GridPoint2(doorTileRow + yBuffer, doorTileCol + xBuffer))) {

                    this.city[doorTileRow + yBuffer][doorTileCol + xBuffer] = this.pathTile;
                    toGenerateFrom.add(new GridPoint2(doorTileRow + yBuffer, doorTileCol + xBuffer));
                }

                for (int i = -1 * this.buildingBuffer; i < (bs.getHeight() + this.buildingBuffer); i++) {
                    for (int j = -1 * this.buildingBuffer; j < (bs.getWidth() + this.buildingBuffer); j++) {

                        GridPoint2 p = new GridPoint2(row + i, col + j);
                        if (isValid(p)) bufferPositions.add(p);
                    }
                }
            }
        }

        debugInfo += "Number of paths: " + toGenerateFrom.size() + "\n";
        // need to repeatedly construct paths as well as check that paths are connected
        FindPath fp = new FindPath(this);
        CheckConnectivity cc = new CheckConnectivity(this);
        
        // use pathfinding to construct rest of paths
        for (GridPoint2 point : toGenerateFrom) {
            debugInfo += "\nPATH from " + point.toString() + "\n";
            List<GridPoint2> path = fp.getPath(point);
            debugInfo += "Length: " + path.size() + "\n";
            debugInfo += path.toString() + "\n";
            for (GridPoint2 p : path) {
                this.city[p.x][p.y] = this.pathTile;
            }
        }

        // check if paths form a connected graph
        List<GridPoint2> generated = cc.check(toGenerateFrom);
        List<GridPoint2> remaining = new ArrayList<>(toGenerateFrom);
        remaining.removeAll(generated);
        int i = -1;
        // connect any disconnected components
        while (remaining.size() > 0) {
            List<GridPoint2> path = fp.findPathBetween(remaining.get(0), generated.get(++i % generated.size()));
            for (GridPoint2 p2 : path) {
                this.city[p2.x][p2.y] = this.pathTile;
            }
            generated = cc.check(toGenerateFrom);
            remaining = new ArrayList<>(toGenerateFrom);
            remaining.removeAll(generated);
        }

        MapGenerator.writeMap(this.city, this.debugFilePath + "cityMap.txt", buildingGenerator.getCityWidth(), buildingGenerator.getCityHeight());
        if (debug) this.debugWrite();
    }

    /**
     * Constructs a PathGenerator class with a building buffer of 3
     * 
     * @param buildingGenerator an instance of BuildingGenerator
     */
    public PathGenerator(BuildingGenerator buildingGenerator) {
        this(buildingGenerator, 2);
    }

    /**
     * 
     * @return
     */
    public char[][] getCity() {
        return this.city.clone();
    }

    /**
     * Returns the path tile character.
     * 
     * @return the path tile
     */
    public char getPathTile() {
        return this.pathTile;
    }

    /**
     * Returns all positions the path cannot be.
     * 
     * @return the buffer positions
     */
    public List<GridPoint2> getBufferPositions() {
        return this.bufferPositions;
    }

    /**
     * Returns whether the coordinates are within the city.
     * 
     * @param child the coordinate to check
     * @return true if inbounds else false
     */
    private boolean isValid(GridPoint2 child) {
        return child.x >= 0 && child.x < this.city.length && child.y >= 0 && 
            child.y < this.city[child.x].length;
    }

    /**
     * Writes the debugger output to a textfile.
     */
    private void debugWrite() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new 
                File(this.debugFilePath + "debugInfo.txt")))) {
            writer.write(debugInfo);
            writer.close();
        } catch (IOException e) {}
    }
}
