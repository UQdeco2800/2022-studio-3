package com.deco2800.game.worker.components.movement;

import java.lang.Math;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 */
public class TouchWorkerInputComponent extends InputComponent {
    private final Vector2 walkDirection = Vector2.Zero.cpy();

    public TouchWorkerInputComponent() {
        super(5);
    }

    /**
     * Triggers worker movement .
     *
     * @return whether the input was processed
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            Vector2 target = new Vector2(screenX, screenY);
            entity.getEvents().trigger("workerWalk", target);
            return true;
        }
        return false;
    }
}