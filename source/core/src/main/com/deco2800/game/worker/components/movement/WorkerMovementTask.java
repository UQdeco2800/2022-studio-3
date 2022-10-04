package com.deco2800.game.worker.components.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a movement task for a Worker. A worker moves to a specified target until they arrive, or
 * get stuck.
 */
public class WorkerMovementTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(WorkerMovementTask.class);

    private final GameTime gameTime;
    private Vector2 target;
    private float stopDistance = 0.01f;
    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;
    private List<GridPoint2> path;

    public WorkerMovementTask(Vector2 target) {
        this.target = target;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        // MapService ms = ServiceLocator.getMapService();
        // path = ms.getPath(MapService.worldToTile(owner.getEntity().getCenterPosition()), MapService.worldToTile(target));
        // movementComponent.setTarget(ms.tileToWorldPosition(path.get(1)));
        // path.remove(0);
        // path.remove(1);
        movementComponent.setTarget(target);
        movementComponent.setMoving(true);
        logger.debug("Starting movement towards {}", target);
        lastTimeMoved = gameTime.getTime();
        lastPos = owner.getEntity().getPosition();
        owner.getEntity().getEvents().addListener("changeWeather", this::changeSpeed);
    }

    public void changeSpeed(float factor) {
        this.movementComponent.changeSpeed(factor);
    }

    @Override
    public void update() {
        if (isAtTarget()) {
            movementComponent.setMoving(false);
            status = Status.FINISHED;
            logger.debug("Finished moving to {}", target);
        // if (path.size() == 0) {
        //     movementComponent.setMoving(false);
        //     status = Status.FINISHED;
        //     logger.debug("Finished moving to {}", target);
        // } else if (isAtTarget()) {
        //     MapService ms = ServiceLocator.getMapService();
        //     movementComponent.setTarget(ms.tileToWorldPosition(path.get(0)));
        //     path.remove(0);
        } else {
            checkIfStuck();
        }
    }

    public void setTarget(Vector2 target) {
        this.target = target;
        movementComponent.setTarget(target);
    }

    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.debug("Stopping movement");
    }

    public boolean isAtTarget() {
        return owner.getEntity().getPosition().dst(target) <= stopDistance;
    }

    private void checkIfStuck() {
        if (didMove()) {
            lastTimeMoved = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
        } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
            movementComponent.setMoving(false);
            status = Status.FAILED;
            logger.debug("Got stuck! Failing movement task");
        }
    }

    public boolean isMoving() {
        return movementComponent.getMoving();
    }

    public void setStopDistance(float stopDistance) {
        this.stopDistance = stopDistance;
    }

    private boolean didMove() {
        return owner.getEntity().getPosition().dst2(lastPos) > 0.001f;
    }
}
