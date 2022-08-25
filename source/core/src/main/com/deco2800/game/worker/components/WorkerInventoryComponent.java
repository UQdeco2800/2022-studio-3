package com.deco2800.game.worker.components;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by the player to track their unit's inventory.
 *
 * Currently only stores the stone and metal amount but can be extended for more 
 * advanced functionality such as storing other items.
 * Can also be used as a more generic component for other entities.
 */
public class WorkerInventoryComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(WorkerInventoryComponent.class);
  private int stone;
  private int metal;
  private int wood;

  public WorkerInventoryComponent() {
    this.stone = 0;
    this.metal = 0;
    this.wood = 0;
  }
  
  public WorkerInventoryComponent(int stone, int metal, int wood) {
    this.stone = stone;
    this.metal = metal;
    this.wood = wood;
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
   * Returns the amount of metal in the inventory
   *
   * @return amount of the metal
   */
  public int getMetal() {
    return this.metal;
  }

  /**
   * Returns the amount of wood in the inventory
   * @return amount of the wood
   */
  public int getWood() {
    return wood;
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
   * Returns if the entity has a certain amount of metal.
   * @param metal required amount of metal
   * @return player has greater than or equal to the required amount of metal
   */
  public Boolean hasMetal(int metal) {
    return this.metal >= metal;
  }

    /**
   * Returns if the entity has a certain amount of wood.
   * @param wood required amount of wood
   * @return player has greater than or equal to the required amount of wood
   */
  public Boolean hasWood(int wood) {
    return this.wood >= wood;
  }

  /**
   * Sets the player's stone. Stone has a minimum bound of 0.
   *
   * @param stone stone
   */
  public void setStone(int stone) {
    if (stone >= 0) {
      this.stone = stone;
      logger.debug("Setting stone to {}", this.stone);
    }
  }

  /**
   * Sets the player's metal. Metal has a minimum bound of 0.
   *
   * @param metal metal
   */
  public void setMetal(int metal) {
    if (metal >= 0) {
      this.metal = stone;
      logger.debug("Setting metal to {}", this.metal);
    }  
  }

  /**
   * Sets the player's wood. Wood h
   * @param wood
   */
  public void setWood(int wood) {
    if (wood >= 0) {
      this.wood = wood;
      logger.debug("Setting wood to {}", this.wood);
    }
  }

  /**
   * Adds to the player's stone. The amount added can be negative.
   * @param stone stone to add
   */
  public void addStone(int stone) {
    setStone(this.stone + stone);
  }

  /**
   * Adds to the player's metal. The amount added can be negative.
   * @param metal metal to add
   */
  public void addMetal(int metal) {
    setMetal(this.metal + metal);
  }    

  /**
   * Adds to the player's wood. The amount added can be negative.
   * @param wood wood to add
   */
  public void addWood(int wood) {
    setWood(this.wood + wood);
  }    

  public int unloadStone() {
    int loaded = getStone();
    setStone(0);
    return loaded;
  }

  public int unloadMetal() {
    int loaded = getMetal();
    setMetal(0);
    return loaded;
  }

  public int unloadWood() {
    int loaded = getWood();
    setWood(0);
    return loaded;
  }
}

