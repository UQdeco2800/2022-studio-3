package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

/**
 * Defines the ship's properties stored in config file to be loaded by the
 * Building Factory.
 */
public class ShipConfig extends BaseEntityConfig {
    public Building type = Building.SHIP;
    public int level = 1;
}