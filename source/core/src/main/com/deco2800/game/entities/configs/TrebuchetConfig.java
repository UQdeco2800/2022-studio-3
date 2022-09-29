package com.deco2800.game.entities.configs;

import com.deco2800.game.components.building.Building;

public class TrebuchetConfig extends BaseEntityConfig {
    public Building type = Building.TREBUCHET;
    public int level = 1;
    public float range = 0f; // for trebuchets ranged attack
    public float sight = 0f; // if ship within sight then attack
}