package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.Component;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

import java.security.Provider;

public class SelectableComponent extends Component {

    //trigger to see whether unit is selected
    private boolean selected;

    /**
     * initializes component with unit not being selected
     */
    public SelectableComponent() {
        selected = false;
    }


    @Override
    public void create() {
        entity.getEvents().addListener("click", this::isIn);
        entity.getEvents().addListener("dragAndClick", this::containsUs);
        entity.getEvents().addListener("dragSelect", this::containsUs);
    }


    @Override
    public void update() {

    }

    /**
     * Called when a single click form the player happens. Checks if the hitbox of current entity is inside that click
     * If it is, this unit is selected and if it's not, then do nothing
     *
     * @param xCoordinate The x coordinate of the mouse
     * @param yCoordinate The y coordinate of the mouse
     */
    public void isIn(int xCoordinate, int yCoordinate) {
        Vector2 pointInWorld = screenToWorldPosition(xCoordinate, yCoordinate);
        Vector2 startPosition = entity.getPosition();
        Vector2 endPosition = entity.getPosition().mulAdd(entity.getScale(), 1f);
        if (contains(pointInWorld.x, startPosition.x, endPosition.x)
                && contains(pointInWorld.y, startPosition.y, endPosition.y)) {
            selected = true;
        } else {
            selected = false;
        }
    }

    /**
     * Called when player wishes to select units in a certain area on the screen and checks if this unit is in that area
     *
     * @param startX start of x coordinate of box
     * @param startY start of y coordinate of box
     * @param endX end of x coordinate of box
     * @param endY end of y coordinate of box
     */
    public void containsUs(int startX, int startY, int endX, int endY) {
        Vector2 centrePosition = entity.getCenterPosition();
        Vector2 pointStartWorld = screenToWorldPosition(startX, startY);
        Vector2 pointEndWorld = screenToWorldPosition(endX, endY);
        if (contains(centrePosition.x, pointStartWorld.x, pointEndWorld.x)
                && contains(centrePosition.y, pointStartWorld.y, pointEndWorld.y)) {
            selected = true;
        } else {
            selected = false;
        }
    }

    /**
     *
     * @param inside position to check
     * @param from one bound to check
     * @param to other bounch to check
     * @return true if inside is between from and to
     */
    public boolean contains(float inside, float from, float to) {
        return inside <= from && inside >= to || inside >= from && inside <= to;
    }

    /**
     * Turns a selected screen position to the world position
     *
     * @param screenX screen position of x
     * @param screenY screen position of y
     * @return (x,y) in vector form in the world
     */
    public Vector2 screenToWorldPosition(int screenX, int screenY) {
        Vector3 worldPos = ServiceLocator.getEntityService().getCamera().unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(worldPos.x, worldPos.y);
    }

    /**
     *
     * @return if the unit is selected to function with other components
     */
    public boolean isSelected() {
        return this.selected;
    }
}
