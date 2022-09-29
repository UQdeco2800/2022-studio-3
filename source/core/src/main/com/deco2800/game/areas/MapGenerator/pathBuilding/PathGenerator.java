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
    // TODO: change array use to GridPoint2
    private static List<int[]> bufferPositions;

    // Debugging functionality set here

    /* Contains handy debug information to be written to a textfile */
    private static String debugInfo;
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
         * leave space of 3 tiles around buildings
         * assume door is to bottom of buildings
         * assume building widths always odd
         * city[row][col]
         */
        List<int[]> toGenerateFrom = new ArrayList<>();

        for (int row = 0; row < this.city.length; row++) {
            for (int col = 0; col < this.city[row].length; col++) {

                BuildingSpecification bs = symbolToBuilding.get((new String(this.city[row])).substring(col, col + 1));
                // if this is not a building tile or it this building has already been reached, skip
                if (bs == null || this.city[row][col - 1] == this.city[row][col] || this.city[row - 1][col] == this.city[row][col]) continue;

                int doorTileRow = row + bs.getHeight();
                int doorTileCol = col + (bs.getWidth() / 2);

                for (int i = 0; i < this.buildingBuffer; i++) {

                    if (this.isValid(new int[]{doorTileRow + i, doorTileCol})) {

                        this.city[doorTileRow + i][doorTileCol] = this.pathTile;
                    }
                }

                if (this.isValid(new int[]{doorTileRow + this.buildingBuffer, doorTileCol})) {

                    this.city[doorTileRow + this.buildingBuffer][doorTileCol] = this.pathTile;
                    toGenerateFrom.add(new int[]{doorTileRow + this.buildingBuffer, doorTileCol});
                }

                for (int i = -1 * this.buildingBuffer; i < (bs.getHeight() + this.buildingBuffer); i++) {
                    for (int j = -1 * this.buildingBuffer; j < (bs.getWidth() + this.buildingBuffer); j++) {

                        int[] p = {row + i, col + j};
                        if (isValid(p)) bufferPositions.add(p);
                    }
                }
            }
        }

        debugInfo += "Number of paths: " + toGenerateFrom.size() + "\n";
        // use pathfinding to construct rest of paths
        for (int[] point : toGenerateFrom) {
            debugInfo += "\nPATH from " + point[0] + "," + point[1] + "\n";
            List<int[]> path = getPath(point);
            debugInfo += "Length: " + path.size() + "\n";
            debugInfo += path.toString() + "\n";
            for (int[] p : path) {
                this.city[p[0]][p[1]] = this.pathTile;
            }
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
        this(buildingGenerator, 3);
    }

    /**
     * Returns whether the coordinates are within the city.
     * 
     * @param child the coordinate to check
     * @return true if inbounds else false
     */
    private boolean isValid(int[] child) {
        return child[0] >= 0 && child[0] < this.city.length && child[1] >= 0 && child[1] < this.city[child[0]].length;
    }

    /**
     * Writes the debugger output to a textfile.
     */
    private void debugWrite() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(new File(this.debugFilePath + "debugInfo.txt")))) {
            writer.write(debugInfo);
            writer.close();
        } catch (IOException e) {}
    }

    /**
     * Finds a path between two points using BFS search.
     * 
     * @param start the starting position
     * @return a list of positions indicating the shortest path
     */
    private List<int[]> getPath(int[] start) {

        // BFS search.
        List<Node> fringe = new ArrayList<>();
        List<Node> visited = new ArrayList<>();

        fringe.add(new Node(start, null, this.city));

        while (fringe.size() > 0) {

            Node node = fringe.get(0);
            fringe.remove(0);
            if (this.isGoal(node.position) && !(node.position[0] == start[0] && node.position[1] == start[1])) {
                debugInfo += "Goal: " + node.position + "\n";
                return node.backtrack();
            }
            
            List<Node> children = node.getChildren();
            for (Node child : children) {
                if (!visited.contains(child)) {
                    // System.out.println("\nChild: " + child.position + "\n");
                    fringe.add(child);
                    visited.add(child);
                }
            }
        }

        // no solution
        debugInfo += "No solution\n";
        return new ArrayList<>();
    }

    private boolean isGoal(int[] position) {
        return this.city[position[0]][position[1]] == this.pathTile;
    }

    /**
     * Wrapper class for search tree nodes.
     */
    private class Node {
        public int[] position;
        public Node parent;
        public char[][] city;
        public Node(int[] position, Node parent, char[][] city) {
            this.parent = parent;
            this.position = position;
            this.city = city;
        }
        public List<Node> getChildren() {
            List<Node> children = new ArrayList<>();
            // debugInfo += "Children\n";
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    int[] child = {this.position[0] + i, this.position[1] + j};
                    if (!(i == 0 && j == 0) && (i == 0 || j == 0) && this.isValid(child)) {
                        // debugInfo += "Child: " + child[0] + ", " + child[1] + "\n";
                        children.add(new Node(child, this, this.city));
                    }
                }
            }
            return children;
        }
        private boolean isValid(int[] child) {
            return child[0] >= 0 && child[0] < this.city.length && child[1] >= 0 && child[1] < this.city[child[0]].length && (this.city[child[0]][child[1]] == '*'  || this.city[child[0]][child[1]] == 'P') && !isBufferPosition(child);
        }
        public List<int[]> backtrack() {
            List<int[]> path = new ArrayList<>();
            Node node = this;
            while (node.parent != null) {
                path.add(node.position);
                node = node.parent;
            }
            Collections.reverse(path);
            return path;
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof PathGenerator.Node) {
                Node n = (Node) o;
                if (n.position[0] == this.position[0] && n.position[1] == this.position[1]) {
                    return true;
                }
            }

            return false;
        }
    }

    private static boolean isBufferPosition(int[] child) {
        for (int[] p : bufferPositions) {
            if (p[0] == child[0] && p[1] == child[1]) return true;
        }

        return false;
    }
}
