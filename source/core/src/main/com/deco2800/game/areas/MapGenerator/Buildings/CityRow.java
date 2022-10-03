package com.deco2800.game.areas.MapGenerator.Buildings;

import com.deco2800.game.areas.MapGenerator.Coordinate;
import java.util.LinkedList;
import java.util.List;

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
     * Space in tiles between placements of buildings (X-Axis)
     */
    private final int CITY_BUFFER;

    /**
     * Space in tiles between the edge of the city and the first building (to account for walls)
     */
    private final int WALL_BUFFER;

    /**
     * Space in tiles which must exist between the right edge of the city and the last (far right) building
     */
    private final int RIGHT_BUFFER;

    /**
     * Denotes the space (in tiles) taken up from the left of the city row
     */
    private int leftSpaceTaken = 0;

    /**
     * Index of the row -> 0 = top
     */
    private final int index;

    /**
     * List of buildings in this row
     */
    private List<Building> buildings;


    /**
     * Creates a new row of the city to allocate buildings to, given the width of the city in tiles,
     * the amount of tiles to dedicate to walls around the city, and the amount of space to place between each city
     * @param cityWidth Width of the city in tiles
     * @param CITY_BUFFER Space to leave (in tiles) between each building
     * @param WALL_BUFFER Space to leave (in tiles) between the edge of the city and the first and last buildings
     */
    public CityRow(int cityWidth, int CITY_BUFFER, int WALL_BUFFER, int RIGHT_BUFFER, int index) {
        this.CITY_BUFFER = CITY_BUFFER;
        this.WALL_BUFFER = WALL_BUFFER;
        this.RIGHT_BUFFER = RIGHT_BUFFER;
        this.rowWidth = cityWidth;
        this.index = index;

        //Initialise lists and variables to default values
        resetRow();
    }

    /**
     * Returns the total amount of buildings placed in this row
     * @return amount of buildings placed in this row
     */
    public int getTotalPlacements() {
        return buildings.size();
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

    public void resetRow() {
        this.buildings = new LinkedList<>();
        this.height = 0;
        this.leftSpaceTaken = 0;
        spaceRemaining = rowWidth - (2 * WALL_BUFFER) - RIGHT_BUFFER;
    }

    /**
     * Attempts to add a building to this City Row
     * @param building building to be added
     * @param corner whether to place a building on the right side
     * @return true if the building was placed, else false if no space
     */
    public boolean addBuilding(Building building, boolean corner) {
        int width = building.getWidth();
        int height = building.getHeight();

        //If no space in row return false no room to place it
        if (width + CITY_BUFFER > spaceRemaining) {
            return false;
        }
        //If the height of this building exceeds the height of the row, set the height to this
        //building's height
        if (height > this.height) {
            this.height = height;
        }

        //Define return variable for x position in row to place city
        int xPlacement;

        if (!corner) {
            //Determine where the coordinate will be placed in the row from the left
            xPlacement = WALL_BUFFER + leftSpaceTaken;
            leftSpaceTaken += width + CITY_BUFFER;
        } else {
            //Determine where the coordinate will be placed in the row from the right
            xPlacement = rowWidth - WALL_BUFFER - width - RIGHT_BUFFER;
        }

        //Update space remaining in the row
        spaceRemaining -= (width + CITY_BUFFER);

        //Set building's x component
        building.setPlacement(new Coordinate(xPlacement, 0));

        //Add building to buildings list
        buildings.add(building);

        return true;
    }

    /**
     * Updates the height of all buildings in the city row, after rows have been centred
     * @param xOffset x value to be added onto start point to make this a valid coordinate on the map
     * @param yOffset y value to be added onto start point to make this a valid coordinate on the map
     */
    public void setBuildingCoordinates(int xOffset, int yOffset) {
        for (Building b : buildings) {
            b.setPlacement(new Coordinate(b.getPlacement().getX() + xOffset, yOffset));
        }
    }

    /**
     * Distributes buildings evenly across the city based on how much empty space is left
     */
    public void centreRow() {
        //Rows of 2 or less do not need centring
        if (buildings.size() < 3) {
            return;
        }
        //Offset to add to every building
        int baseOffset = spaceRemaining / (buildings.size() - 2);
        //Offset to add to some buildings
        int conditionalOffset = spaceRemaining % (buildings.size() - 2);
        int usedOffsets = 0;

        //Centre all buildings but the edge buildings (Building at index 0 and 1)
        int shift = 0;
        for (int i = 2; i < buildings.size(); i++) {
            int conditional = 0;
            //Add conditional offset if necessary
            if (usedOffsets < conditionalOffset) {
                conditional = 1;
                usedOffsets++;
            }
            //Shift building by the offset
            Building currentBuilding = buildings.get(i);
            Coordinate currentPlacement = currentBuilding.getPlacement();
            shift += baseOffset + conditional;
            int newXPlacement = currentPlacement.getX() + shift;
            currentBuilding.setPlacement(new Coordinate(newXPlacement, currentPlacement.getY()));
        }
    }

    /**
     * Returns a copy of this CityRow's building list
     * @return list of buildings in the row
     */
    public LinkedList<Building> getBuildings() {
        return new LinkedList<>(buildings);
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
                && this.buildings.size() == otherRow.getBuildings().size()
                && this.height == otherRow.getHeight()
                && this.rowWidth == otherRow.getRowWidth());
    }

    @Override
    public String toString() {
        return "Index: " + index + ", Height: " + height +", Placements: " +buildings.size();
    }
}
