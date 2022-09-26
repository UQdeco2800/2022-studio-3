package com.deco2800.game.areas.MapGenerator.Buildings;

import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.ResourceSpecification;

public class CityRow {
    /**
     * Height that this row of buildings will occupy - max height of all buildings
     */
    private int height = 0;

    /**
     * Width in tiles of the entire row
     */
    private final int rowWidth;

    /**
     * Space in tiles remaining for placement
     */
    private int spaceRemaining;

    /**
     * Total amount of buildings placed in this row
     */
    private int totalPlacements = 0;

    /**
     * Space in tiles between placements of buildings (X-Axis)
     */
    private final int CITY_BUFFER;

    /**
     * Space in tiles between the edge of the city and the first building (to account for walls)
     */
    private final int WALL_BUFFER;

    /**
     * Denotes which side of the row to place the building: true -> left, false -> right
     */
    private boolean placementSide = true;

    /**
     * Denotes the space taken up from the left of the city row
     */
    private int leftSpaceTaken = 0;

    /**
     * Denotes the space taken up from the right of the city row
     */
    private int rightSpaceTaken = 0;

    /**
     * Index of the row -> 0 = top
     */
    private final int index;

    /**
     * Creates a new row of the city to allocate buildings to, given the width of the city in tiles,
     * the amount of tiles to dedicate to walls around the city, and the amount of space to place between each city
     * @param cityWidth Width of the city in tiles
     * @param CITY_BUFFER Space to leave (in tiles) between each building
     * @param WALL_BUFFER Space to leave (in tiles) between the edge of the city and the first and last buildings
     */
    public CityRow(int cityWidth, int CITY_BUFFER, int WALL_BUFFER, int index) {
        this.CITY_BUFFER = CITY_BUFFER;
        this.WALL_BUFFER = WALL_BUFFER;
        this.rowWidth = cityWidth;
        this.index = index;
        spaceRemaining = cityWidth - (2 * WALL_BUFFER);
    }

    /**
     * Returns the total amount of buildings placed in this row
     * @return amount of buildings placed in this row
     */
    public int getTotalPlacements() {
        return this.totalPlacements;
    }

    public int getRowWidth() {
        return this.rowWidth;
    }

    public int getHeight() {
        return this.height;
    }

    public int getSpaceRemaining() {
        return this.spaceRemaining;
    }

    public int getIndex() {
        return this.index;
    }

    /**
     * Attempts to add a building to this City row
     * @param width width in tiles of building to be added
     * @param height height in tiles of building to be added
     * @return the x position of the building within the row, or -1 if it cannot be fit
     */
    public int addBuilding(int width, int height) {
        //If no space in row return -1
        if (width + CITY_BUFFER > spaceRemaining) {
            return -1;
        }
        //If the height of this building exceeds the height of the row, set the height to this
        //building's height
        if (height > this.height) {
            this.height = height;
        }

        //Define return variable for x position in row to place city
        int xPlacement;

        if (placementSide) {
            //Determine where the coordinate will be placed in the row from the left
            xPlacement = WALL_BUFFER + leftSpaceTaken;
            leftSpaceTaken += width + CITY_BUFFER;
        } else {
            //Determine where the coordinate will be placed in the row from the right
            xPlacement = rowWidth - WALL_BUFFER - rightSpaceTaken - width;
            rightSpaceTaken += width + CITY_BUFFER;
        }

        //Update space remaining in the row
        spaceRemaining -= (width + CITY_BUFFER);

        //Invert placement side
        placementSide = !placementSide;

        //Update total placements
        totalPlacements++;

        return xPlacement;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            //The compared "other" object references the same object as this object - equal
            return true;
        }
        if (!(other instanceof CityRow)) {
            //The compared "other" object is not of type CityRow, therefore it cannot be equal
            return false;
        }
        //Cast other as CityRow for comparison
        CityRow otherRow = (CityRow) other;

        return (this.spaceRemaining == otherRow.getSpaceRemaining()
                && this.totalPlacements == otherRow.getTotalPlacements()
                && this.height == otherRow.getHeight()
                && this.rowWidth == otherRow.getRowWidth());
    }

    @Override
    public String toString() {
        return "Index: " + index + ", Height: " + height +", Placements: " +totalPlacements;
    }
}
