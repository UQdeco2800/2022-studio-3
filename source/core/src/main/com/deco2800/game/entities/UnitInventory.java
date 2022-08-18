package com.deco2800.game.entities;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their unit's inventory.
 *
 * Currently only stores the stone and iron amount but can be extended for more 
 * advanced functionality such as storing other items.
 * Can also be used as a more generic component for other entities.
 */
public class UnitInventory extends Component{
  // idk what this does tbh
  private static final Logger logger = LoggerFactory.getLogger(UnitInventory.class);
  private int stone;
  private int iron;

  public UnitInventory(int stone, int iron) {
    this.stone = stone;
    this.iron = iron;
  }

  /**
   * Returns the amount of stone in the inventory
   *
   * @return amount of the stone
   */
  public int getStone() {
    return this.stone;
  }

  /**
   * Returns the amount of iron in the inventory
   *
   * @return amount of the iron
   */
  public int getIron() {
    return this.iron;
  }

  /**
   * Returns if the entity has a certain amount of stone.
   * @param stone required amount of stone
   * @return player has greater than or equal to the required amount of stone
   */
  public Boolean hasStone(int stone) {
    return this.stone >= stone;
  }

  /**
   * Returns if the entity has a certain amount of iron.
   * @param iron required amount of iron
   * @return player has greater than or equal to the required amount of iron
   */
  public Boolean hasIron(int iron) {
    return this.iron >= iron;
  }

  /**
   * Sets the player's stone. Stone has a minimum bound of 0.
   *
   * @param stone stone
   */
  public void setStone(int stone) {
    if (stone >= 0) {
      this.stone = stone;
    } else {
      this.stone = 0;
    }
    logger.debug("Setting stone to {}", this.stone);
  }

  /**
   * Sets the player's iron. Iron has a minimum bound of 0.
   *
   * @param iron iron
   */
  public void setIron(int iron) {
    if (iron >= 0) {
      this.iron = stone;
    } else {
      this.iron = 0;
    }
    logger.debug("Setting iron to {}", this.iron);
  }

  /**
   * Adds to the player's stone. The amount added can be negative.
   * @param stone stone to add
   */
  public void addStone(int stone) {
    setStone(this.stone + stone);
  }

  /**
   * Adds to the player's iron. The amount added can be negative.
   * @param iron iron to add
   */
  public void addIron(int iron) {
    setIron(this.iron + iron);
  }    
}

