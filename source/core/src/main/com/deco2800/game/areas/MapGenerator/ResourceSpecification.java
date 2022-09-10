package com.deco2800.game.areas.MapGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;

public class ResourceSpecification {
    private final String name;
    private final int width;
    private final int height;
    private final int amount;
    private final int preferredDistance;
    private List<Coordinate> placements;
    private List<Coordinate> potentialPlacements;


    /**
     * Creates a new ResourceSpecification with desired placement information within the game
     * @param name Name of the resource
     * @param width tile side length of resource
     * @param height tile height of resource
     * @param minAmount Minimum amount of this resource the game should add
     * @param maxAmount Maximum amount of this resource the game should add
     * @param preferredDistance Preferred proximity to the city (smaller -> closer)
     * @requires minAmount is less than maxAmount
     */
    public ResourceSpecification(String name, int width, int height, int minAmount, int maxAmount, int preferredDistance) {
        //Set base details of resource specification
        this.name = name;
        this.width = width;
        this.height = height;
        this.preferredDistance = preferredDistance;
        //Choose how many to add to game
        amount = new Random().nextInt(maxAmount - minAmount) + minAmount + 1;

        placements = new ArrayList<>();
        potentialPlacements = new ArrayList<>();
    }

    /**
     * Getter method to return all the tile locations that this resource has been placed at
     * @return the placements list of coordinates for this resource
     */
    public List<Coordinate> getPlacements() {
        return this.placements;
    }

    /**
     * Getter method to return all the tile locations that this resource may be placed at
     * @return
     */
    public List<Coordinate> getPotentialPlacements() {
        return this.potentialPlacements;
    }

    /**
     * Returns the width of this resource (in tiles)
     * @return Width of resource
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Returns the height of this resource (in tiles)
     * @return Height of resource
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Returns the amount of this resource to add to the game
     * @return amount of this resource to add
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Returns the preferredDistance of this resource from the city centre
     * @return preferredDistance
     */
    public int getPreferredDistance() {
        return this.preferredDistance;
    }

    /**
     * Returns the name of the resource
     * @return name of the resource
     */
    public String getName() {
        return this.name;
    }

    /**
     * Associates this ResourceSpecification with a tile of the map to be placed on
     * @param c Coordinate of the tile to place this resource
     */
    public void addPlacement(Coordinate c) {
        placements.add(c);
    }

    /**
     * Sets a new list of potential placements for this resource on the map
     * @param newPotentialPlacements the new places where this tile may be validly placed
     */
    public void updatePotentialPlacements(List<Coordinate> newPotentialPlacements) {
        this.potentialPlacements = new ArrayList<>(newPotentialPlacements);
    }

    /**
     * Empties the placements and potential placements lists, when Map must be re-generated
     */
    public void resetPlacements() {
        this.potentialPlacements = new ArrayList<>();
        this.placements = new ArrayList<>();
    }

    /**
     * Returns how many more of this resource must be placed by the ResourceGenerator
     * @return how many more of this resource that must be placed
     */
    public int getRemainingPlacements() {
        return amount - placements.size();
    }

    /**
     * Returns human-readable form of a ResourceSpecification used for testing
     * @return human-readable string of a ResourceSpecification
     */
    @Override
    public String toString() {
        StringJoiner output = new StringJoiner(System.lineSeparator());
        output.add("Name: " + this.name);
        output.add("Width: " + this.width);
        output.add("Height: " + this.height);
        output.add("Amount: " + this.amount);
        output.add("Preferred Distance: " + this.preferredDistance);
        return output.toString();
    }

}
