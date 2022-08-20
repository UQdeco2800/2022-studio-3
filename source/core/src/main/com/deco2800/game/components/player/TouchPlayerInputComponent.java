package com.deco2800.game.components.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import com.badlogic.gdx.InputProcessor;

/**
 * Input handler for the player for keyboard and touch (mouse) input.
 * This input handler uses keyboard and touch input.
 */
public class TouchPlayerInputComponent extends InputComponent {
  private final Vector2 walkDirection = Vector2.Zero.cpy();

  public TouchPlayerInputComponent() {
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
      case Input.Keys.UP:
        walkDirection.add(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Input.Keys.LEFT:
        walkDirection.add(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Input.Keys.DOWN:
        walkDirection.add(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Input.Keys.RIGHT:
        walkDirection.add(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      default:
        return false;
    }
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
      case Input.Keys.UP:
        walkDirection.sub(Vector2Utils.UP);
        triggerWalkEvent();
        return true;
      case Input.Keys.LEFT:
        walkDirection.sub(Vector2Utils.LEFT);
        triggerWalkEvent();
        return true;
      case Input.Keys.DOWN:
        walkDirection.sub(Vector2Utils.DOWN);
        triggerWalkEvent();
        return true;
      case Input.Keys.RIGHT:
        walkDirection.sub(Vector2Utils.RIGHT);
        triggerWalkEvent();
        return true;
      case Input.Keys.G:
        entity.getEvents().trigger("spawnTownHall");
        return true;
      case Input.Keys.H:
        entity.getEvents().trigger("spawnWall");
        return true;
      case Input.Keys.J:
        entity.getEvents().trigger("spawnBarracks");
        return true;
      case Input.Keys.K:
        entity.getEvents().trigger("spawnMedievalBarracks");
        return true;
      default:
        return false;
    }
  }

  /**
   * Triggers the player attack.
   * @return whether the input was processed
   * @see InputProcessor#touchDown(int, int, int, int)
   */
  @Override
  public boolean touchDown(int screenX, int screenY, int pointer, int button) {
    entity.getEvents().trigger("attack");
    screenY = (int) (Gdx.graphics.getHeight() - screenY);
    screenY = screenY / 90; // tilemap we can see is 15 by 2
    screenX = screenX / 128; // 90 pixels for each y tile and 128 for each x tile
    entity.getEvents().trigger("place", new Vector2(screenX, screenY));
    return true;
  }

  private void triggerWalkEvent() {
    if (walkDirection.epsilonEquals(Vector2.Zero)) {
      entity.getEvents().trigger("walkStop");
    } else {
      entity.getEvents().trigger("walk", walkDirection);
    }
  }

  @Override
  public boolean mouseMoved(int screenX, int screenY) {


    screenY = (int) (Gdx.graphics.getHeight() - screenY);
    screenY = screenY / (90);
    screenX = screenX / (128);
    entity.getEvents().trigger("placing", new Vector2(screenX, screenY));

    return true;
  }
}
