package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static java.lang.Math.max;

/**
 * Component used to store information related to combat such as health, attack, etc. Any entities
 * which engage it combat should have an instance of this class registered. This class can be
 * extended for more specific combat needs.
 */
public class CombatStatsComponent extends Component {

  private static final Logger logger = LoggerFactory.getLogger(CombatStatsComponent.class);
  private int troops;
  private int health;
  private int maxHealth;
  private int baseAttack;
  private int baseDefence;
  private float landSpeed;
  private int range;

  public CombatStatsComponent(int health, int baseAttack, int baseDefence) {
    this(0, health, baseAttack, baseDefence, 0f);
  }

  public CombatStatsComponent(int troops, int health, int baseAttack, int baseDefence, float landSpeed) {
    this(troops, health, baseAttack, baseDefence, landSpeed, 0);
  }

  public CombatStatsComponent(int troops, int health, int baseAttack, int baseDefence, float landSpeed, int range) {
    setTroops(troops);
    setMaxHealth(health);
    setHealth(health);
    setBaseAttack(baseAttack);
    setBaseDefence(baseDefence);
    setLandSpeed(landSpeed);
    setRange(range);
  }

  /**
   * Returns the attack range for unit
   *
   * @return entity's attack range
   */
  public int getRange() {
    return range;
  }

  /**
   * Sets the attack range for a unit
   *
   * @param range attack for unit
   */
  public void setRange(int range) {
    if (range >= 0) {
      this.range = range;
    } else {
      logger.error("Can not set attack range to a negative range value");
    }
  }

  /**
   * Returns number of troops the unit has
   *
   * @return entity's troop count
   */
  public int getTroops() {
    return troops;
  }

  /**
   * Sets the number of troops the unit has
   *
   * @param troops number of troops to set.
   */
  public void setTroops(int troops) {
    if (troops >= 0) {
      this.troops = troops;
    } else {
      logger.error("Can not set no. of troops to a negative value");
    }
  }

  /**
   * Returns the land movement speed of the unit
   *
   * @return land movement speed of unit
   */
  public float getLandSpeed() {
    return landSpeed;
  }

  /**
   * Sets the land movement speed of unit
   *
   * @param landSpeed land movement speed to set
   */
  public void setLandSpeed(float landSpeed) {
    if (landSpeed >= 0f) {
      this.landSpeed = landSpeed;
    } else {
      logger.error("Can not set land speed of troops to a negative value");
    }
  }

  /**
   * Returns true if the entity's has 0 health, otherwise false.
   *
   * @return is player dead
   */
  public Boolean isDead() {
    return health == 0;
  }

  public Boolean isMaxHealth() {
    return health == maxHealth;
  }

  /**
   * Returns the entity's health.
   *
   * @return entity's health
   */
  public int getHealth() {
    return health;
  }

  /**
   * Get's the entity's maximum health.
   * @return entity's maximum health
   */
  public int getMaxHealth() {
    return maxHealth;
  }

  /**
   * Sets the entity's health. Health has a minimum bound of 0.
   *
   * @param health health
   */
  public void setHealth(int health) {
    if (health >= 0) {
      this.health = health;
      if (health > maxHealth) {
        this.health = maxHealth;
      }
    } else {
      this.health = 0;
    }
    if (entity != null) {
      entity.getEvents().trigger("updateHealth", this.health);
    }
  }

  /**
   * Sets the entity's maximum health. Maximum Health must be greater than or equal to 0.
   * @param maxHealth Maximum health an entity can have
   */
  public void setMaxHealth(int maxHealth) {
    this.maxHealth = maxHealth;
    if (this.maxHealth < 0) {
      this.maxHealth = 0;
    }
  }

  /**
   * Adds to the player's health. The amount added can be negative.
   *
   * @param health health to add
   */
  public void addHealth(int health) {
    setHealth(this.health + health);
  }

  /**
   * Returns the entity's base attack damage.
   *
   * @return base attack damage
   */
  public int getBaseAttack() {
    return baseAttack;
  }

  /**
   * Sets the entity's attack damage. Attack damage has a minimum bound of 0.
   *
   * @param attack Attack damage
   */
  public void setBaseAttack(int attack) {
    if (attack >= 0) {
      this.baseAttack = attack;
    } else {
      logger.error("Can not set base attack to a negative attack value");
    }
  }

  /**
   * Sets the entity's defence from attacks. Defence has a minimum bound of 0.
   *
   * @param defence Attack defence
   */
  public void setBaseDefence(int defence) {
    if (defence >= 0) {
      this.baseDefence = defence;
    } else {
      /* note: this may be valid in the final build, but for now I want
       * to leave it as is.
       */
      logger.error("Can not set base defence to a negative defence value");
    }
  }

  /**
   * Returns the entity's base defence.
   *
   * @return base defence
   */
  public int getBaseDefence() {return baseDefence;}

  // TODO: Substitute dummy damage function with more complete function
  /**
   * Manages the entity being hit by an attacking enemy.
   *
   * Combatant takes damage equal to the attacker's attack less this entity's
   * defence, with a minimum bound of 1.
   *
   * @param attacker The Combat Stats of an attacking entity
   */
  public void hit(CombatStatsComponent attacker) {
    // Guarantee that at least one damage is done
    int newHealth = getHealth() - max(1,
            attacker.getBaseAttack() - getBaseDefence());
    setHealth(newHealth);
  }


}
