package com.deco2800.game.components;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;

/**
 * Abstract component used to store information universal to every unit,
 * irrespective of whether it's a building unit, a worker unit or a combat
 * unit.
 * NOTE:
 * Health is not necessarily health points of a combat unit, it
 * can refer to:
 *    - structural integrity of buildings (both initial and remaining)
 *    - remaining productivity of mines, is it depleted or not or how much longer it depletes
 *    - etc..
 */

abstract class BaseEntityStatsComponent extends Component {
//  private static final Logger logger = LoggerFactory.getLogger(BaseEntityStatsComponent.class);

  private float health;

  protected BaseEntityStatsComponent(float baseHealth) {
    setHealth(baseHealth);
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(float health) {
    if (health >= 0) {
      this.health = health;
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public float getHealth() {
    return health;
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is entity alive
   */
  public boolean isDead() {
    return this.health == 0;
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(float health) {
    setHealth(this.health+health);
  }
}