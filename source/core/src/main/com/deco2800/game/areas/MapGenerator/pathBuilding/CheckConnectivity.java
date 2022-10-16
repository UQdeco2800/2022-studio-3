package com.deco2800.game.areas.MapGenerator.pathBuilding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.badlogic.gdx.math.GridPoint2;

public class CheckConnectivity {
    private PathGenerator pg;

    /**
     * Constructs an instance of CheckConnectivity. Used for determining if a graph (i.e. path system) 
     * is connected.
     * 
     * @param pg an instance of PathGenerator
     */
    public CheckConnectivity(PathGenerator pg) {
        this.pg = pg;
    }

    /**
     * Determines if a graph of positions is connected with paths and returns all disconnected 
     * points.
     * 
     * @param toGenerateFrom list of vertices
     * @return a list of disconnected vertices
     */
    public List<GridPoint2> check(List<GridPoint2> toGenerateFrom) {

        PathGenerator.debugInfo += "\nCheckConnectivity.java output:\n";

        // BFS search.
        GridPoint2 start = toGenerateFrom.get(0);
        List<Node> fringe = new ArrayList<>();
        List<Node> visited = new ArrayList<>();
        List<GridPoint2> points = new ArrayList<>();
        points.add(start);

        fringe.add(new Node(start, null, this.pg));

        // emergency stopping condition if something breaks
        int stop = this.pg.getCity().length * this.pg.getCity()[0].length;
        int i = 0;

        while (fringe.size() > 0 || i == stop) {
            i++;

            Node node = fringe.get(0);
            fringe.remove(0);
            if (points.equals(toGenerateFrom)) {
                PathGenerator.debugInfo += "Connected!\n";
                return points;
            }
            
            List<Node> children = node.getChildren();
            for (Node child : children) {
                if (!visited.contains(child)) {
                    fringe.add(child);
                    visited.add(child);
                }
                if (toGenerateFrom.contains(child.position)) {
                    points.add(child.position);
                }
            }
        }

        // no solution
        PathGenerator.debugInfo += "Not connected...\n";
        return points;
    }

    /**
     * Wrapper class for search tree nodes.
     */
    private class Node {
        public GridPoint2 position;
        public Node parent;
        public PathGenerator pg;
        public Node(GridPoint2 position, Node parent, PathGenerator pg) {
            this.parent = parent;
            this.position = position;
            this.pg = pg;
        }
        public List<Node> getChildren() {
            List<Node> children = new ArrayList<>();
            // debugInfo += "Children\n";
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    GridPoint2 child = new GridPoint2(this.position.x + i, this.position.y + j);
                    if (!(i == 0 && j == 0) && (i == 0 || j == 0) && this.isValid(child)) {
                        // debugInfo += "Child: " + child[0] + ", " + child[1] + "\n";
                        children.add(new Node(child, this, this.pg));
                    }
                }
            }
            return children;
        }
        private boolean isValid(GridPoint2 child) {
            return  child.x >= 0 && child.x < this.pg.getCity().length && 
            child.y >= 0 && child.y < this.pg.getCity()[child.x].length && 
            this.pg.getCity()[child.x][child.y] == this.pg.getPathTile();
        }
        @Override
        public boolean equals(Object o) {
            if (o instanceof CheckConnectivity.Node) {
                Node n = (Node) o;
                if (this.position.equals(n.position)) {
                    return true;
                }
            }

            return false;
        }

        @Override
        public int hashCode() {
            return position.hashCode();
        }
    }
}
