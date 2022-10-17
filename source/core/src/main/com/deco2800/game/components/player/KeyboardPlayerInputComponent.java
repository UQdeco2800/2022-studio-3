package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler only uses keyboard input.
 */
public class KeyboardPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  int touchDownX = -1;
  int touchDownY = -1;

  public KeyboardPlayerInputComponent() {
    super(5);
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyDown(int)
   */
  @Override
  public boolean keyDown(int keycode) {
    switch (keycode) {
      case Keys.W:
        walkDirection.add(Vector2Utils.UP);
        break;
      case Keys.A:
        walkDirection.add(Vector2Utils.LEFT);
        break;
      case Keys.S:
        walkDirection.add(Vector2Utils.DOWN);
        break;
      case Keys.D:
        walkDirection.add(Vector2Utils.RIGHT);
        break;
      default:
        return false;
    }
    triggerWalkEvent();
    return false;
  }

  /**
   * Triggers player events on specific keycodes.
   *
   * @return whether the input was processed
   * @see InputProcessor#keyUp(int)
   */
  @Override
  public boolean keyUp(int keycode) {
    switch (keycode) {
      case Keys.W:
        walkDirection.sub(Vector2Utils.UP);
        break;
      case Keys.A:
        walkDirection.sub(Vector2Utils.LEFT);
        break;
      case Keys.S:
        walkDirection.sub(Vector2Utils.DOWN);
        break;
      case Keys.D:
        walkDirection.sub(Vector2Utils.RIGHT);
        break;
      default:
        return false;
    }
    triggerWalkEvent();
    return false;
  }


  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }
}
