package com.deco2800.game.components.worker.movement;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Input handler for selecting, un-selecting and moving worker units
 */
public class WorkerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private final Camera camera;
    private boolean isSelected = false;

    public WorkerInputComponent() {
        super(5);
        this.camera = ServiceLocator.getEntityService().getCamera();
    }

    /**
     * Determines whether to select unit on left-mouse click
     * Moves a selected unit on right-mouse click
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event
     * @param button  the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Find the world coordinates of the cursor
        Vector2 cursorWorldPos = screenToWorldPosition(screenX, screenY);
        if (button == Input.Buttons.RIGHT) {
            if (isSelected) {
                // Worker is selected. Prepare to move to cursor location.
                // Find the difference between the entity position and its central point
                Vector2 entityDeltas = entity.getPosition().sub(entity.getCenterPosition());
                // Find target vector such that when the entity reaches the target, it's
                // central point will align with the cursor
                Vector2 centerTarget = cursorWorldPos.add(entityDeltas);
                // Trigger WorkerMovementTask
                entity.getEvents().trigger("workerWalk", centerTarget);
            }
        } else if (button == Input.Buttons.LEFT) {
            // Worker is not selected.
            // Determine if user is trying to select the entity.
            if (inEntityBounds(cursorWorldPos.x, cursorWorldPos.y)) {
                isSelected = true;
            }
        }
        return false;
    }

    /**
     * Deselects worker when ESC key is pressed.
     *
     * @param keyCode the key typed
     * @return whether the input was processed
     */
    @Override
    public boolean keyDown(int keyCode) {
        if (keyCode == Input.Keys.ESCAPE) {
            // Deselect the worker (if not already)
            isSelected = false;
        }
        return false;
    }

    /**
     * Return whether the entity is selected or not
     *
     * @return True if the entity is selected. False otherwise.
     */
    public boolean isSelected() {
        return this.isSelected;
    }

    /**
     * Converts from screen coordinates to world coordinates, and returns them as a Vector2.
     *
     * @param screenX the x coordinate on the screen
     * @param screenY the y coordinate on the screen
     * @return the equivalent world coordinates
     */
    public Vector2 screenToWorldPosition(int screenX, int screenY) {
        Vector3 worldPos = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(worldPos.x, worldPos.y);
    }

    /**
     * Return a Rectangle which represents the region in the game occupied by the entity.
     *
     * @return the rectangle representing the entity's region in the game
     */
    public Rectangle getEntityBounds() {
        Vector2 entityPos = entity.getPosition();
        Vector2 entityCenter = entity.getCenterPosition();
        float entityWidth = 2 * Math.abs(entityPos.x - entityCenter.x);
        float entityHeight = 2 * Math.abs(entityPos.y - entityCenter.y);
        return new Rectangle(entityPos.x, entityPos.y, entityWidth, entityHeight);
    }

    /**
     * Determines if a given point (x,y) is within the entity's bounds
     *
     * @param x the x coordinate of the point's world position
     * @param y the y coordinate of the point's world position
     * @return true if the point is in the entity's bounds. False otherwise.
     */
    public boolean inEntityBounds(float x, float y) {
        return getEntityBounds().contains(x, y);
    }
}