package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(BuildingActions.class);
    private boolean placed;
    private TextureRenderComponent textureRenderComponent;
    private PhysicsComponent physicsComponent;
    private CameraComponent cameraComponent;

    public void create() {
        //entity.getEvents().addListener("placing", this::placing);
        entity.getEvents().addListener("place", this::place);
        entity.getEvents().addListener("cancelPlacement", this::cancelPlacement);
        textureRenderComponent = entity.getComponent(TextureRenderComponent.class);
        textureRenderComponent.setEnabled(true);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.setEnabled(false);
        cameraComponent = entity.getComponent(CameraComponent.class);
        placed = false;
    }

    /**
     * Converts mouse input units to game area grid point units and rounds to the nearest integer by scaling position
     * vector
     * @param screenX Mouse x coordinate
     * @param screenY Mouse y coordinate
     * @return 2-D vector for the position the mouse is on the screen
     */
    private static Vector2 mouseToGrid(int screenX, int screenY) {
        // invert Y
        screenY = Gdx.graphics.getHeight() - screenY;
        // GameArea starts at x=240
        screenX -= 240;
        if (screenX < 0) {
            screenX = 0;
        }
        Vector2 position = new Vector2(screenX, screenY);
        // will remove constants later sprint. Scale from input units to grid point
        position.scl(17.29f / (Gdx.graphics.getWidth() - 240), 11.3f / Gdx.graphics.getHeight());
        // Snap to grid by rounding
        position = new Vector2((int)(position.x), (int)(position.y));
        return position;
    }

    public void placing(int screenX, int screenY) {
        if (placed) {return;}
        Vector2 position = mouseToGrid(screenX, screenY);
        entity.setPosition(position);
    }

    public void place(int screenX, int screenY) {
        if (placed) {return;}
        placed = true;
        physicsComponent.setEnabled(true);
        Vector2 position = mouseToGrid(screenX, screenY);
        entity.setPosition(position);
    }

    public void cancelPlacement() {
        if (placed) {return;}
        entity.dispose();
    }

}
