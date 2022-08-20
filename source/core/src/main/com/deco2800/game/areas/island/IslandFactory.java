package com.deco2800.game.areas.island;

import com.badlogic.gdx.graphics.OrthographicCamera;

/**
 * Class that will generate the map and all entities that occupy it, and register it
 * with the game engine.
 * 
 *
 */
public class IslandFactory {
	// defines the height and width of the map. change later
	private static final GridPoint2 MAP_SIZE = new GridPoint2(30, 30);
	
	/**
	 * Create a island factory with Orthogonal orientation
     *
     * @param cameraComponent Camera to render islands to. Must be ortographic.
     */
    public IslandFactory(CameraComponent cameraComponent) {
	 	// where does TerrainOrientation come from??
	    this(cameraComponent, IslandOrientation.ORTHOGONAL);
	}
	
	/**
     * Create a island factory
     *
     * @param cameraComponent Camera to render islands to. Must be orthographic.
     * @param orientation orientation to render terrain at
     */
    public IslandFactory(CameraComponent cameraComponent, IslandOrientation orientation) {
	    this.camera = (OrthographicCamera) cameraComponent.getCamera();
	    this.orientation = orientation;
	}
    
    public IslandComponent createIsland() {
    	ResourceService resourceService = ServiceLocator.getResourceService();
    	// create TextureRegion's here using the ResourceService
    	// can change this size as needed
    	float tileWorldSize = 1f;
    	return createIslandWithView(tileWorldSize);
    }
    
    private IslandComponent createIslandWithView(float tileWorldSize) {
    	// a GridPoint2 object can be created depending on TextureRegion used. these are dummy values
    	GridPoint2 tilePixelSize = new GridPoint2(1f, 1f);
    	// pass to this method the types of tiles to create
        TiledMap tiledMap = createForestDemoTiles(tilePixelSize);
        TiledMapRenderer renderer = createRenderer(tiledMap, tileWorldSize / tilePixelSize.x);
        return new IslandComponent(camera, tiledMap, renderer, orientation, tileWorldSize);
    }
    
    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
    	// because we are using an isometric view
    	return new IsometricTiledMapRenderer(tiledMap, tileScale);
    }
	
	// creates a TiledMap layer which represents the game area
	private TiledMap createForestDemoTiles(GridPoint2 tileSize) {
	    TiledMap tiledMap = new TiledMap();
	    
	    // create tile objects here
	    DummyTile dummyTile = new DummyTile();
	    
	    TiledMapTileLayer layer = new TiledMapTileLayer(MAP_SIZE.x, MAP_SIZE.y, tileSize.x, tileSize.y);

	    // Create base island
		fillTiles(layer, MAP_SIZE, dummyTile);
		
		tiledMap.getLayers().add(layer);
		return tiledMap;
  	}
	
	// i don't know why this is static??
	private static void fillTiles(TiledMapTileLayer layer, GridPoint2 mapSize, TerrainTile tile) {
		/*
		 * Fill in the base map tiles here using the follwing pattern:
		 * 
		 * Cell cell = new Cell();
         * cell.setTile(tile);
         * layer.setCell(x, y, cell);
         * 
         * can call procedural map generation algorithm here
		 */
		
		proceduralMapGeneration();
	}
	
	private static void proceduralMapGeneration() {}
}