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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.RenderComponent;
import com.deco2800.game.rendering.Renderable;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;



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
     * Size of the minimap as a float - square side length
     */
    private final float MINIMAP_SIZE = 2f;


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
    protected void draw(SpriteBatch batch) {
        //Set bounds of minimap, based on camera size
        float minimapLength = MINIMAP_SIZE * camera.zoom;
        float minimapHeight = MINIMAP_SIZE * camera.zoom;

        //Get layer of cells from TiledMap
        TiledMapTileLayer terrainLayer = (TiledMapTileLayer)tiledMap.getLayers().get(0);
        //Determine map width and height
        int mapWidth = terrainLayer.getWidth();
        int mapHeight = terrainLayer.getHeight();
        //Determine the scaled width of each tile in the minimap based on minimap size and map size
        float tileWidth = minimapLength / mapWidth;
        float tileHeight = minimapHeight / mapHeight;
        //Get size of screen in pixels
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        //Convert screen size to world coordinates
        Vector3 world = camera.unproject(new Vector3(screenWidth, screenHeight, 0));

        //Initiate a shapeRenderer to draw map rectangles
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //Iterate over each map tile, and draw a rectangle of the correct colour
        for (int i = 0; i < mapWidth; i++) {
            for (int j = 0; j < mapHeight; j++) {
                TiledMapTileLayer.Cell cell = terrainLayer.getCell(i, j);
                //Take the tile and get its id to determine colour
                //0 = Grass, 1 = Sand, 2 = Ocean
                TerrainTile tile = (TerrainTile) cell.getTile();
                if (tile.getId() == 0) {
                    //City tile - green
                    shapeRenderer.setColor(Color.GREEN);
                } else if (tile.getId() == 1){
                    //Island tile - yellow
                    shapeRenderer.setColor(Color.YELLOW);
                } else {
                    //Must be ocean tile - blue
                    shapeRenderer.setColor(Color.BLUE);
                }
                //Draw each tile at the appropriate position, with the appropriate dimensions
                shapeRenderer.rect(world.x - ((mapWidth - i - 1) * tileWidth), world.y + (tileHeight * j), tileWidth, tileHeight);
            }
        }
        //End shape rendering, restart batch
        shapeRenderer.end();
        batch.begin();
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
     * Minimap instantiated on layer 1 to draw over terrain
     * @return layer of minimap - 1
     */
    @Override
    public int getLayer() {
        return 1;
    }

    @Override
    public int compareTo(Renderable o) {
        return Float.compare(getZIndex(), o.getZIndex());
    }

    public void dispose() {
        ServiceLocator.getRenderService().unregister(this);
    }
}
