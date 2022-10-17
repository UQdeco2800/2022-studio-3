package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputLayer;
import com.deco2800.game.services.ServiceLocator;

import static java.lang.Math.abs;

public class MouseInputComponent extends InputComponent {

    //used for touchDown
    int touchDownX;
    //used for touchDown
    int touchDownY;

    // For friendly units movement
    private final Camera camera;

    boolean leftPressed;

    /**
     * Controls the input of the mouse, whether it be clicked or clicked and dragged so that when the player does so,
     * a trigger is sent to the Selectable component with the location and type of click movement
     */
    public MouseInputComponent() {
        super(5);
        leftPressed = false;
        // For friendly units movement
        this.camera = ServiceLocator.getEntityService().getCamera();
    }

    @Override
    public void create() {
        ServiceLocator.getInputService().register(this, InputLayer.FRIENDLY);
    }

    /**
     * Stores value in this class as the starting point of left mouse button being clicked
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            this.leftPressed = true;
            this.touchDownX = screenX;
            this.touchDownY = screenY;
            entity.getEvents().trigger("release spell");
        } else if (button == Input.Buttons.RIGHT) {
            entity.getEvents().trigger("moveLocation", screenX, screenY);
            // For friendly unit movement (has 'unitWalk' event handler)
            if(entity.getEvents().hasEvent("unitWalk")){
                // Only selected friendly unit will be moving
                SelectableComponent selectedUnit = entity.getComponent(SelectableComponent.class);
                if(selectedUnit != null && selectedUnit.isSelected()){
                    // Find the world coordinates of the cursor
                    Vector2 cursorWorldPos = screenToWorldPosition(screenX, screenY);
                    // Find the difference between the entity position and its central point
                    Vector2 entityDeltas = entity.getPosition().sub(entity.getCenterPosition());
                    // Find target vector such that when the entity reaches the target, it's
                    // central point will align with the cursor
                    Vector2 centerTarget = cursorWorldPos.add(entityDeltas);
                    // Trigger UnitMovementTask
                    entity.getEvents().trigger("unitWalk", centerTarget);
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!this.leftPressed) {
            entity.getEvents().trigger("singleHover", screenX, screenY);
        }

        entity.getEvents().trigger("update pointer", screenX, screenY);
        return false;
    }


    /**
     * To be completed/doesn't work yet:
     * When mouse is clicked and dragged, a rectangle is drawn to tell you the area of units that will be selected.
     * More of a cosmetic function
     *
     * @param screenX X-coordinate of the pointer on screen
     * @param screenY Y-coordinate of the pointer on screen
     * @param pointer the pointer for the event.
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.leftPressed) {
            entity.getEvents().trigger("multipleHover", touchDownX, touchDownY, screenX, screenY);
            entity.getEvents().trigger("updateBox", touchDownX,
                    Gdx.graphics.getHeight() - touchDownY, screenX, Gdx.graphics.getHeight() - screenY);
        }
        return false;
    }

    /**
     * handles the finish of the click.
     * If it's a single tap, then we want to select a single unit in out game, if there is an area, then we send
     * this area onto the selectComponent function so that it can determine the units in the area and then select it
     *
     * @param screenX X-coordinate of the pointer on screen
     * @param screenY Y-coordinate of the pointer on screen
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int touchUpX = screenX;
        int touchUpY = screenY;

        if (button == Input.Buttons.LEFT) {
            if (touchDownX == touchUpX && touchDownY == touchUpY) {
                entity.getEvents().trigger("singleSelect", touchUpX, touchUpY);
            } else {
                entity.getEvents().trigger("multipleSelect", touchDownX, touchDownY, touchUpX, touchUpY);
            }
            entity.getEvents().trigger("stopBox");
        }
        this.leftPressed = false;
        return false;
    }

    /**
     * Converts from screen coordinates to world coordinates, and returns them as a Vector2 (For friendly units).
     *
     * @param screenX the x coordinate on the screen
     * @param screenY the y coordinate on the screen
     * @return the equivalent world coordinates
     */
    public Vector2 screenToWorldPosition(int screenX, int screenY) {
        Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(worldPos.x, worldPos.y);
    }
}
