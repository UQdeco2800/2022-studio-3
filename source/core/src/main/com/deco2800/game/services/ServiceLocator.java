package com.deco2800.game.services;

import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.GameAreaEventService;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.input.InputService;
import com.deco2800.game.map.MapService;
import com.deco2800.game.areas.MapGenerator.FloodingGenerator;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A simplified implementation of the Service Locator pattern:
 * https://martinfowler.com/articles/injection.html#UsingAServiceLocator
 *
 * <p>Allows global access to a few core game services.
 * Warning: global access is a trap and should be used <i>extremely</i> sparingly.
 * Read the wiki for details (https://github.com/UQdeco2800/game-engine/wiki/Service-Locator).
 */
public class ServiceLocator {
  private static final Logger logger = LoggerFactory.getLogger(ServiceLocator.class);
  private static EntityService entityService;
  private static RenderService renderService;
  private static PhysicsService physicsService;
  private static GameTime timeSource;
  private static InputService inputService;
  private static ResourceService resourceService;
  private static MapService mapService;
  private static GameAreaEventService gameAreaEventService;
  public static AtlantisGameArea gameArea;

  public static EntityService getEntityService() {
    return entityService;
  }
  public static MapService getMapService() {
    return mapService;
  }
  
  public static RenderService getRenderService() {
    return renderService;
  }

  public static PhysicsService getPhysicsService() {
    return physicsService;
  }

  public static GameTime getTimeSource() {
    return timeSource;
  }

  public static InputService getInputService() {
    return inputService;
  }

  public static ResourceService getResourceService() {
    return resourceService;
  }

  public static AtlantisGameArea getGameArea(){
    return gameArea;
  }

  public static GameAreaEventService getGameAreaEventService() {
    return gameAreaEventService;
  }

  public static void registerMapService(MapService service) {
    logger.debug("Registering map service {}", service);
    mapService = service;
  }

  public static void registerGameAreaEventService(GameAreaEventService service) {
    logger.debug("Registering game area event service {}", service);
    gameAreaEventService = service;
  }
  public static void registerEntityService(EntityService service) {
    logger.debug("Registering entity service {}", service);
    entityService = service;
  }

  public static void registerRenderService(RenderService service) {
    logger.debug("Registering render service {}", service);
    renderService = service;
  }

  public static void registerPhysicsService(PhysicsService service) {
    logger.debug("Registering physics service {}", service);
    physicsService = service;
  }

  public static void registerTimeSource(GameTime source) {
    logger.debug("Registering time source {}", source);
    timeSource = source;
  }

  public static void registerInputService(InputService source) {
    logger.debug("Registering input service {}", source);
    inputService = source;
  }

  public static void registerResourceService(ResourceService source) {
    logger.debug("Registering resource service {}", source);
    resourceService = source;
  }

  public static void registerGameArea(AtlantisGameArea source){
    gameArea = source;
  }

  public static void clear() {
    entityService = null;
    renderService = null;
    physicsService = null;
    timeSource = null;
    inputService = null;
    resourceService = null;
    gameAreaEventService = null;
  }

  private ServiceLocator() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
