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
    private final TiledMap tiledMap;
    private final OrthographicCamera camera;

    private final OrthographicCamera testcam = new OrthographicCamera(100, 45);
    private SpriteBatch test = new SpriteBatch();

    public MinimapComponent(TiledMap map, OrthographicCamera camera) {
        this.tiledMap = map;
        this.camera = camera;
        testcam.zoom = 10;
    }

    @Override
    protected void draw(SpriteBatch batch) {
        //float minimapLength = 2 * camera.zoom;
        //float tileSize = minimapSize / 1;
        ShapeRenderer shapeRenderer = new ShapeRenderer();
        batch.end();
        shapeRenderer.setProjectionMatrix(batch.getProjectionMatrix());
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.RED);
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();
        Vector3 world = camera.unproject(new Vector3(screenWidth, screenHeight, 0));
        float squareSize = 2 * camera.zoom;
        shapeRenderer.rect(world.x - squareSize, world.y, squareSize, squareSize);
        shapeRenderer.setColor(Color.BLUE);
        shapeRenderer.end();
        batch.begin();
    }

    @Override
    public float getZIndex() {
        return 0;
    }

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
