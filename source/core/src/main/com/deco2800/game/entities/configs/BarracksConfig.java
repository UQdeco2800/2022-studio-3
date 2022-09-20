package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the barracks properties stored in config file to be loaded by the Building Factory.
 */
public class BarracksConfig extends BaseEntityConfig{
    public Building type = Building.BARRACKS;
    public int level = 1;
}
