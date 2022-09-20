package com.deco2800.game.areas.MapGenerator.Buildings;

import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;

import java.util.ArrayList;
import java.util.List;

public class BuildingSpecification {
    /**
     * Name of the building being placed
     */
    private final String name;
    /**
     * Width in tiles of building being placed
     */
    private final int width;
    /**
     * Height in tiles of building being placed
     */
    private final int height;
    /**
     * Number of buildings to add to the game
     */
    private final int num;

    /**
     * Locations at which the building is to be placed
     */
    private List<Coordinate> placements;

    /**
     * Construct a new BuildingSpecification
     * @param name name of building
     * @param width width in tiles
     * @param height height in tiles
     * @param num number of this building to place
     */
    public BuildingSpecification(String name, int width, int height, int num) {
        this.name = name;
        this.width = width;
        this.height = height;
        this.num = num;
        placements = new ArrayList<>();
    }

    /**
     * Adds a placement coordinate for this building
     * @param placement location to place building
     */
    public void addPlacement(Coordinate placement) {
        if (numRemaining() > 0) {
            placements.add(placement);
        }
    }

    /**
     * Returns the number of buildings still to be placed
     * @return Number of buildings of this type to place
     */
    public int numRemaining() {
        return num - placements.size();
    }

    /**
     * Returns a copy of this BuildingSpecification's placement list
     * @return the BuildingSpecification's placement list
     */
    public List<Coordinate> getPlacements() {
        return new ArrayList<>(placements);
    }

    /**
     * Getter method to return this BuildingSpecification's width
     * @return width of this building in tiles
     */
    public int getWidth() {
        return width;
    }

    /**
     * Getter method to return this BuildingSpecification's height
     * @return height of building in tiles
     */
    public int getHeight() {
        return height;
    }

    /**
     * Getter method to return BuildingSpecification name
     * @return name of this building
     */
    public String getName() {
        return name;
    }
}
