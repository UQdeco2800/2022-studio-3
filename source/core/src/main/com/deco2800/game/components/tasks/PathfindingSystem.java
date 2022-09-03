package com.deco2800.game.components.tasks;

import com.badlogic.gdx.ai.pfa.Connections;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.GraphPath;
import com.badlogic.gdx.ai.pfa.Heuristic;
import com.badlogic.gdx.ai.pfa.indexed.IndexAStartPathFinder;
import com.badlogic.gdx.ai.pfa.indexed.IndexedGraph;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas;

//Implement the pathfinding system to the building
public class PathfindingSystem {

    //private static Graph graph;
    private static Map map;
    private static DistanceHeuristic dh;

    public PathfindingSystem() {
        dh = new DistanceHeuristics();
    }

    public void generateGraph() {
        Array<Node> nodes = new Array<~>();
        int count = 0;

        for (int row = GameArea.MAP_SIZE; row >= 0; row--){
            for (int col = GameArea.MAP_SIZE; col >= 0; col--){
                float x = (row - col) * GameArea.TILE_WIDTH / 2.0001f;
                float y = (col -row) * GameArea.TILE_HEIGHT / 2f;

                nodes.add(new Node(new Vector2(x, y), new Vector2(row, col), count));
                count++;
            }
        }
    }

    //Make node connections
    for(Node node:nodes) {
        for (int i = 0; i < nodes.size; i++){
            if(node.tilePos.x + 1 == nodes.get(i).tilePos.x && nodes.tilePos.y == nodes.get(i).tilePos.y)
                || (node.tilePos.x - 1 == nodes.get(i).tilePos.x && node.tilePos.y == nodes.get(i).tilePos.y)
                || (node.tilePos.x  == nodes.get(i).tilePos.x && node.tilePos.y + 1 == nodes.get(i).tilePos.y)
                || (node.tilePos.x  == nodes.get(i).tilePos.x && node.tilePos.y - 1 == nodes.get(i).tilePos.y)

                //Enable to move smoother (can go diagonally)
                || (node.tilePos.x + 1 == nodes.get(i).tilePos.x && node.tilePos.y + 1 == nodes.get(i).tilePos.y)
                || (node.tilePos.x - 1 == nodes.get(i).tilePos.x && node.tilePos.y - 1 == nodes.get(i).tilePos.y)
                || (node.tilePos.x + 1 == nodes.get(i).tilePos.x && node.tilePos.y - 1 == nodes.get(i).tilePos.y)
                || (node.tilePos.x - 1 == nodes.get(i).tilePos.x && node.tilePos.y + 1 == nodes.get(i).tilePos.y)

            {
                node.connections.add(new NodeConn(node, nodes.get(i)));

            }
        }

        graph = new Graph(nodes);
    }

    //set the starting point and ending point
    public static GraphPath<Node> getPath(Vector2 entityPos, Vector2 targetPos) {
        Node start = null;
        Node end = null; //will change to building location

        for (Node node:graph.getNodes()){
            if (Math.abc(node.pos.x - entityPos.x) < 8
            && Math.abc(node.pos.y - entityPos.y) < 8){
                start = node;
            }

            if (Math.abc(node.pos.x - targetPos.x) < 8
                    && Math.abc(node.pos.y - targetPos.y) < 8){
                end = node;
            }
        }

        GraphPath<Node> path = new DefaultGraphPath<Node>();
        new IndexedAStartPathFinder(graph).serchNodePath(start, end, dh, path);

        return path;
    }

    public Array<Node> getNodesForDebug() {
        return graph.getNodes();
    }

    public class Graph implements IndexedGraph<Node> {

        private Array<Node> nodes;

        public Graph(Array<Node> nodes){
            this.nodes = nodes;
        }

        @Override
        public int getIndex(Node node) {
            return node.index;
        }

        @Override
        public int getNodeCount() {
            return node.size;
        }

        @Override
        public Array<Connection<Node>> getConnections(Node fromNode) {
            return fromNode.connections;
        }

        public Array<Node> getNodes() {
            return nodes;
        }

    }

    public class DistanceHeuristic implements Heuristic<Node> {
        @Override
        public float estimate(Node node, Node endNode) {
            return node.pos.dst(endNode.pos);

        }
    }

    public class Node {

        public Vector2 pos;
        public Vector2 tilePos;
        public int index;
        public Array<Connection<Node>> connections = new Array<~>();
        public polygon p;

        public Node (Vector2 pos, Vector2 tilePos, int index) {
            this.pos = pos;
            this.tilePos = tilePos;
            this.index = index;
            p = new Polygon(new float[]{
                    pos.x + 4, pos.y,
                    pos.x, pos.y +2,
                    pos.y +4, pos.y,
                    pos.y +8, pos.y +2
            });
        }
    }

    public class NodeConn implements Connection<Node> {

        private Node fromNode;
        private Node toNode;

        public NodeConn(Node fromNode, Node toNode) {
            this.fromNode = fromNode;
            this.toNode = toNode;
        }

        @Override
        public float getCost() {
            return 1;
        }

        @Override
        public Node getFromNode() {
            return fromNode;
        }

        @Override
        public Node getToNode() {
            return toNode;
        }
    }


}