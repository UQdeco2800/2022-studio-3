package com.deco2800.game.entities;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CameraComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Provides a global access point for entities to register themselves. This allows for iterating
 * over entities to perform updates each loop. All game entities should be registered here.
 *
 * Avoid adding additional state here! Global access is often the easy but incorrect answer to
 * sharing data.
 */
public class EntityService {
  private static final Logger logger = LoggerFactory.getLogger(EntityService.class);
  private static final int INITIAL_CAPACITY = 16;

  private final Array<Entity> entities = new Array<>(false, INITIAL_CAPACITY);

  /**
   * Register a new entity with the entity service. The entity will be created and start updating.
   *
   * @param entity new entity.
   */
  public void register(Entity entity) {
    logger.debug("Registering {} in entity service", entity);
    entities.add(entity);
    entity.create();
  }

  /**
   * Unregister an entity with the entity service. The entity will be removed and stop updating.
   *
   * @param entity entity to be removed.
   */
  public void unregister(Entity entity) {
    logger.debug("Unregistering {} in entity service", entity);
    entities.removeValue(entity, true);
  }

  /**
   * Update all registered entities. Should only be called from the main game loop.
   */
  public void update() {
    for (Entity entity : entities) {
      entity.earlyUpdate();
      entity.update();
    }
  }

  /**
   * Dispose all entities.
   */
  public void dispose() {
    for (Entity entity : entities) {
      entity.dispose();
    }
  }

  /**
   * Finds the game camera which is registered with an entity in the EntityService, and returns it.
   * Otherwise, returns null if no such entity exists.
   *
   * @return the game camera
   */
  public Camera getCamera() {
    for (Entity entity : entities) {
      if (entity.getComponent(CameraComponent.class) != null) {
        CameraComponent cameraComponent = entity.getComponent(CameraComponent.class);
        return cameraComponent.getCamera();
      }
    }
    return null;
  }

  public Array<Entity> getEntities() {
    return this.entities;
  }


  public void trigger(String event, float factor) {
    for (int i = 0; i < entities.size; i++) {
      entities.get(i).getEvents().trigger(event, factor);
    }
  }
}

