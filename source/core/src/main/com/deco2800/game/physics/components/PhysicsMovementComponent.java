package com.deco2800.game.physics.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.ai.movement.MovementController;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
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

  private String previousDirection = null;
  private boolean stopped = false;



  /**
   * Initialising the PhysicsMovementComponent by default.
   */
  public PhysicsMovementComponent() {}

  /**
   * Creat a entity that have a base speed.
   * @param speed the moving speed
   */
  public PhysicsMovementComponent(Vector2 speed) {
    maxSpeed = speed;
  }

  public void faceLeft() {
    previousDirection = "left";
    this.getEntity().getEvents().trigger("goLeft");
  }

  public void faceRight() {
    previousDirection = "right";
    this.getEntity().getEvents().trigger("goRight");
  }

  public void faceUp() {
    previousDirection = "up";
    this.getEntity().getEvents().trigger("goUp");
  }

  public void faceDown() {
    previousDirection = "down";
    this.getEntity().getEvents().trigger("goDown");
  }

  public void changeAnimation() {
    if (Boolean.FALSE.equals(this.getEntity().getComponent(CombatStatsComponent.class).isDead())) {
      if (Math.abs(this.getDirection().x) > Math.abs(this.getDirection().y)) {
        if (this.getDirection().x < 0) {
          faceLeft();
        } else if (this.getDirection().x > 0) {
          faceRight();
        }
      } else {
        if (this.getDirection().y > 0) {
          faceUp();
        } else if (this.getDirection().y < 0) {
          faceDown();
        }
      }
    }
  }

  @Override
  public void create() {
    physicsComponent = entity.getComponent(PhysicsComponent.class);
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
      if (previousDirection != null && !stopped) {
        switch (previousDirection) {
          case "up":
            this.getEntity().getEvents().trigger("stopUp");
            break;
          case "down":
            this.getEntity().getEvents().trigger("stopDown");
            break;
          case "left":
            this.getEntity().getEvents().trigger("stopLeft");
            break;
          case "right":
            this.getEntity().getEvents().trigger("stopRight");
            break;
        }
        stopped = true;
      } else {
        stopped = false;
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
