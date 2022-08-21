package com.deco2800.game.worker;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.components.tasks.MovementTask;
import com.deco2800.game.components.tasks.WaitTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerIdleTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WorkerIdleTask.class);

    private final float waitTime;
    private Vector2 startPos;
    private MovementTask movementTask;
    private WaitTask waitTask;
    private Task currentTask;

    /**
     * @param waitTime How long in seconds to wait.
     */
    public WorkerIdleTask(float waitTime) {
        this.waitTime = waitTime;
    }

    @Override
    public int getPriority() {
        return 1; // Low priority task
    }

    @Override
    public void start() {
        super.start();
        startPos = owner.getEntity().getPosition();

        waitTask = new WaitTask(waitTime);
        waitTask.create(owner);
        // Wait for MouseEvent before starting movement task
        movementTask = new MovementTask(getNewPosition());
        movementTask.create(owner);
        movementTask.start();
        currentTask = movementTask;

        this.owner.getEntity().getEvents().trigger("idleStart");
    }

    @Override
    public void update() {
        if (currentTask.getStatus() != Status.ACTIVE) {
            if (currentTask == movementTask) {
                startWaiting();
            } else {
                startMoving();
            }
        }
        currentTask.update();
    }

    private void startWaiting() {
        logger.debug("Starting waiting");
        swapTask(waitTask);
    }

    private void startMoving() {
        logger.debug("Starting moving");
        movementTask.setTarget(getNewPosition());
        swapTask(movementTask);
    }

    private void swapTask(Task newTask) {
        if (currentTask != null) {
            currentTask.stop();
        }
        currentTask = newTask;
        currentTask.start();
    }

    /**
     * Finds what new position the worker should move to based on the
     * coordinates of the MouseEvent
     * @return the new position vector
     */
    private Vector2 getNewPosition() {
        return new Vector2();
    }
}
