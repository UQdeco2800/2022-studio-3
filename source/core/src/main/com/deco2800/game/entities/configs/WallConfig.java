package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the wall properties stored in config file to be loaded by the Building Factory.
 */
public class WallConfig extends BaseEntityConfig {
    public Building type = Building.WALL;
    public int level = 1;
}

