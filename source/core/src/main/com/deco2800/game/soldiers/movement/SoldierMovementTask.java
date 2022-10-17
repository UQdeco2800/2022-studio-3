package com.deco2800.game.soldiers.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SoldierMovementTask extends DefaultTask{
    private static final Logger logger = LoggerFactory.getLogger(SoldierMovementTask.class);

    private final GameTime gameTime;
    private Vector2 target;
    private float stopDistance = 0.01f;
    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;

    public SoldierMovementTask(Vector2 target) {
        this.target = target;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        movementComponent.setTarget(target);
        movementComponent.setMoving(true);
        logger.debug("Starting movement towards {}", target);
        lastTimeMoved = gameTime.getTime();
        lastPos = owner.getEntity().getPosition();
    }

    @Override
    public void update() {
        if (isAtTarget()) {
            movementComponent.setMoving(false);
            status = Status.FINISHED;
            logger.debug("Finished moving to {}", target);
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
