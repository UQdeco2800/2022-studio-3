package com.deco2800.game.map;

public class MapService {
	private final List<MapComponent> interactables = new ArrayList<>();
	private final List<GridPoint2> islandTiles = new ArrayList<>();
	/**
     * Current tileSize of game
     */
    private float tileSize = -1;

    /**
     * Width of game map
     */
    private int mapWidth;

    /**
     * Height of game map
     */
    private int mapHeight;
	
	public void register(MapComponent comp) {
		interactables.add(comp);
	}
	
	public void unregister(MapComponent comp) {
		interactables.remove(comp);
	}
	
	public List<GridPoint2> getPath(float startX, float startY, float endX, float endY) {
		List<GridPoint2> path = new ArrayList<>();
		// add BFS search here?
		return path;
	}
	
	public void registerMapDetails(int height, int width) {
		mapWidth = width;
		mapHeight = height;
	}
	
	public void addIslandTile(int x, int y) {
		islandTiles.add(new GridPoint2(x, y));
	}
	
	public boolean isOccupied(int x, int y) {
		// TODO: add HashMap struct & remove loop
		for (MapComponent c : interactables) {
			GridPoint2 coord = worldToTile(c.getEntity());
			if (x == coord.x && y == coord.y) {
				return true;
			}
		}
		
		return false;
	}
	
	public Entity getEntityAt(int x, int y) throws NoEntityException {
		// TODO: add HashMap struct & remove loop
		for (MapComponent c : interactables) {
			GridPoint2 coord = worldToTile(c.getEntity());
			if (x == coord.x && y == coord.y) {
				return c.getEntity();
			}
		}
		
		throw new NoEntityException();
	}
	
	/**
     * Converts a world point to a position on the tile map
     * @param worldX world y position of an entity
     * @param worldY world y position of an entity
     * @return position of the world point on the tile map
     */
    public GridPoint2 worldToTile(float worldX, float worldY) {
        //Define constants to make expression simpler
        float b = tileSize / 2;
        float a = tileSize / 3.724f;

        int tileX = (int) ((worldX / b) - (worldY / a)) / 2;
        int tileY = (int) (worldY / a) + tileX;

        return new GridPoint2(tileX, tileY);
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