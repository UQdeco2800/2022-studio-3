package com.deco2800.game.components.building;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.helpers.Util;

/**
 * Action component for interacting with a building. Building events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class BuildingActions extends Component {
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
        entity.getEvents().addListener("clicked", this::clicked); // Not triggered by any event yet
        entity.getEvents().addListener("placing", this::placing);
        entity.getEvents().addListener("levelUp", this::addLevel); // Not triggered by any event yet
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        placed = true;
    }

    public static Vector2 screenToPosition(int screenX, int screenY) {
        Vector3 position = ServiceLocator.getEntityService().getCamera().unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(position.x, position.y);
    }

    public void clicked(int screenX, int screenY) {
        if (placed) {
            startPlacing(screenX, screenY);
        } else {
            place(screenX, screenY);
        }
    }

    public void startPlacing(int screenX, int screenY) {
        Vector2 position = screenToPosition(screenX, screenY);
        if (entity.getComponent(ColliderComponent.class).getFixture().testPoint(position)) {
            Util.report("start placing");
            placed = false;
            physicsComponent.setEnabled(false);
        }
    }

    /**
     * On mouse moved event, the Entity's position follows the mouse until placed
     * @param screenX Mouse x coordinate
     * @param screenY Mouse y coordinate
     */
    public void placing(int screenX, int screenY) {
        if (placed) {return;}
        Util.report("placing");
        Vector2 position = screenToPosition(screenX, screenY);
        entity.setPosition(position);
    }

    /**
     * On Left mouse button down, the building will placed at the mouse position
     * @param screenX Mouse x coordinate
     * @param screenY Mouse y coordinate
     */
    public void place(int screenX, int screenY) {
        if (placed) {return;}
        Util.report("place");
        placed = true;
        physicsComponent.setEnabled(true);
        Vector2 position = screenToPosition(screenX, screenY);
        position.mulAdd(entity.getScale(), -0.5f);
        entity.setPosition(new Vector2((int)position.x,(int)position.y));
    }

}
