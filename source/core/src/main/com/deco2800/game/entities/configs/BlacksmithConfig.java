package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the Black Smith properties stored in config file to be loaded by the Building Factory.
 */
public class BlacksmithConfig extends BaseEntityConfig{
    public Building type = Building.BLACKSMITH;
    public int level = 1;
}
