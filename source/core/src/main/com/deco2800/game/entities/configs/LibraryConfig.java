package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the Library properties stored in config file to be loaded by the Building Factory.
 */
public class LibraryConfig extends BaseEntityConfig{
    public Building type = Building.LIBRARY;
    public int level = 1;
}
