package com.deco2800.game.worker.components.movement;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default state of the worker when they have nothing to do. Waits until a movement task is triggered.
 */
public class WorkerIdleTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WorkerIdleTask.class);
    private boolean idling = false;
    private WorkerMovementTask movementTask;
    private Vector2 startPos;

    public WorkerIdleTask() {}

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();
        movementTask = new WorkerMovementTask(startPos);
        movementTask.create(owner);
        movementTask.start();
        owner.getEntity().getEvents().addListener("workerWalk", this::startMoving);
    }

    @Override
    public void update() {
        if (movementTask.isMoving()) {
            movementTask.update();
        } else if (!idling) {
            // Start idling again
            owner.getEntity().getEvents().trigger("workerIdleAnimate");
            idling = true;
        }
    }

    /**
     * Moves the worker to the target
     *
     * @param target target of the worker
     */
    public void startMoving(Vector2 target) {
        logger.debug("Starting moving to {}", target);
        movementTask.stop();
        movementTask.setTarget(target);
        movementTask.start();
        // Trigger movement animation
        owner.getEntity().getEvents().trigger("workerWalkAnimate");
        idling = false;
    }

    /**
     * Return the current movement task
     *
     * @return the movement task
     */
    public WorkerMovementTask getMovementTask() {
        return this.movementTask;
    }

    /**
     * Return whether the worker is currently idling.
     *
     * @return True if the worker is stationary. False otherwise.
     */
    public boolean isIdling() {
        return this.idling;
    }
}
