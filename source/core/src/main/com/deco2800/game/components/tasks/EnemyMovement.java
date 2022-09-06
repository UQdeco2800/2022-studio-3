package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.RandomPointGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;

import java.util.HashMap;

public class EnemyMovement extends DefaultTask implements PriorityTask {

  private Vector2 destinationPoint;
  private Vector2 startPos;

  private MovementTask movementTask;

  private Task currentTask;

  private GridPoint2 target;

  AtlantisTerrainFactory terrainFactory;
  private boolean inMotion = true;

  private TerrainComponent terrain;

  private final GameTime gameTime = ServiceLocator.getTimeSource();
  private long lastTime = gameTime.getTime();
  private Vector2 avoidanceTarget;
  private Vector2 lastPosition;

  private float angle;
  private MovementState movementStatus;
  public EnemyMovement(AtlantisTerrainFactory terrainFactory) {
    this.target = RandomPointGenerator.getCityCenter(terrainFactory);
    this.terrainFactory = terrainFactory;
    terrain = terrainFactory.createAtlantisTerrainComponent();
  }

  /**
   * @return 
   */
  @Override
  public int getPriority() {
    return 1;
  }

  /**
   * Start running this task. This will usually be called by an AI controller.
   */
  @Override
  public void start() {
    super.start();
    setStartPos(owner.getEntity().getPosition());
    startPos = this.owner.getEntity().getCenterPosition();
    destinationPoint = terrain.tileToWorldPosition(RandomPointGenerator.getCityCenter(terrainFactory));
    setMovementTask(new MovementTask(destinationPoint, 3.5f));
    movementTask.create(owner);
    movementTask.start();
    lastPosition = startPos;
    setCurrentTask(movementTask);
    angle = (float)Vector2Utils.angleTo(destinationPoint);
    movementStatus = MovementState.MOVING;
    this.owner.getEntity().getEvents().trigger("move-west");

  }

  private void isStuck() {

  }

  /**
   * Run one frame of the task. Similar to the update() in Components.
   */
  @Override
  public void update() {
    Vector2 position = this.owner.getEntity().getCenterPosition();
    MapGenerator mg = terrainFactory.getMapGenerator();
    float tileSize = terrain.getTileSize();
    if (gameTime.getTimeSince(lastTime) >= 500L) {
      lastPosition = position;
    }
    if (currentTask == movementTask) {
      if (!(position.dst2(lastPosition) > 0.001f)) {
        if (position.dst(destinationPoint) <= 2f) {
          currentTask.stop();
          this.owner.getEntity().getEvents().trigger("idle");
          System.out.println("Its reached the target!");
        }
        System.out.println("Its not moving");
      } else {
        System.out.println("Its moving");
      }
//      switch (movementStatus) {
//        case STUCK:
//          currentTask.stop();
//          movementStatus = MovementState.AVOIDING;
//          break;
//        case MOVING:
//          if (position.dst2(lastPosition) > 0.001f) {
//            if (!((MovementTask)currentTask).getTarget().equals(destinationPoint)) {
//              ((MovementTask) currentTask).setTarget(destinationPoint);
//            }
//            if (position.dst(destinationPoint) <= 2f) {
//              movementStatus = MovementState.REACHED;
//            }
//          } else {
//            movementStatus = MovementState.STUCK;
//          }
//          break;
//        case REACHED:
//          currentTask.stop();
//          break;
//        case AVOIDING:
//          avoidanceTarget = new Vector2(lastPosition.x + tileSize, lastPosition.y + tileSize);
//          ((MovementTask)currentTask).setTarget(avoidanceTarget);
//          currentTask.start();
//          movementStatus = MovementState.MOVING;
//          break;
//      }
    }
//    if (currentTask == movementTask) {
//      if (((MovementTask)currentTask).getTarget().equals(destinationPoint)) {
//        if (position.dst(destinationPoint) <= 2f) {
//          currentTask.stop();
//          this.owner.getEntity().getEvents().trigger("idle");
//        }
//      }
//    }

    switch (currentTask.getStatus()) {
      case FINISHED:
        System.out.println("I'm at target!!");
        // Start attacking target
        break;
      case FAILED:
        System.out.println("I'm stuck!!");
        // Stop movement task
        // change target
        break;
      case ACTIVE:
        break;
      case INACTIVE:
        break;
    }

  }

  /**
   * Stop the task immediately. This can be called at any time by the AI controller.
   */
  @Override
  public void stop() {
    super.stop();
  }

  public Vector2 getDestinationPoint() {
    return destinationPoint;
  }

  public void setDestinationPoint(Vector2 destinationPoint) {
    this.destinationPoint = destinationPoint;
  }

  public Vector2 getStartPos() {
    return startPos;
  }

  public void setStartPos(Vector2 startPos) {
    this.startPos = startPos;
  }

  public void setMovementTask(MovementTask movementTask) {
    this.movementTask = movementTask;
  }

  public void setCurrentTask(Task currentTask) {
    this.currentTask = currentTask;
  }

  private enum MovementState {
    STUCK,
    MOVING,
    REACHED,
    AVOIDING
  }
}
