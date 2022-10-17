package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * When this entity touches a valid enemy's hitbox, deal damage to them and apply a knockback.
 *
 * <p>Requires CombatStatsComponent, HitboxComponent on this entity.
 *
 * <p>Damage is only applied if target entity has a CombatStatsComponent. Knockback is only applied
 * if target entity has a PhysicsComponent.
 */
public class TouchAttackComponent extends Component {
  private short targetLayer;
  private float knockbackForce = 0f;
  private CombatStatsComponent combatStats;
  private HitboxComponent hitboxComponent;

  private EntityDirectionComponent entityDirectionComponent;
  private boolean isAttacking = false;
  private Entity targetEntity;

  /**
   * Create a component which attacks entities on collision, without knockback.
   * @param targetLayer The physics layer of the target's collider.
   */
  public TouchAttackComponent(short targetLayer) {
    this.targetLayer = targetLayer;
  }

  /**
   * Create a component which attacks entities on collision, with knockback.
   * @param targetLayer The physics layer of the target's collider.
   * @param knockback The magnitude of the knockback applied to the entity.
   */
  public TouchAttackComponent(short targetLayer, float knockback, int hitDamage) {
    this.targetLayer = targetLayer;
    this.knockbackForce = knockback;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("collisionStart", this::onCollisionStart);
    combatStats = entity.getComponent(CombatStatsComponent.class);
    hitboxComponent = entity.getComponent(HitboxComponent.class);
    entityDirectionComponent = entity.getComponent(EntityDirectionComponent.class);
  }

  private void onCollisionStart(Fixture me, Fixture other) {
    if (hitboxComponent.getFixture() != me) {
      // Not triggered by hitbox, ignore
      return;
    }

    if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
      // Doesn't match our target layer, ignore
      return;
    }

    // Try to attack target.
    Entity source = ((BodyUserData) me.getBody().getUserData()).entity;
    Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
    CombatStatsComponent targetStats = target.getComponent(CombatStatsComponent.class);

    if (targetStats != null) {
      this.targetEntity = target;
      targetStats.hit(combatStats);
      if (targetStats.getHealth() > 0) {
        if (!isAttacking) {
          isAttacking = true;
          if (entityDirectionComponent != null) {
            switch (entityDirectionComponent.getEntityDirection()) {
              case DEFAULT:
                break;
              case WEST:
                source.getEvents().trigger("attackWest");
                target.getEvents().trigger("attackEast");
                break;
              case EAST:
                source.getEvents().trigger("attackEast");
                target.getEvents().trigger("attackWest");
                break;
              case NORTH:
                source.getEvents().trigger("attackNorth");
                target.getEvents().trigger("attackSouth");
                break;
              case SOUTH:
                source.getEvents().trigger("attackSouth");
                target.getEvents().trigger("attackNorth");
                break;
            }
          }
        }
      }else{
        isAttacking = false;
      }
    }

//     Apply knockback
    PhysicsComponent physicsComponent = target.getComponent(PhysicsComponent.class);

    if (physicsComponent != null && knockbackForce > 0f) {
      Body targetBody = physicsComponent.getBody();
      Vector2 direction = target.getCenterPosition().sub(entity.getCenterPosition());
      Vector2 impulse = direction.setLength(knockbackForce);
      targetBody.applyLinearImpulse(impulse, targetBody.getWorldCenter(), true);
    }
  }

}
