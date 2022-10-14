package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the town hall properties stored in config file to be loaded by
 * the Building Factory.
 */
public class TownHallConfig extends BaseEntityConfig {
    public Building type = Building.TOWNHALL;
    public int level = 1;
}
