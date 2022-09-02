package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.ai.tasks.TaskRunner;
import org.slf4j.LoggerFactory;

import java.util.logging.Logger;

public class EnemyMovement extends DefaultTask implements PriorityTask {

  private Vector2 destinationPoint;
  private Vector2 startPos;

  private MovementTask movementTask;

  private Task currentTask;

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
    setMovementTask(new MovementTask(new Vector2(10f, 4f)));
    movementTask.create(owner);
    movementTask.start();

    setCurrentTask(movementTask);

    this.owner.getEntity().getEvents().trigger("enemy-movement");
  }

  /**
   * Run one frame of the task. Similar to the update() in Components.
   */
  @Override
  public void update() {

  }

  /**
   * Stop the task immediately. This can be called at any time by the AI controller.
   */
  @Override
  public void stop() {

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
}
