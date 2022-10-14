package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the farm properties stored in config file to be loaded by the Building Factory.
 */
public class FarmConfig extends BaseEntityConfig{
    public Building type = Building.FARM;
    public int level = 1;
}
