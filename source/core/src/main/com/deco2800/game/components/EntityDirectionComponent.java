package com.deco2800.game.components;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EntityDirectionComponent extends Component {
  private static final Logger logger = LoggerFactory.getLogger(EntityDirectionComponent.class);

  private EntityDirection entityDirection;

  public EntityDirectionComponent() {
    this.entityDirection = EntityDirection.DEFAULT;
  }

  public EntityDirection getEntityDirection() {
    return entityDirection;
  }

  private void setEntityDirection(EntityDirection direction) {
    this.entityDirection = direction;
  }

  public void setDirectionNorth() {
    setEntityDirection(EntityDirection.NORTH);
  }

  public void setDirectionSouth() {
    setEntityDirection(EntityDirection.SOUTH);
  }

  public void setDirectionEast() {
    setEntityDirection(EntityDirection.EAST);
  }

  public void setDirectionWest() {
    setEntityDirection(EntityDirection.WEST);
  }
}
