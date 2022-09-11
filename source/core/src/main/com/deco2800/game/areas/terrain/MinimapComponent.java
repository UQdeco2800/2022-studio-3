package com.deco2800.game.areas.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.input.CameraInputComponent;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.Renderable;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MinimapComponent extends RenderComponent {
    /**
     * Map of the game
     */
    private final TiledMap tiledMap;
    /**
     * Primary camera of the game
     */
    private final OrthographicCamera camera;

    /**
     * Scale the Minimap in size multiplicatively
     */
    private final float MINIMAP_SCALE = 1.5f;


    /**
     * Create a new MinimapComponent, with a TiledMap to render and the game's camera
     * @param map TiledMap representing the game to be rendered
     * @param camera Game camera
     */
    public MinimapComponent(TiledMap map, OrthographicCamera camera) {
        this.tiledMap = map;
        this.camera = camera;
    }


    @Override
    public void draw(SpriteBatch batch) {
        //Get layer of cells from TiledMap
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        //Determine map width and height
        int mapWidth = terrainLayer.getWidth();
        int mapHeight = terrainLayer.getHeight();

        //Set bounds of minimap, based on camera size
        float minimapLength = mapWidth * MINIMAP_SCALE/100f * camera.zoom;
        float minimapHeight = mapHeight * MINIMAP_SCALE/100f * camera.zoom;

        //Determine the scaled width of each tile in the minimap based on minimap size and map size
        float tileWidth = minimapLength / mapWidth;
        float tileHeight = minimapHeight / mapHeight;

        //Get size of screen in pixels
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        //Convert screen size to world coordinates
        Vector3 world = camera.unproject(new Vector3(screenWidth, screenHeight, 0));

        //Define constant for scaling images to a desired pixel size
        float scaleConstant = 6/25f;

        //Define textures for drawing the map
        Texture dummyTile = ServiceLocator.getResourceService().getAsset("test/files/dummyTexture.png", Texture.class);
        Texture dummyOcean = ServiceLocator.getResourceService().getAsset("test/files/dummyOcean.png", Texture.class);

        //Determine width and height in pixels of each rendered tile
        GridPoint2 tileSize = worldToPixelLength(new Vector2(tileWidth, tileHeight));

        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                Texture currentTexture;
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(i, j);
                //Take the tile and get its id to determine colour
                //0 = Grass, 1 = Sand, 2 = Ocean
                TerrainTile tile = (TerrainTile) cell.getTile();
                if (tile.getId() == 0) {
                    //City tile
                    currentTexture = dummyTile;
                } else if (tile.getId() == 1){
                    //Island tile
                    currentTexture = dummyTile;
                } else {
                    //Must be ocean tile
                    currentTexture = dummyOcean;
                }
                //Draw each tile texture at the appropriate position, with the appropriate dimensions
                //Determine scaling for the tile image based on current zoom
                float tileXScale = tileSize.x / (currentTexture.getWidth() * scaleConstant);
                float tileYScale = tileSize.y / (currentTexture.getHeight() * scaleConstant);
                Vector2 tileScale = new Vector2(tileXScale * camera.zoom, tileYScale * camera.zoom);
                //Set the draw position of the tile
                Vector2 drawPosition = new Vector2(world.x - ((mapWidth - i - 1) * tileWidth), world.y + (tileHeight * j));
                batch.draw(currentTexture, drawPosition.x, drawPosition.y, tileScale.x, tileScale.y);
            }
        }

        //Display field of view

        //Initiate a shapeRenderer to draw map field of view
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        batch.end();
        
        //Get edges of screen
        Vector3 worldSW = camera.unproject(new Vector3(0, screenHeight, 0));
        Vector3 worldSE = camera.unproject(new Vector3(screenWidth, screenHeight, 0));
        Vector3 worldNW = camera.unproject(new Vector3(0, 0, 0));
        Vector3 worldNE = camera.unproject(new Vector3(screenWidth, 0, 0));

        //Convert to tile coordinates
        TerrainComponent tc = this.entity.getComponent(TerrainComponent.class);
        Map<String, GridPoint2> tileExtremities = new HashMap<>();
        tileExtremities.put("NW", CameraInputComponent.worldToTile(tc.getTileSize(), worldNW.x, worldNW.y));
        tileExtremities.put("NE", CameraInputComponent.worldToTile(tc.getTileSize(), worldNE.x, worldNE.y));
        tileExtremities.put("SW", CameraInputComponent.worldToTile(tc.getTileSize(), worldSW.x, worldSW.y));
        tileExtremities.put("SE", CameraInputComponent.worldToTile(tc.getTileSize(), worldSE.x, worldSE.y));

        //Set tiles within bounds if they are out of bounds
        for (GridPoint2 tp : tileExtremities.values()) {
            if (tp.x < 0) {
                tp.x = 0;
            } else if (tp.x >= mapWidth) {
                tp.x = mapWidth - 1;
            }
            if (tp.y < 0) {
                tp.y = 0;
            } else if (tp.y >= mapHeight) {
                tp.y = mapHeight - 1;
            }
        }
        //Define max and min tiles viewable for each cartesian coordinate
        int maxX = tileExtremities.get("SE").x;
        int minX = tileExtremities.get("NW").x;
        int maxY = tileExtremities.get("NE").y;
        int minY = tileExtremities.get("SW").y;

        //Set hollow fill for field of view rectangle
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.BLACK);
        /*
            - Rectangle x edge set at the (mapWidth - 1 - the smallest X tile visible) * tileWidth
                - mapWidth - 1 - smallest_tile_x sends you to tile position desired,
                but it must be multiplied by tileWidth to get position on world
            - Rectangle y point set at minimum y point -> again multiplied by tileHeight to reach
              world position
            - Rectangle side lengths is simply the tiles along * by the width or height
         */
        shapeRenderer.rect(world.x - ((mapWidth - minX - 1) * tileWidth),
                world.y + (tileHeight * minY),
                (maxX - minX) * tileWidth,
                (maxY - minY) * tileHeight);
        shapeRenderer.end();
        drawEntities(shapeRenderer, world, tileHeight, tileWidth, mapWidth);

        batch.begin();
    }

    /**
     * Helper function that draws an entity on the minimap.
     */
    private void drawEntities(ShapeRenderer shapeRenderer, Vector3 world, float tileHeight, float tileWidth, int mapWidth) {
        Map<GridPoint2, MapComponent> positionToEntity = ServiceLocator.getMapService().getEntityOccupiedPositions();
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);


        for (Map.Entry<GridPoint2, MapComponent> item : positionToEntity.entrySet()) {
            if (item.getValue().isDisplay()) {
                GridPoint2 position = item.getKey();
                shapeRenderer.setColor(item.getValue().getColour());
                shapeRenderer.rect(world.x - ((mapWidth - position.x - 1) * tileWidth), world.y + (tileHeight * position.y), tileWidth, tileHeight);
            }
        }
        shapeRenderer.end();
    }

    /*
     * Takes a pair of floats in the form of a Vector2, which correspond to the width and height of a rectangle
     * drawn on the screen. Returns the pixel size of the width and height of this rectangle.
     * @param worldSize (x,y) side lengths of a rectangle in world coordinates
     * @return the pixel side lengths of the desired rectangle
     */
    private GridPoint2 worldToPixelLength(Vector2 worldSize) {
        float pixelWidth = camera.project(new Vector3(worldSize.x, 0, 0)).x - camera.project(new Vector3(0,0,0)).x;
        float pixelHeight = camera.project(new Vector3(0, worldSize.y, 0)).y - camera.project(new Vector3(0,0,0)).y;
        return new GridPoint2((int)Math.ceil(pixelWidth), (int)Math.ceil(pixelHeight));
    }

    /**
     * Gets Z index of Minimap - 0 (higher Z in layer 1 drawn on top)
     * @return z index
     */
    @Override
    public float getZIndex() {
        return 0;
    }

    /**
     * Minimap instantiated on layer 2 to draw over terrain and player
     * @return layer of minimap - 2
     */
    @Override
    public int getLayer() {
        return 2;
    }

    @Override
    public int compareTo(Renderable o) {
        return Float.compare(getZIndex(), o.getZIndex());
    }

    public void dispose() {
        ServiceLocator.getRenderService().unregister(this);
    }
}
