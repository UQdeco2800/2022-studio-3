package com.deco2800.game.areas.MapGenerator.Buildings;

import com.deco2800.game.areas.MapGenerator.Coordinate;

public class Building {
    private final int width;
    private final int height;
    private final String name;
    private Coordinate placement;

    public Building (int width, int height, String name) {
        this.width = width;
        this.height = height;
        this.name = name;
    }

    /**
     * Returns width of building
     * @return width of building
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns height of building
     * @return height of building
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the name of this building
     * @return name of building
     */
    public String getName() {
        return name;
    }

    /**
     * Sets this building's placement to a desired coordinate
     * @param placement coordinate within city bounds to place building
     */
    public void setPlacement(Coordinate placement) {
        this.placement = placement;
    }

    /**
     * Returns the placement of this building
     * @return placement of building relative to city bounds
     */
    public Coordinate getPlacement() {
        return this.placement;
    }
}
