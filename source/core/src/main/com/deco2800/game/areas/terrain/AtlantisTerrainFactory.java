package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.random.PseudoRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.tiles.AnimatedTiledMapTile;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.TerrainComponent.TerrainOrientation;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.random.PseudoRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/** Factory for creating game terrain. */
public class AtlantisTerrainFactory {
    /**
     * Variables to create the map with MapGenerator
     */
    private static final int mapWidth = 200;
    private static final int mapHeight = 90;
    private static final int cityWidth = 50;
    private static final int cityHeight = 44;
    private static final int islandSize = 80;

    public static final float mapTileScale = 1f;

    /**
     * TerrainFactory variables
     */
    private final OrthographicCamera camera;
    private final TerrainOrientation orientation = TerrainOrientation.ISOMETRIC;
    //Store textures used to build game
    private final Map<String, TextureRegion> textures;

    private final CameraComponent cameraComponent;

    /**
     * Instantiate a new MapGenerator for this terrain factory
     */
    private static MapGenerator mapGenerator = new MapGenerator(mapWidth, mapHeight, cityWidth, cityHeight, islandSize);

    /**
     * Create a terrain factory with Isometric orientation
     *
     * @param cameraComponent Camera to render terrain to. Must be orthographic.
     */
    public AtlantisTerrainFactory(CameraComponent cameraComponent) {
        this.camera = (OrthographicCamera) cameraComponent.getCamera();
        this.cameraComponent = cameraComponent;
        this.textures = new HashMap<>();
    }

    /**
     * Create all textures needed to generate the AtlantisMap
     */
    private void createTextures() {
        ResourceService resourceService = ServiceLocator.getResourceService();
        textures.put("Grass", new TextureRegion(resourceService.getAsset("images/Grass.png", Texture.class)));
        textures.put("Sand", new TextureRegion(resourceService.getAsset("images/Sand.png", Texture.class)));
        textures.put("Sand Starfish", new TextureRegion(resourceService.getAsset("images/sand_starfish.png", Texture.class)));
        textures.put("Sand Shell", new TextureRegion(resourceService.getAsset("images/sand_shell.png", Texture.class)));
        textures.put("City", new TextureRegion(resourceService.getAsset("images/city_tile.png", Texture.class)));
        textures.put("Sea1", new TextureRegion(resourceService.getAsset("images/sea_1.png", Texture.class)));
        textures.put("Sea2", new TextureRegion(resourceService.getAsset("images/sea_2.png", Texture.class)));
        textures.put("Sea3", new TextureRegion(resourceService.getAsset("images/sea_3.png", Texture.class)));
        textures.put("Sea4", new TextureRegion(resourceService.getAsset("images/sea_4.png", Texture.class)));
        textures.put("Flash1", new TextureRegion(resourceService.getAsset("images/flash_1.png", Texture.class)));
        textures.put("Flash2", new TextureRegion(resourceService.getAsset("images/flash_2.png", Texture.class)));
        textures.put("Flash3", new TextureRegion(resourceService.getAsset("images/flash_3.png", Texture.class)));
        textures.put("Flash4", new TextureRegion(resourceService.getAsset("images/flash_4.png", Texture.class)));
    }

    private void setMapGenerator (MapGenerator newMapGenerator) {
        mapGenerator = newMapGenerator;
    }

    public TerrainComponent createAtlantisTerrainComponent() {
        createTextures();
        //Take a sample texture to determine size
        TextureRegion grass = textures.get("Grass");
        //Determine the size of grass TextureRegion in pixels
        GridPoint2 tilePixelSize = new GridPoint2(grass.getRegionWidth(), grass.getRegionHeight());
        //Create a TiledMap holding cells with TextureRegions denoting the image at that tile
        //i.e. the data structure which will be visualised
        TiledMap tiledMap = createMapTiles(tilePixelSize);
        //Make renderer for map
        TiledMapRenderer renderer = createRenderer(tiledMap, mapTileScale / tilePixelSize.x);
        //Return the component
        return new TerrainComponent(camera,  tiledMap, renderer, orientation, mapTileScale);
    }

    /**
     * Randomly selects a tile for a unit to roam to.
     * @param startPos Starting position, bounding the exploration.
     * @return A GridPoint2 coordinate to move to.
     */
    public GridPoint2 randomlySelectTileToMoveTo(GridPoint2 startPos) {
        //Get relevant details
        int mapHeight = mapGenerator.getHeight();
        int cityHeight = mapGenerator.getCityHeight();
        int cityWidth = mapGenerator.getCityWidth();
        int x = startPos.x/2;
        int y = startPos.y/2;

        //Set bounds
        int minX = mapGenerator.getBottomLeftY();
        int maxX = minX + cityWidth;
        int minY = mapGenerator.getBottomLeftX();
        int maxY = minY + cityHeight;

        //New Bounds for x and y
        int lowerX = x - 5;
        int upperX = x + 5;
        int lowerY = y - 5;
        int upperY = y + 5;

        //Keep within bounds
        if (lowerX < minX) {
            lowerX = minX;
        }
        if (upperX > maxX) {
            upperX = maxX;
        }
        if (lowerY < minY) {
            lowerY = minY;
        }
        if (upperY > maxY) {
            upperY = maxY;
        }

        //Generate New Coords
        int newX = (lowerX >= upperX) ? lowerX : PseudoRandom.seedRandomInt(lowerX, upperX);
        int newY = (lowerY >= upperY) ? lowerY : PseudoRandom.seedRandomInt(lowerY, upperY);
        return new GridPoint2(newX, newY);
    }

    /**
     * Updates the structure and rendering to show flooding.
     */
    public void floodTiles() {
        mapGenerator.flood();
        this.createAtlantisTerrainComponent();
    }

    /**
     * Updates the structure and rendering to show flashing tiles before flooding.
     */
    public void flashTiles() {
        mapGenerator.flashTiles();
        this.createAtlantisTerrainComponent();
    }

    /**
     * Create a new isometric renderer for the map
     * @param tiledMap the map of cells denoting the game area
     * @param tileScale how scaled each tile image is (1 unchanged)
     * @return
     */
    private TiledMapRenderer createRenderer(TiledMap tiledMap, float tileScale) {
        return new IsometricTiledMapRenderer(tiledMap, tileScale);
    }

    /**
     * Creates the TiledMap holding a single layer of cells holding TerrainTiles
     * @param tileSize
     * @return
     */
    private TiledMap createMapTiles(GridPoint2 tileSize) {
        TiledMap tiledMap = new TiledMap();
        TiledMapTileLayer layer = new TiledMapTileLayer(mapWidth, mapHeight, tileSize.x, tileSize.y);

        //Fill layer with cells holding textures corresponding to the map generated by MapGenerator
        fillTiles(layer);
        //Add the layer of tiles to the tiledMap - it will only have one layer.
        tiledMap.getLayers().add(layer);
        return tiledMap;
    }

    /**
     * Fills each cell of a tiledMap with correct texture corresponding
     * @param layer the layer with which to be filled with cells
     */
    private void fillTiles(TiledMapTileLayer layer) {
        //Set terrainTiles based on textures stored in textures list
        TerrainTile cityTile = new TerrainTile(textures.get("City"));
        TerrainTile sandTile = new TerrainTile(textures.get("Sand"));
        TerrainTile sandStarTile = new TerrainTile(textures.get("Sand Starfish"));
        TerrainTile sandShellTile = new TerrainTile(textures.get("Sand Shell"));

        //Create an AnimatedTiledMapTile with frames corresponding to each ocean texture
        Array<StaticTiledMapTile> oceanFrames = new Array<>();
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea1")));
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea2")));
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea3")));
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea4")));
        AnimatedTiledMapTile animatedOceanTile = new AnimatedTiledMapTile(1f/2f, oceanFrames);

        //Create an AnimatedTiledMapTile for flashing tiles to be flooded
        Array<StaticTiledMapTile> flashTiles = new Array<>();
        flashTiles.add(new StaticTiledMapTile(textures.get("Flash1")));
        flashTiles.add(new StaticTiledMapTile(textures.get("Flash2")));
        flashTiles.add(new StaticTiledMapTile(textures.get("Flash3")));
        flashTiles.add(new StaticTiledMapTile(textures.get("Flash4")));
        AnimatedTiledMapTile animatedFlashTile = new AnimatedTiledMapTile(1f/5f, flashTiles);

        //Set id for each tile - used for visualising minimap
        cityTile.setId(0);
        sandTile.setId(1);
        sandStarTile.setId(1);
        sandShellTile.setId(1);

        //Load the map from the map generator
        char[][] map = mapGenerator.getMap();
        //Iterate through the map and set cells according to their type
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                int randNum = new Random().nextInt(100);
                Cell cell = new Cell();
                if (map[y][x] == mapGenerator.getOceanChar()) {
                    //Set ocean tiles to animated ocean textures
                    cell.setTile(animatedOceanTile);
                } else if (map[y][x] == mapGenerator.getFlashChar()) {
                    cell.setTile(animatedFlashTile);
                } else if (map[y][x] == mapGenerator.getIslandChar()) {
                    //Set island tiles to sand textures
                    //Set to correct new texture if applicable
                    if (randNum == 0) {
                        cell.setTile(sandStarTile);
                    } else if (randNum == 1) {
                        cell.setTile(sandShellTile);
                    } else {
                        cell.setTile(sandTile);
                    }
                    ServiceLocator.getMapService().addIslandTile(x, mapHeight - 1 - y);
                } else {
                    cell.setTile(cityTile);
                    ServiceLocator.getMapService().addIslandTile(x, mapHeight - 1 - y);
                }
                //Set cell to layer at a position (i.e. Tile) - y mapped inversely
                layer.setCell(x, mapHeight - 1 - y, cell);
            }
        }
    }

    /**
     * Change the outline of the map island into sea
     * @param layer The layer before the change from island to sea
     */
    private void floodOutlineMap(TiledMapTileLayer layer) {
        //Create an AnimatedTiledMapTile with frames corresponding to each ocean texture
        Array<StaticTiledMapTile> oceanFrames = new Array<>();
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea1")));
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea2")));
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea3")));
        oceanFrames.add(new StaticTiledMapTile(textures.get("Sea4")));
        AnimatedTiledMapTile animatedOceanTile = new AnimatedTiledMapTile(1/3f, oceanFrames);

        char[][] map = mapGenerator.getOutlineMap();
        for (int x = 0; x < mapWidth; x++) {
            for (int y = 0; y < mapHeight; y++) {
                Cell cell = new Cell();
                if (map[y][x] == mapGenerator.getIslandChar()) {
                    //ServiceLocator.getMapService().removeIslandTile(x, mapHeight - 1 - y);
                    cell.setTile(animatedOceanTile);
                    ServiceLocator.getMapService().addIslandTile(x, mapHeight - 1 - y);
                }
                layer.setCell(x, mapHeight - 1 - y, cell);
            }
        }
    }

    /**
     * Returns a new MapGenerator with identical parameters as the one created for the game -
     * used in testing the consistency of the MapGenerator
     * @return new MapGenerator with correct parameters
     */
    public static MapGenerator makeMapGenerator() {
        return new MapGenerator(mapWidth, mapHeight, cityWidth, cityHeight, islandSize);
    }

    /**
     * Getter for the CameraComponent of this class
     * @return MapGenerator object
     */
    public CameraComponent getCameraComponent() {
        return this.cameraComponent;
    }

    /**
     * Getter for the MapGenerator of this class
     * @return CameraComponent object
     */
    public MapGenerator getMapGenerator() {
        return this.mapGenerator;
    }
}
