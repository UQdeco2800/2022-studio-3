package com.deco2800.game.physics;

public class PhysicsLayer {
  public static final short NONE = 0;
  public static final short DEFAULT = (1 << 0);
  public static final short PLAYER = (1 << 1);
  public static final short WORKER = PLAYER;
  public static final short SOLDIER = PLAYER;
  // Terrain obstacle, e.g. trees
  public static final short OBSTACLE = (1 << 2);
  public static final short RESOURCE_NODE = OBSTACLE;
  public static final short BUILDING_NODE = OBSTACLE;
  // NPC (Non-Playable Character) colliders
  public static final short NPC = (1 << 3);
  public static final short ALL = ~0;

  public static boolean contains(short filterBits, short layer) {
    return (filterBits & layer) != 0;
  }

  private PhysicsLayer() {
    throw new IllegalStateException("Instantiating static util class");
  }
}
