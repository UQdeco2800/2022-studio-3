package com.deco2800.game.soldiers.movement;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.components.enemy.EnemySignal;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.utils.Array;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implements a fighting task for a Soldier. A Soldier may chase down an enemy or 
 * moves to a specified target until they arrive.
 * 
 * Attack Strategy for Soldier Units
 *  - User selects a target enemy for the soldier and the soldier will follow the enemy.
 *  - If what the user selects is not an enemy, it will move to the clicked location.
 */
public class FightingMovementTask extends DefaultTask {
    private static final Logger logger = LoggerFactory.getLogger(FightingMovementTask.class);

    private final GameTime gameTime;
    private boolean isFollowing;
    private Vector2 target;
    private Entity targetEnemy;
    private Vector2 targetCurrentPosition;
    private Vector2 finalTarget;
    private float stopDistance = 0.01f;
    private long lastTimeMoved;
    private Vector2 lastPos;
    private PhysicsMovementComponent movementComponent;
    private List<GridPoint2> path;
    private MapService mapService;

    public FightingMovementTask(Vector2 target) {
        this.target = target;
        this.targetEnemy = null;
        this.gameTime = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        this.movementComponent = owner.getEntity().getComponent(PhysicsMovementComponent.class);
        this.mapService = ServiceLocator.getMapService();
        this.mapService.unregister(owner.getEntity().getComponent(MapComponent.class));
    
        // If there is an enemy to chase
        if (this.checkIfEnemy()) {
            logger.info("Enemy targeted");
            this.targetCurrentPosition = this.targetEnemy.getPosition();
            this.finalTarget = this.targetCurrentPosition;
            this.isFollowing = true;
            this.setTarget(this.targetCurrentPosition);
        // If there is no enemy to chase
        } else {
            logger.info("No enemy found");
            this.finalTarget = this.target;
            this.isFollowing = false;
            this.path = mapService.getPath(MapService.worldToTile(owner.getEntity().getCenterPosition()), MapService.worldToTile(target));
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
            
    }

    @Override
    public void update() {
        if (this.isFollowing) {
            if (this.targetEnemy != null) {
                this.targetCurrentPosition = this.targetEnemy.getCenterPosition();
                this.setTarget(this.targetCurrentPosition);
            } else {
                this.isFollowing = false;
            }              
        }
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
            checkIfStuck();
        }
    }

    @Override
    public void stop() {
        super.stop();
        movementComponent.setMoving(false);
        logger.debug("Stopping movement");
    }

    public void checkIfStuck() {
        if (gameTime.getTime() - lastTimeMoved > 1000) {
            if (lastPos.epsilonEquals(owner.getEntity().getPosition(), 0.01f)) {
                logger.info("Stuck, moving to final target");
                PhysicsComponent physicsComponent = owner.getEntity().getComponent(PhysicsComponent.class);
                if (physicsComponent != null) {
                    Body entityBody = physicsComponent.getBody();
                    Vector2 direction = owner.getEntity().getCenterPosition().sub(owner.getEntity().getCenterPosition());
                    Vector2 impulse = direction.setLength(0.2f);
                    entityBody.applyLinearImpulse(impulse, entityBody.getWorldCenter(), true);
                }
                setTarget(finalTarget);
                movementComponent.setMoving(true);
                path.clear();
            }
            lastTimeMoved = gameTime.getTime();
            lastPos = owner.getEntity().getPosition();
        }
    }

    public void setTarget(Vector2 target) {
        this.target = target;
        movementComponent.setTarget(target);
    }

    public boolean checkIfEnemy() {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (Entity entity : entities) {
            if (entity.getCenterPosition().epsilonEquals(this.target, 1f) && entity.getComponent(EnemySignal.class) != null) {
                this.targetEnemy = entity;
                return true;
            }
        }
        return false;
    }

    public void changeSpeed(float factor) {
        this.movementComponent.changeSpeed(factor);
    }

    private boolean isAtTarget() {
        return owner.getEntity().getPosition().dst(target) <= stopDistance;
    }

    public boolean isMoving() {
        return movementComponent.getMoving();
    }

    public void setStopDistance(float stopDistance) {
        this.stopDistance = stopDistance;
    }
}
