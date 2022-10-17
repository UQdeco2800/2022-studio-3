package com.deco2800.game.components;

/**
 * Enumeration used to represent compass direction of
 * enemy entities.
 */
public enum EntityDirection {
  NORTH {
    @Override
    public String toString() {
      return "north";
    }
  },
  SOUTH {
    @Override
    public String toString() {
      return "south";
    }
  },
  EAST {
    @Override
    public String toString() {
      return "east";
    }
  },
  WEST {
    @Override
    public String toString() {
      return "west";
    }
  },
  DEFAULT {
    @Override
    public String toString() {
      return "default";
    }
  }
}
