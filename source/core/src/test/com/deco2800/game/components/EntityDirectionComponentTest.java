package com.deco2800.game.components;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityDirectionComponentTest {

  @Test
  void getEntityDirection() {
    EntityDirectionComponent entityDirectionComponent = new EntityDirectionComponent();
    assertEquals(EntityDirection.DEFAULT, entityDirectionComponent.getEntityDirection());
  }

  @Test
  void setDirectionNorth() {
    EntityDirectionComponent entityDirectionComponent = new EntityDirectionComponent();
    assertEquals(EntityDirection.DEFAULT, entityDirectionComponent.getEntityDirection());

    entityDirectionComponent.setDirectionNorth();
    assertEquals(EntityDirection.NORTH, entityDirectionComponent.getEntityDirection());
  }

  @Test
  void setDirectionSouth() {
    EntityDirectionComponent entityDirectionComponent = new EntityDirectionComponent();
    assertEquals(EntityDirection.DEFAULT, entityDirectionComponent.getEntityDirection());

    entityDirectionComponent.setDirectionSouth();
    assertEquals(EntityDirection.SOUTH, entityDirectionComponent.getEntityDirection());
  }

  @Test
  void setDirectionEast() {
    EntityDirectionComponent entityDirectionComponent = new EntityDirectionComponent();
    assertEquals(EntityDirection.DEFAULT, entityDirectionComponent.getEntityDirection());

    entityDirectionComponent.setDirectionEast();
    assertEquals(EntityDirection.EAST, entityDirectionComponent.getEntityDirection());
  }

  @Test
  void setDirectionWest() {
    EntityDirectionComponent entityDirectionComponent = new EntityDirectionComponent();
    assertEquals(EntityDirection.DEFAULT, entityDirectionComponent.getEntityDirection());

    entityDirectionComponent.setDirectionWest();
    assertEquals(EntityDirection.WEST, entityDirectionComponent.getEntityDirection());
  }
}