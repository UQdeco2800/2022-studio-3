package com.deco2800.game.areas.MapGenerator.Buildings;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.MapGenerator.Coordinate;

import java.util.Objects;

public class BuildingSpecification {
    /**
     * Building type to add to game
     */
    private final Building building;

    /**
     * Number of buildings to add to the game
     */
    private final int num;

    /**
     * Number of buildings that have been added
     */
    private int numPlaced;


    /**
     * Construct a new BuildingSpecification
     * @param name name of building
     * @param width width in tiles
     * @param height height in tiles
     * @param door coordinates of the building door
     * @param num number of this building to place
     */
    public BuildingSpecification(String name, int width, int height, String door, int num) {
        String[] components = door.split(",");
        int xCoord = Integer.parseInt(components[0]);
        int yCoord = Integer.parseInt(components[1]);
        this.building = new Building(width, height, name, new GridPoint2(xCoord, yCoord));
        this.num = num;
        this.numPlaced = 0;
    }

    /**
     * Returns the number of buildings to place in the game.
     * @return number of buildings to place
     */
    public int numToBuild() {
        return this.num;
    }

    /**
     * Returns the number of buildings still to be placed
     * @return Number of buildings of this type to place
     */
    public int numRemaining() {
        return num - numPlaced;
    }

    /**
     * Increments the amount of buildings counter
     */
    public void incNumPlaced() {
        numPlaced++;
    }

    public String getName() {
        return building.getName();
    }

    public int getHeight() {
        return building.getHeight();
    }

    public int getWidth() {
        return building.getWidth();
    }

    public GridPoint2 getDoor() {
        return building.getDoor();
    }

    public void resetPlacements() {
        this.numPlaced = 0;
    }

    public int getNum() {
        return this.num;
    }

    @Override
    public String toString() {
        return this.getName();
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof BuildingSpecification)) {
            return false;
        }

        BuildingSpecification otherBS = (BuildingSpecification) other;

        return (this.getName().equals(otherBS.getName())
                && this.getWidth() == otherBS.getWidth()
                && this.getHeight() == otherBS.getHeight()
                && this.getDoor().equals(otherBS.getDoor())
                && this.getNum() == otherBS.getNum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getWidth(),
                            getHeight(), getDoor(), getNum());
    }
}
