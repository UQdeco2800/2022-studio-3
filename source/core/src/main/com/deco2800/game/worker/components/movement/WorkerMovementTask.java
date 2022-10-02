package com.deco2800.game.worker.components.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.map.MapComponent;
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
    private Vector2 finalTarget;
    private float stopDistance = 0.01f;
    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;
    private List<GridPoint2> path;
    private MapService mapService;

    public WorkerMovementTask(Vector2 target) {
        this.target = target;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        this.mapService = ServiceLocator.getMapService();
        this.mapService.unregister(owner.getEntity().getComponent(MapComponent.class));
        this.path = mapService.getPath(MapService.worldToTile(owner.getEntity().getCenterPosition()), MapService.worldToTile(target));
        this.finalTarget = target;
        logger.info("Path from {} to {}: {}", MapService.worldToTile(owner.getEntity().getCenterPosition()), MapService.worldToTile(target), path);
        if (!path.isEmpty()) {
            this.setTarget(MapService.tileToWorldPosition(path.get(0)));
            path.remove(0);
            movementComponent.setMoving(true);
            logger.info("Starting movement towards {}", MapService.worldToTile(target));
            lastTimeMoved = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
            owner.getEntity().getEvents().addListener("changeWeather", this::changeSpeed);
        } else {
            this.setTarget(target);
            movementComponent.setMoving(true);
            logger.info("NO PATH FOUND USING DEFAULT MOVEMENT Starting movement towards {}", MapService.worldToTile(target));
        }    
    }

    public void changeSpeed(float factor) {
        this.movementComponent.changeSpeed(factor);
    }

    @Override
    public void update() {
        if (isAtTarget()) {
            if (path.isEmpty()) {
                movementComponent.setMoving(false);
                status = Status.FINISHED;
                logger.info("Finished path");
            } else {
                setTarget(MapService.tileToWorldPosition(path.get(0)));
                path.remove(0);
                movementComponent.setMoving(true);
                logger.info("Moving to the next target: {}", MapService.worldToTile(target));
                lastTimeMoved = gameTime.getTime();
                lastPos = owner.getEntity().getPosition();
            }
        } else {
            //logger.info("Failed to move to {}", MapService.worldToTile(target));
            if (gameTime.getTime() - lastTimeMoved > 1000) {
                if (lastPos.epsilonEquals(owner.getEntity().getPosition(), 0.01f)) {
                    logger.info("Stuck, moving to final target");
                    setTarget(finalTarget);
                    movementComponent.setMoving(true);
                    path.clear();
                }
                lastTimeMoved = gameTime.getTime();
                lastPos = owner.getEntity().getPosition();
            }
        }
    }

    public void setTarget(Vector2 target) {
        this.target = target;
        movementComponent.setTarget(target);
    }
    
    public void setPath(List<GridPoint2> path) {
        this.path = path;
    }

    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.debug("Stopping movement");
    }

    private boolean isAtTarget() {
        return owner.getEntity().getPosition().dst(target) <= stopDistance;
    }

    private void checkIfStuck() {
        if (didMove()) {
            lastTimeMoved = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
        } else if (gameTime.getTimeSince(lastTimeMoved) > 500L) {
            movementComponent.setMoving(false);
            status = Status.FAILED;
            logger.info("Got stuck! Failing movement task");
            //movementComponent.setTarget(finalTarget);
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
