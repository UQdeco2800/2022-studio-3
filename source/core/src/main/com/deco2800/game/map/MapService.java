package com.deco2800.game.map;

import java.util.ArrayList;
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

public class MapService {
	private final List<MapComponent> interactables = new ArrayList<>();
	private final List<GridPoint2> islandTiles = new ArrayList<>();
	// must update their positions
	private final Map<GridPoint2, MapComponent> entityToPosition = new HashMap<>();
	/**
     * Current tileSize of game
     */
    private static float tileSize = -1;

    /**
     * Width of game map
     */
    private int mapWidth;

    /**
     * Height of game map
     */
    private int mapHeight;

	public Map<GridPoint2, MapComponent> getEntityOccupiedPositions() {
		return new HashMap<>(entityToPosition);
	}
	
	/**
	 * 
	 * @param comp
	 * @throws IllegalEntityPlacementException
	 */
	public void register(MapComponent comp) { // throws IllegalEntityPlacementException {
		interactables.add(comp);
		GridPoint2 position = worldToTile(comp.getEntity().getPosition());

		// // check the tile is not already occupied
		// if (entityToPosition.get(position) != null) {
		// 	throw new OccupiedTileException();
		// }

		// // check the tile is in the bounds of the island
		// if (!islandTiles.contains(position)) {
		// 	throw new OutOfBoundsException();
		// }

		entityToPosition.put(position, comp);
	}
	
	/**
	 * 
	 * @param comp
	 */
	public void unregister(MapComponent comp) {
		interactables.remove(comp);
		entityToPosition.remove(worldToTile(comp.getEntity().getPosition()));
	}
	
	/**
	 * 
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @return
	 */
	public List<GridPoint2> getPath(GridPoint2 start, GridPoint2 goal) {
		List<GridPoint2> path = new ArrayList<>();

		// BFS search
		List<GridPoint2> fringe = new ArrayList<>();
		List<GridPoint2> visited = new ArrayList<>();
		if (goal.equals(start)) {
			return path;
		}
		fringe.add(start);

		while (fringe.size() > 0) {

			GridPoint2 node = fringe.get(0);
			if (goal.equals(node)) {
				path.add(node);
				return path;
			}
			
			List<GridPoint2> children = getChildren(node);
			for (GridPoint2 child : children) {
				if (!visited.contains(child)) {
					fringe.add(child);
					visited.add(child);
				}
			}
		}

		return path;
	}

	/**
	 * Helper function for tree based search.
	 * 
	 * @param node
	 * @return
	 */
	private List<GridPoint2> getChildren(GridPoint2 node) {
		List<GridPoint2> children = new ArrayList<>();
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				GridPoint2 child = new GridPoint2(node.x + i, node.y + j);
				if (!(i == 0 && j == 0) && islandTiles.contains(child)) {
					children.add(child);
				}
			}
		}
		return children;
	}
	
	/**
	 * 
	 * @param height
	 * @param width
	 * @param tileSize
	 */
	public void registerMapDetails(int height, int width, float tileSize) {
		this.mapWidth = width;
		this.mapHeight = height;
		MapService.tileSize = tileSize;
	}
	
	/**
	 * 
	 * @param x
	 * @param y
	 */
	public void addIslandTile(int x, int y) {
		islandTiles.add(new GridPoint2(x, y));
	}
	
	/**
	 * 
	 * @param position
	 * @return
	 */
	public boolean isOccupied(GridPoint2 position) {
		return entityToPosition.get(position) != null;
	}
	
	/**
	 * 
	 */
	public Entity getEntityAt(GridPoint2 position) throws NoEntityException {
		MapComponent comp = entityToPosition.get(position);
		if (comp == null) {
			throw new NoEntityException();
		}
		
		return comp.getEntity();
	}

	/**
	 * 
	 * @param position
	 * @return
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
}