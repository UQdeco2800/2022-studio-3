package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Used to keep record of the position that moving units such as enemies, friendlies
 * and workers, it uses an EnemyDirection enumeration class for each compass direction
 * and EnemyDirection.DEFAULT which is only set upon initialising the class, this to stop
 * avoid using null for direction when component is first initialised.
 */
public class EntityDirectionComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(EntityDirectionComponent.class);

  private EntityDirection entityDirection;

  public EntityDirectionComponent() {
    this.entityDirection = EntityDirection.DEFAULT;
  }

  /**
   * Returns the direction the unit is facing in
   *
   * @return the direction unit is facing in
   */
  public EntityDirection getEntityDirection() {
    return entityDirection;
  }

  /**
   * Sets the direction entity is facing in.
   *
   * @param direction the compass direction (or DEFAULT) to be set
   */
  private void setEntityDirection(EntityDirection direction) {
    this.entityDirection = direction;
  }

  /**
   * Sets enemy direction to north
   */
  public void setDirectionNorth() {
    setEntityDirection(EntityDirection.NORTH);
  }

  /**
   * Sets enemy direction to south
   */
  public void setDirectionSouth() {
    setEntityDirection(EntityDirection.SOUTH);
  }

  /**
   * Sets enemy direction to east
   */
  public void setDirectionEast() {
    setEntityDirection(EntityDirection.EAST);
  }

  /**
   * Sets enemy direction to west
   */
  public void setDirectionWest() {
    setEntityDirection(EntityDirection.WEST);
  }
}
