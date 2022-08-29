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

/**
 * Action component for interacting with a building. Building events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class BuildingActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(BuildingActions.class);
    private boolean placed;
    private PhysicsComponent physicsComponent;

    private int level;

    /**
     * Constructs components with specified level
     * @param level initial building level when created
     */
    public BuildingActions(int level) {
        this.level = level;
    }

    /**
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Increments level by 1
     */
    public void addLevel() {
        this.level++;
    }

    /**
     * Called upon creation. Used to define events for event listener to trigger.
     */
    public void create() {
        entity.getEvents().addListener("startPlacing", this::startPlacing); // Not triggered by any event yet
        entity.getEvents().addListener("placing", this::placing);
        entity.getEvents().addListener("place", this::place);
        entity.getEvents().addListener("levelUp", this::addLevel); // Not triggered by any event yet
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        placed = true;
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
        // Scale from input units to grid point
        position.scl(17.29f / (Gdx.graphics.getWidth() - 240), 11.3f / Gdx.graphics.getHeight());
        // Snap to grid by rounding
        position = new Vector2((int)(position.x), (int)(position.y));
        return position;
    }

    /**
     * Disables physics component and sets placed to false
     */
    public void startPlacing() {
        if (!placed) {return;}
        placed = false;
        physicsComponent.setEnabled(false);
    }

    /**
     * On mouse moved event, the Entity's position follows the mouse until placed
     * @param screenX Mouse x coordinate
     * @param screenY Mouse y coordinate
     */
    public void placing(int screenX, int screenY) {
        if (placed) {return;}
        Vector2 position = mouseToGrid(screenX, screenY);
        entity.setPosition(position);
    }

    /**
     * On Left mouse button down, the building will placed at the mouse position
     * @param screenX Mouse x coordinate
     * @param screenY Mouse y coordinate
     */
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
