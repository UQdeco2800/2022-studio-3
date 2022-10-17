package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.areas.terrain.TerrainFactory;
import com.deco2800.game.entities.BuildingType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class ConstructionInputComponent extends InputComponent {
    private Camera camera;
    private InputService inputService;
    private Entity placementHightlight;
    private GridPoint2 placePoint = new GridPoint2(0, 0);
    private BuildingType type;
    private boolean clear = false;
    private boolean finished = false;

    public ConstructionInputComponent(Entity highlight, BuildingType type) {
        super(7);
        this.camera = ServiceLocator.getEntityService().getCamera();
        this.inputService = ServiceLocator.getInputService();
        placementHightlight = highlight;
        this.type = type;
        Vector3 mousePos = camera.unproject(new Vector3(Gdx.input.getX(),
                Gdx.input.getY(), 0));
        GridPoint2 nearest = worldPositionToTile(new Vector2(mousePos.x,
                mousePos.y));
        ServiceLocator.getGameArea().spawnEntityAt(highlight, nearest, true,
                true);

    }

    // follow the mouse
    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector3 mouseOver = camera.unproject(new Vector3(screenX, screenY, 0));
        GridPoint2 nearestTile = worldPositionToTile(new Vector2(mouseOver.x,
                mouseOver.y));
        Vector2 offset;
        PolygonShape clearance = new PolygonShape();
        switch(type) {
            case BARRACKS -> {
                offset = new Vector2(2f, 2f);
                clearance.setAsBox(2f, 2f, tileToWorldPosition(nearestTile.x,
                        nearestTile.y), (float) (Math.PI/4f));
            }
            case WALL -> {
                nearestTile.sub(0, 1);
                offset = new Vector2(0.5f, 0.5f);
                clearance.setAsBox(1.5f, 1.5f,
                        tileToWorldPosition(nearestTile.x,
                        nearestTile.y), (float) (Math.PI/4f));
            }
            default -> {
                offset = new Vector2(0, 0);
                clearance.set(new float[]{0f});
            }

        }
        setClear(ServiceLocator.gameArea.isRegionClear(clearance));
        placementHightlight.setPosition(tileToWorldPosition(nearestTile.x,
                nearestTile.y).sub(offset));
        placePoint = nearestTile;
        // we don't want this to interfere with any other mouseMoved calls
        return false;
    }

    // if left click, attempt build; if right click, cancel
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            if (!clear) return false;
            ServiceLocator.getGameArea().spawnEntityAt(getBuilding(type),
                    placePoint, true, true);
            placementHightlight.setScale(0, 0);
            ServiceLocator.getInputService().unregister(this);
            finished = true;
            return true;
        } else if (button == Input.Buttons.RIGHT) {
            placementHightlight.setScale(0, 0);
            finished = true;
            ServiceLocator.getInputService().unregister(this);
            finished = true;
            return true;
        }
        return false;
    }

    /**
     * Wrapper for the BuildingFactory methods to use enum
     * @param type which type of building we are producing
     * @return the Entity corresponding to the type of building given
     */
    private Entity getBuilding(BuildingType type) {
        if (type == BuildingType.WALL) {
            return BuildingFactory.createWall();
        } else if (type == BuildingType.BARRACKS) {
            return BuildingFactory.createBarracks();
        }
        return BuildingFactory.createBaseBuilding();
    }

    private GridPoint2 worldPositionToTile(Vector2 worldPos) {
        // really just invert the previous function
        float tileSize = AtlantisTerrainFactory.mapTileScale;
        float i = ((worldPos.x * 2f) - (worldPos.y * 3.724f)) / tileSize / 2f;
        float j =
                ((worldPos.x * 2f) + (worldPos.y * 3.724f)) / tileSize / 2f;
        return new GridPoint2(Math.round(i), Math.round(j));

    }

    private Vector2 tileToWorldPosition(int x, int y) {
        float tileSize = AtlantisTerrainFactory.mapTileScale;
        return new Vector2((x + y) * tileSize / 2, (y - x) * tileSize / 3.724f);
    }

    /**
     * Updates highlight texture and clear field based on given boolean
     * @param isClear whether the placement region is clear of obstructions
     */
    private void setClear(boolean isClear) {
        // We should really have variables in our ResourceService corresponding
        // to particular textures but oh well
        if (isClear) {
            switch (type) {
                case BARRACKS -> placementHightlight.getComponent(
                        TextureRenderComponent.class)
                        .setTexturePath("images/barracks_highlight_green.png");
                case WALL -> placementHightlight.getComponent(TextureRenderComponent.class).setTexturePath("images/wooden_wall_green.png");
            }
        } else {
            switch(type) {
                case BARRACKS -> placementHightlight.getComponent(TextureRenderComponent.class).setTexturePath("images/barracks_highlight_red.png");
                case WALL -> placementHightlight.getComponent(TextureRenderComponent.class).setTexturePath("images/wooden_wall_red.png");
            }
        }

        this.clear = isClear;
    }

    /**
     * Flag for the calling ConstructionCommand that this building placement
     * is done.
     * @return whether this building process has been cancelled or completed
     */
    public boolean isFinished() {
        return finished;
    }

}
