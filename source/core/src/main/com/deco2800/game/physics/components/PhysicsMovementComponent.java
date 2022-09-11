package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.EntityDirection;
import com.deco2800.game.components.EntityDirectionComponent;
import com.deco2800.game.utils.math.Vector2Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Movement controller for a physics-based entity. */
public class PhysicsMovementComponent extends Component implements MovementController {
  private static final Logger logger = LoggerFactory.getLogger(PhysicsMovementComponent.class);
  private Vector2 maxSpeed = Vector2Utils.ONE;
  private PhysicsComponent physicsComponent;
  private Vector2 targetPosition;
  private boolean movementEnabled = true;
  private boolean stop = false;
  private String previousDirection = null;

  private EntityDirectionComponent entityDirectionComponent;


  /**
   * Initialising the PhysicsMovementComponent by default.
   */
  public PhysicsMovementComponent() {
  }

  /**
   * Creat a entity that have a base speed.
   * @param speed the moving speed
   */
  public PhysicsMovementComponent(Vector2 speed) {
    maxSpeed = speed;
  }

  public void faceWest() {
    EntityDirection previousDirection = entityDirectionComponent.getEntityDirection();

    switch (previousDirection) {
      case EAST:
      case SOUTH:
      case NORTH:
      case DEFAULT:
        entityDirectionComponent.setDirectionWest();
        this.getEntity().getEvents().trigger("goWest");
        break;
    }
  }

  public void faceEast() {
    EntityDirection previousDirection = entityDirectionComponent.getEntityDirection();
    switch (previousDirection) {
      case WEST:
      case SOUTH:
      case NORTH:
      case DEFAULT:
        entityDirectionComponent.setDirectionEast();
        this.getEntity().getEvents().trigger("goEast");
        break;
    }
  }

  public void faceNorth() {
    EntityDirection previousDirection = entityDirectionComponent.getEntityDirection();
    switch (previousDirection) {
      case WEST:
      case EAST:
      case SOUTH:
      case DEFAULT:
        entityDirectionComponent.setDirectionNorth();
        this.getEntity().getEvents().trigger("goNorth");
        break;
    }
  }

  public void faceSouth() {
    EntityDirection previousDirection = entityDirectionComponent.getEntityDirection();
    switch (previousDirection) {
      case WEST:
      case EAST:
      case NORTH:
      case DEFAULT:
        entityDirectionComponent.setDirectionSouth();
        this.getEntity().getEvents().trigger("goSouth");
        break;
    }
  }

  public void changeAnimation() {
    if (Boolean.FALSE.equals(this.getEntity().getComponent(CombatStatsComponent.class).isDead())) {
      if (Math.abs(this.getDirection().x) > Math.abs(this.getDirection().y)) {
        if (this.getDirection().x < 0) {
          faceWest();
        } else if (this.getDirection().x > 0) {
          faceEast();
        }
      } else {
        if (this.getDirection().y > 0) {
          faceNorth();
        } else if (this.getDirection().y < 0) {
          faceSouth();
        }
      }
    }
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
    entityDirectionComponent = entity.getComponent(EntityDirectionComponent.class);
  }

  @Override
  public void update() {
    if (movementEnabled && targetPosition != null) {
      Body body = physicsComponent.getBody();
      updateDirection(body);
    }
  }

  /**
   * Enable/disable movement for the controller. Disabling will immediately set velocity to 0.
   *
   * @param movementEnabled true to enable movement, false otherwise
   */
  @Override
  public void setMoving(boolean movementEnabled) {
    this.movementEnabled = movementEnabled;
    if (!movementEnabled) {
      Body body = physicsComponent.getBody();
      setToVelocity(body, Vector2.Zero);
      if (previousDirection != null && !stop) {
        this.getEntity().getEvents().trigger("default");
        stop = true;
      } else {
        stop = false;
      }
    }
  }

  @Override
  public boolean getMoving() {
    return movementEnabled;
  }

  /** @return Target position in the world */
  @Override
  public Vector2 getTarget() {
    return targetPosition;
  }

  /**
   * Set a target to move towards. The entity will be steered towards it in a straight line, not
   * using pathfinding or avoiding other entities.
   *
   * @param target target position
   */
  @Override
  public void setTarget(Vector2 target) {
    logger.trace("Setting target to {}", target);
    this.targetPosition = target;
  }

  private void updateDirection(Body body) {
    Vector2 desiredVelocity = getDirection().scl(maxSpeed);
    setToVelocity(body, desiredVelocity);
    changeAnimation();
  }

  private void setToVelocity(Body body, Vector2 desiredVelocity) {
    // impulse force = (desired velocity - current velocity) * mass
    Vector2 velocity = body.getLinearVelocity();
    Vector2 impulse = desiredVelocity.cpy().sub(velocity).scl(body.getMass());
    body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
  }

  private Vector2 getDirection() {
    // Move towards targetPosition based on our current position
    return targetPosition.cpy().sub(entity.getPosition()).nor();
  }
}
