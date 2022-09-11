package com.deco2800.game.map;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.map.util.IllegalEntityPlacementException;
import com.deco2800.game.map.util.NoEntityException;
import com.deco2800.game.map.util.OccupiedTileException;
import com.deco2800.game.map.util.OutOfBoundsException;

/**
 * Stores all game entity positions.
 */
public class MapService {

	/* Stores all register entities */
	private final List<MapComponent> interactables = new ArrayList<>();
	/* Stores all island tile positions */
	public final List<GridPoint2> islandTiles = new ArrayList<>();
	/* Stores the entity occupying a position */
	private final Map<GridPoint2, MapComponent> positionToEntity = new HashMap<>();
	/* Stores all positions entities occupy */
	private final Map<MapComponent, List<GridPoint2>> entityToPositions = new HashMap<>();
	/* Current tileSize of game */
    private static float tileSize = -1;
    /* Width of game map */
    private int mapWidth;
    /* Height of game map */
    private int mapHeight;

	/**
	 * Returns a map of positions to entities.
	 * 
	 * @return map of positions to entities.
	 */
	public Map<GridPoint2, MapComponent> getEntityOccupiedPositions() {
		return new HashMap<>(positionToEntity);
	}

	/**
	 * Updates the MapService's understanding of an entity's current position.
	 * 
	 * @param comp the entity whose position is to be updated.
	 */
	public void update(MapComponent comp) {
		List<GridPoint2> oldPositions = entityToPositions.get(comp);
		List<GridPoint2> newPositions = getAllOccupiedPositions(comp);
		for (GridPoint2 pos : oldPositions) {
			positionToEntity.remove(pos);
		}
		for (GridPoint2 pos : newPositions) {
			positionToEntity.put(pos, comp);
		}
		entityToPositions.put(comp, newPositions);
	}
	
	/**
	 * Registers an entity with the MapService.
	 * 
	 * @param comp entity to register.
	 */
	public void register(MapComponent comp) {
		interactables.add(comp);
		List<GridPoint2> positions = getAllOccupiedPositions(comp);
		for (GridPoint2 pos : positions) {
			positionToEntity.put(pos, comp);
		}
		entityToPositions.put(comp, positions);
	}

	/**
	 * Retrieves all current positions occupied by an entity.
	 * 
	 * @param comp the entity
	 * @return list of occupied positions.
	 */
	private List<GridPoint2> getAllOccupiedPositions(MapComponent comp) {
		Vector2 vecPos = comp.getEntity().getPosition();
		Vector2 vecScale = comp.getEntity().getScale();
		GridPoint2 bottomRightCorner = worldToTile(vecPos);
		GridPoint2 topLeftCorner = worldToTile(vecPos.x + vecScale.x, vecPos.y + vecScale.y);
		// GridPoint2 topLeftCorner = worldToTile(comp.getEntity().getCenterPosition());

		List<GridPoint2> occupied = new ArrayList<>();
		for (int i = topLeftCorner.x; i <= bottomRightCorner.x; i++) {
			for (int j = bottomRightCorner.y; j <= topLeftCorner.y; j++) {
				GridPoint2 pos = new GridPoint2(i, j);
				occupied.add(pos);
			}
		}
		return occupied;
	}
	
	/**
	 * Remove entity from the MapService.
	 * 
	 * @param comp the entity to remove.
	 */
	public void unregister(MapComponent comp) {
		interactables.remove(comp);
		List<GridPoint2> positions = entityToPositions.get(comp);
		if (positions == null) {
			// this case will never be hit
			return;
		}
		for (GridPoint2 pos : positions) {
			positionToEntity.remove(pos);
		}
		entityToPositions.remove(comp);
	}

	/**
	 * Returns the shortest path to a goal, or throws an exception if the goal is invalid.
	 * 
	 * @param comp the entity to find a path for.
	 * @param goal the goal positions
	 * @return a list of tile positions
	 * @throws OutOfBoundsException if the tile is not a land tile
	 * @throws OccupiedTileException if the goal position is occupied by another entity
	 */
	public List<GridPoint2> findPathForEntity(MapComponent comp, GridPoint2 goal) throws OutOfBoundsException, OccupiedTileException {
		if (isOccupied(goal)) {
			throw new OccupiedTileException();
		}
		if (!islandTiles.contains(goal)) {
			throw new OutOfBoundsException();
		}
		
		return getPath(worldToTile(comp.getEntity().getCenterPosition()), goal);
	}
	
	/**
	 * Finds a path between two points using BFS search.
	 * 
	 * @param start the starting position
	 * @param goal the end position
	 * @return a list of positions indicating the shortest path
	 */
	public List<GridPoint2> getPath(GridPoint2 start, GridPoint2 goal) {

		// BFS search. I don't believe there is a reasonable heuristic we can add here 
		// considering there is little predictability to map generation/layout atm. Since 
		// this a dynamic environment there comes the issue of computational complexity.
		// could potentially frame this as a stochastic environment and/or not flat
		// Another option to reduce space + time complexity without the hassle is 
		// bidirectional IDDFS
		List<Node> fringe = new ArrayList<>();
		List<Node> visited = new ArrayList<>();
		if (goal.equals(start)) {
			return new ArrayList<>();
		}
		fringe.add(new Node(start, null));

		while (fringe.size() > 0) {

			Node node = fringe.get(0);
			fringe.remove(0);
			if (goal.equals(node.position)) {
				return node.backtrack();
			}
			
			List<Node> children = node.getChildren();
			for (Node child : children) {
				if (!visited.contains(child) && !isOccupied(child.position)) {
					fringe.add(child);
					visited.add(child);
				}
			}
		}

		// no solution
		return new ArrayList<>();
	}

	private class Node {
		public GridPoint2 position;
		public Node parent;
		public Node(GridPoint2 position, Node parent) {
			this.parent = parent;
			this.position = position;
		}
		public List<Node> getChildren() {
			List<Node> children = new ArrayList<>();
			for (int i = -1; i <= 1; i++) {
				for (int j = -1; j <= 1; j++) {
					GridPoint2 child = new GridPoint2(this.position.x + i, this.position.y + j);
					if (!(i == 0 && j == 0) && islandTiles.contains(child)) {
						children.add(new Node(child, this));
					}
				}
			}
			return children;
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
			if (o instanceof MapService.Node) {
				Node n = (Node) o;
				if (n.position.equals(this.position)) {
					return true;
				}
			}

			return false;
		}
	}
	
	/**
	 * Registers relevant map details with MapService
	 * 
	 * @param height height of map
	 * @param width width of map
	 * @param tileSize size of a tile
	 */
	public void registerMapDetails(int height, int width, float tileSize) {
		this.mapWidth = width;
		this.mapHeight = height;
		MapService.tileSize = tileSize;
	}
	
	/**
	 * Add an island tile position to the MapService
	 * 
	 * @param x x-coordinate of tile
	 * @param y y-coordinate of tile
	 */
	public void addIslandTile(int x, int y) {
		islandTiles.add(new GridPoint2(x, y));
	}

	/**
	 * Returns true if position is occupied by an entity, else false.
	 * 
	 * @param position to check if occupied
	 * @return true if occupied, else false
	 */
	public boolean isOccupied(GridPoint2 position) {
		return positionToEntity.get(position) != null;
	}
	
	/**
	 * Gets the entity at the given position.
	 * 
	 * @throws NoEntityException if there is no entity
	 */
	public Entity getEntityAt(GridPoint2 position) throws NoEntityException {
		MapComponent comp = positionToEntity.get(position);
		if (comp == null) {
			throw new NoEntityException();
		}
		
		return comp.getEntity();
	}

	/**
	 * Converts a world point to a position on the tile map
	 * 
     * @param position Vector2 position to convert
     * @return position of the world point on the tile map
	 */
	public static GridPoint2 worldToTile(Vector2 position) {
		return MapService.worldToTile(position.x, position.y);
	}
	
	/**
     * Converts a world point to a position on the tile map
     * @param worldX world y position of an entity
     * @param worldY world y position of an entity
     * @return position of the world point on the tile map
     */
    public static GridPoint2 worldToTile(float worldX, float worldY) {
		return MapService.worldToTile(MapService.tileSize, worldX, worldY);
    }

    /**
     * Converts a world point to a position on the tile map
     * @param tileSize tileSize returned by TerrainComponent of map
     * @param worldX world y position of an entity
     * @param worldY world y position of an entity
     * @return position of the world point on the tile map
     */
    public static GridPoint2 worldToTile(float tileSize, float worldX, float worldY) {
        //Define constants to make expression simpler
        float b = tileSize / 2;
        float a = tileSize / 3.724f;

        int tileX = (int) ((worldX / b) - (worldY / a)) / 2;
        int tileY = (int) (worldY / a) + tileX;

        return new GridPoint2(tileX, tileY);
    }

	public static Vector2 tileToWorldPosition(GridPoint2 tilePos) {
		return tileToWorldPosition(tilePos.x, tilePos.y);
	}

	public static Vector2 tileToWorldPosition(int x, int y) {
		return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 3.724f);
	}
}