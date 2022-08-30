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
	
	/**
	 * 
	 * @param comp
	 * @throws IllegalEntityPlacementException
	 */
	public void register(MapComponent comp) throws IllegalEntityPlacementException {
		interactables.add(comp);
		GridPoint2 position = worldToTile(comp.getEntity().getPosition());

		// check the tile is not already occupied
		if (entityToPosition.get(position) != null) {
			throw new OccupiedTileException();
		}

		// check the tile is in the bounds of the island
		if (!islandTiles.contains(position)) {
			throw new OutOfBoundsException();
		}

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
	public List<GridPoint2> getPath(float startX, float startY, float endX, float endY) {
		List<GridPoint2> path = new ArrayList<>();

		// add BFS search here?

		return path;
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