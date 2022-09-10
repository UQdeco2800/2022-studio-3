package com.deco2800.game.areas.MapGenerator;

/**
 * Coordinates represent a cartesian point on the map and are used to assist in processing
 * data in the dynamic generation of the map
 */
public class Coordinate {
    /**
     * X position on the cartesian plane
     */
    private final int x;
    /**
     * Y position on the cartesian plane
     */
    private final int y;

    /**
     * Constructs a new coordinate at the given (x,y) position
     * @param x x position to initiate coordinate at
     * @param y y position to initiate coordinate at
     */
    public Coordinate(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Default constructor for an empty coordinate, assumes (0,0) starting location
     */
    public Coordinate() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Getter function to return the x value of this coordinate
     * @return associated x value
     */
    public int getX() {
        return this.x;
    }

    /**
     * Getter function to return the y value of this coordinate
     * @return associated y value
     */
    public int getY() {
        return this.y;
    }

    /**
     * Returns whether a coord is in map bounds of the specified MapGenerator
     * @param mg MapGenerator with which this coordinate is referencing
     * @return true if in bounds, false otherwise
     */
    public boolean inBounds(MapGenerator mg) {
        int mapWidth = mg.getWidth();
        int mapHeight = mg.getHeight();
        return this.x >= 0 && this.x < mapWidth && this.y >= 0
                && this.y < mapHeight;
    }

    /**
     * Returns whether a coord is in map bounds
     * @param mapWidth width of the map
     * @param mapHeight height of the map
     * @return true if in bounds, false otherwise
     */
    public boolean inBounds(int mapWidth, int mapHeight) {
        return this.x >= 0 && this.x < mapWidth && this.y >= 0
                && this.y < mapHeight;
    }

    /**
     * Coordinates are equal if their x and y values coincide. The equals function is overriden
     * to reflect this
     * @param other other object being compared to this coordinate
     * @return true if they are equal by the above definition, false otherwise
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Coordinate)) {
            return false;
        }

        Coordinate otherCoordinate = (Coordinate) other;

        return (this.x == otherCoordinate.getX()
                && this.y == otherCoordinate.getY());
    }

    /**
     * Coordinates can be more easily visualised in typical cartesian form of "(x, y)"
     * The toString() function is overridden to allow debugging of errors
     * @return human-readable representation of a coordinate
     */
    @Override
    public String toString() {
        return "(" + this.x +", " + this.y +")";
    }
}
