package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends BaseEntityStatsComponent {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private float attack;
  private float defence;
  private float speed;

  public CombatStatsComponent(float health, float attack, float defence, float speed) {
    super(health);
    setAttack(attack);
    setDefence(defence);
    setSpeed(speed);
  }

  /**
   * Returns the entity's attack damage.
   *
   * @return attack damage
   */
  public float getAttack() {
    return attack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setAttack(float attack) {
    if (attack >= 0) {
      this.attack = attack;
    } else {
      logger.error("Can not set attack to a negative attack value");
    }
  }

  /**
   * Returns the entity's defence ability
   *
   * @return defence ability
   */
  public float getDefence() {
    return defence;
  }

  /**
   * Sets the entity's defence ability, Defence ability has a minimum bound of 0.
   *
   * @param defence Defence ability
   */
  public void setDefence(float defence) {
    if (defence >= 0) {
      this.defence = defence;
    } else {
      logger.error("Can not set defence to a negative defence value");
    }
  }

  /**
   * Returns the entity's speed, this speed values applies for attacks and movement
   *
   * @return entity's speed
   */
  public float getSpeed() {
    return speed;
  }

  /**
   * Sets the entity's speed, Speed has a minimum bound of 0.
   *
   * @param speed Speed of the entity
   */
  public void setSpeed(float speed) {
    if (speed >= 0) {
      this.speed = speed;
    } else {
      logger.error("Can not set speed to a negative speed value");
    }
  }

  public void hit(CombatStatsComponent attacker) {
    float newHealth = getHealth() - attacker.getAttack();
    setHealth(newHealth);
  }
}
