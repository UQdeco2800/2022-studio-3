package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.ai.tasks.TaskRunner;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.RandomPointGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.map.util.OccupiedTileException;
import com.deco2800.game.map.util.OutOfBoundsException;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.Vector2Utils;
import com.deco2800.game.utils.random.Timer;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.logging.Logger;

/**
 * AI task to handle enemy movement
 */
public class EnemyMovement extends DefaultTask implements PriorityTask {
  /**
   * Global access to destination point for a given movement task.
   */
  private Vector2 destinationPoint;
  /**
   * Point from which a movement task started from.
   */
  private Vector2 startPos;
  /**
   * Initial starting position for bounding the instance.
   */
  private Vector2 initStartPos;
  /**
   * Instant of a movementTask
   */
  private MovementTask movementTask;
  /**
   * Current movement task.
   */
  private Task currentTask;
  /**
   * Target grid point.
   */
  private GridPoint2 target;
  /**
   * Reference to the terrain factory.
   */
  AtlantisTerrainFactory terrainFactory;
  /**
   * Terrain component.
   */
  private TerrainComponent terrain;
  /**
   * Boolean value representing whether an enemy is locked onto a target.
   */
  private Boolean targetLock;

  /**
   * Timer used to udpate ai movement.
   */
  private Timer timer;

  /**
   * Creates an EnemyMovement instance.
   * @param terrainFactory The terrain factory representing the game area.
   */
  public EnemyMovement(AtlantisTerrainFactory terrainFactory) {
    this.terrainFactory = terrainFactory;
    this.terrain = terrainFactory.createAtlantisTerrainComponent();
    this.timer = new Timer(50000, 100000);
  }

  /**
   * Start running this task. This will usually be called by an AI controller.
   */
  @Override
  public void start() {
    super.start();
    this.initStartPos = this.owner.getEntity().getCenterPosition();
    this.selectRandomTarget();
    this.createMovementTask();
  }

  /**
   * Run one frame of the task. Similar to the update() in Components.
   */
  @Override
  public void update() {
    if (this.targetLock == false) {
      if (isFriendlyUnitInVicinity()) {
        return;
      } else if (this.movementTask.isAtTarget()) {
        this.updateMovementTask();
      } else if (this.timer.isTimerExpired()) {
        this.updateMovementTask();
      }
    }
  }

  /**
   * Updates movement task by selecting a target, creating a movement object
   * and restarting the timer.
   */
  private void updateMovementTask() {
    this.currentTask.stop();
    selectRandomTarget();
    createMovementTask();
    this.timer = new Timer(5000, 10000);
  }

  /**
   * Returns True/False depending on if their is a friendly unit in LOS.
   * @return Boolean representing whether a friendly unit is within the LOS.
   */
  private Boolean isFriendlyUnitInVicinity() {
    return false;
  }

  /**
   * Returns priority of task
   * @return
   */
  @Override
  public int getPriority() {
    return 1;
  }


  private void swapTask(Task newTask) {
    if (currentTask != null) {
      currentTask.stop();
    }
    currentTask = newTask;
    currentTask.start();
  }

  /**
   * Stop the task immediately. This can be called at any time by the AI controller.
   */
  @Override
  public void stop() {
    super.stop();
  }

  /**
   * Returns the destination point of entity
   *
   * @return destination point of entity
   */
  public Vector2 getDestinationPoint() {
    return destinationPoint;
  }

  /**
   * Sets the destination point of entity
   *
   * @param destinationPoint Destination point of entity
   */
  public void setDestinationPoint(Vector2 destinationPoint) {
    this.destinationPoint = destinationPoint;
  }

  /**
   * Returns the starting position upon spawning of entity
   *
   * @return spawn point of entity
   */
  public Vector2 getStartPos() {
    return startPos;
  }

  /**
   * Sets the starting position of entity
   *
   * @param startPos spawn point of entity
   */
  public void setStartPos(Vector2 startPos) {
    this.startPos = startPos;
  }

  public void selectRandomTarget() {
    this.startPos = this.owner.getEntity().getCenterPosition();
    this.target = this.terrainFactory.randomlySelectTileToMoveTo(terrain.worldPositionToTile(this.initStartPos));
    this.targetLock = false;
    destinationPoint = terrain.tileToWorldPosition(this.target);
  }

  /**
   * Creates a movement task.
   */
  public void createMovementTask() {
    setMovementTask(new MovementTask(destinationPoint));
    this.movementTask.create(owner);
    this.movementTask.start();
    setCurrentTask(this.movementTask);
  }

  /**
   * Sets the movement task
   *
   * @param movementTask movement task to be set
   */
  public void setMovementTask(MovementTask movementTask) {
    this.movementTask = movementTask;
  }

  /**
   * Sets the current task
   *
   * @param currentTask cureent task to be set
   */
  public void setCurrentTask(Task currentTask) {
    this.currentTask = currentTask;
  }
}