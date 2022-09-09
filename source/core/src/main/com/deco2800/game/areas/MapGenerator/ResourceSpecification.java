package com.deco2800.game.areas.MapGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResourceSpecification {
    private final String name;
    private final int size;
    private final int amount;
    private final int preferredDistance;
    private List<Coordinate> placements;
    private List<Coordinate> potentialPlacements;

    /**
     * Creates a new ResourceSpecification with desired placement information within the game
     * @param name Name of the resource
     * @param size Square size of the resource in tiles e.g 1 -> 1x1 resource
     * @param minAmount Minimum amount of this resource the game should add
     * @param maxAmount Maximum amount of this resource the game should add
     * @param preferredDistance Preferred proximity to the city (smaller -> closer)
     * @requires minAmount is less than maxAmount
     */
    public ResourceSpecification(String name, int size, int minAmount, int maxAmount, int preferredDistance) {
        //Set base details of resource specification
        this.name = name;
        this.size = size;
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
     * Returns the size of this resource, in square tile size -> e.g 2 -> 2x2 resource size
     * @return Size of resource
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Returns the amount of this resource to add to the game
     * @return amount of this resource to add
     */
    public int getAmount() {
        return this.amount;
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
     * Returns how many more of this resource must be placed by the ResourceGenerator
     * @return how many more of this resource that must be placed
     */
    public int getRemainingPlacements() {
        return amount - placements.size();
    }

}
