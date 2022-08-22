package com.deco2800.game.worker.components.movement;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 */
public class TouchWorkerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();
    private final Camera camera;

    public TouchWorkerInputComponent() {
        super(5);
        this.camera = ServiceLocator.getEntityService().getCamera();
    }

    /**
     * Triggers worker movement on left-mouse click.
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event
     * @param button the button
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            // Find the world coordinates of the cursor
            Vector2 bottomLeftTarget = screenToWorldPosition(screenX, screenY);
            // Find the difference between the entity position and its central point
            Vector2 entityDeltas = entity.getPosition().sub(entity.getCenterPosition());
            // Find new target vector such that when the entity reaches the target, it's
            // central point will align with the cursor
            Vector2 centerTarget = bottomLeftTarget.add(entityDeltas);
            // Trigger WorkerMovementTask
            entity.getEvents().trigger("workerWalk", centerTarget);
            return true;
        }
        return false;
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
}