package com.deco2800.game.worker.components.movement;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorkerIdleTask extends DefaultTask implements PriorityTask {
    private static final Logger logger = LoggerFactory.getLogger(WorkerIdleTask.class);

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
        movementTask.update();
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
    }
}
