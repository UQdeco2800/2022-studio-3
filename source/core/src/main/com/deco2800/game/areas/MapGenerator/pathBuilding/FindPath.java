package com.deco2800.game.areas.MapGenerator.pathBuilding;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.badlogic.gdx.math.GridPoint2;

public class FindPath {
    private PathGenerator pg;

    /**
     * Constructs an instance of FindPath. Used for finding a path between two tiles.
     * 
     * @param pg an instance of PathGenerator
     */
    public FindPath(PathGenerator pg) {
        this.pg = pg;
    }

    /**
     * Finds a path between two points using BFS search.
     * 
     * @param start the starting position
     * @param finish the end position
     * @return a list of positions indicating the shortest path
     */
    public List<GridPoint2> findPathBetween(GridPoint2 start, GridPoint2 finish) {

        // BFS search.
        List<Node> fringe = new ArrayList<>();
        List<Node> visited = new ArrayList<>();

        fringe.add(new Node(start, null, this.pg));

        while (fringe.size() > 0) {

            Node node = fringe.get(0);
            fringe.remove(0);
            if (node.position.equals(finish)) {
                PathGenerator.debugInfo += "Goal: " + node.position + "\n";
                return node.backtrack();
            }
            
            List<Node> children = node.getChildren();
            for (Node child : children) {
                if (!visited.contains(child)) {
                    fringe.add(child);
                    visited.add(child);
                }
            }
        }

        // no solution
        PathGenerator.debugInfo += "No solution\n";
        return new ArrayList<>();
    }

    /**
     * Finds a path between two points using BFS search.
     * 
     * @param start the starting position
     * @return a list of positions indicating the shortest path
     */
    public List<GridPoint2> getPath(GridPoint2 start) {

        // BFS search.
        List<Node> fringe = new ArrayList<>();
        List<Node> visited = new ArrayList<>();

        fringe.add(new Node(start, null, this.pg));

        while (fringe.size() > 0) {

            Node node = fringe.get(0);
            fringe.remove(0);
            if (this.pg.getCity()[node.position.x][node.position.y] == this.pg.getPathTile() &&
                !(node.position.equals(start))) {
                PathGenerator.debugInfo += "Goal: " + node.position + "\n";
                return node.backtrack();
            }
            
            List<Node> children = node.getChildren();
            for (Node child : children) {
                if (!visited.contains(child)) {
                    fringe.add(child);
                    visited.add(child);
                }
            }
        }

        // no solution
        PathGenerator.debugInfo += "No solution\n";
        return new ArrayList<>();
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
            // PathGenerator.debugInfo += "Children\n";
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    GridPoint2 child = new GridPoint2(this.position.x + i, this.position.y + j);
                    if (!(i == 0 && j == 0) && (i == 0 || j == 0) && this.isValid(child)) {
                        // PathGenerator.debugInfo += "Child: " + child[0] + ", " + child[1] + "\n";
                        children.add(new Node(child, this, this.pg));
                    }
                }
            }
            return children;
        }
        private boolean isValid(GridPoint2 child) {
            return child.x >= 0 && child.x < this.pg.getCity().length && 
                child.y >= 0 && child.y < this.pg.getCity()[child.x].length && 
                (this.pg.getCity()[child.x][child.y] == '*'  || 
                this.pg.getCity()[child.x][child.y] == 'P') && !this.pg.getBufferPositions().contains(child);
        }
        public List<GridPoint2> backtrack() {
            List<GridPoint2> path = new ArrayList<>();
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
            if (o instanceof FindPath.Node) {
                Node n = (Node) o;
                if (this.position.equals(n.position)) {
                    return true;
                }
            }

            return false;
        }
    }
}
