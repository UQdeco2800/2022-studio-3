package com.deco2800.game.areas.MapGenerator;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

import java.util.*;


/**
 * Responsible for the procedural placement of a static collection of resources around the game map
 */
public class ResourceGenerator {
    private Map<String, Coordinate> cityDetails;
    private char[][] map;
    private final List<ResourceSpecification> resources;
    private final MapGenerator mg;
    private final char RESOURCE_CHAR = 'r';
    private List<Coordinate> islandTiles = new ArrayList<>();

    private int generationAttempts = 0;
    private final int MAX_GENERATION_ATTEMPTS = 5;
    /**
     * Space between each resource in tiles (all around it)
     */
    private final int RESOURCE_BUFFER = 1;

    /**
     * Constructs a new ResourceGenerator, provided the game's MapGenerator
     * @param mg The game's MapGenerator object
     */
    public ResourceGenerator(MapGenerator mg) {
        //Set the resources MapGenerator
        this.mg = mg;

        //Initialise resources List
        resources = new ArrayList<>();

        //Initialise default values of map objects from the passed MapGenerator
        resourceSetup();

        //Initialise islandTiles  list
        findIslandTiles();

        //Read in resourceSpecification data from configs/resources.json
        /* For each resourceSpecification object defined under the "resources" header in
           resources.json, add a resource object to the resources list.
         */
        JsonReader json = new JsonReader();
        JsonValue resourcesJson = json.parse(Gdx.files.internal("configs/resources.json")).get("resources");

        for (JsonValue r : resourcesJson.iterator()) {
            //Define object
            ResourceSpecification resource = new ResourceSpecification(r.getString("name"),
                    r.getInt("width"), r.getInt("height"), r.getInt("minAmount"),
                    r.getInt("maxAmount"), r.getInt("preferredDistance"));

            //Add object to list
            resources.add(resource);
        }

        //Place each resource the desired number of times based on preferred distance
        placeResources();
        //Test output
        writeCurrentResources("/home/r0m4n/Documents/deco2800/map.txt");
    }

    /**
     * Initialises the resource generator with the current game terrain map and city details
     */
    public void resourceSetup () {
        this.cityDetails = mg.getCityDetails();
        this.map = MapGenerator.copyMap(mg.getMap(), mg.getWidth(), mg.getHeight());

        //Empty all existing placements of resources if there are any
        for (ResourceSpecification r : resources) {
            r.resetPlacements();
            if (generationAttempts > 0) {
                r.setNewAmount();
            }
        }

    }


    /**
     * Writes the current resources to an output file, for testing purposes
     */
    private void writeCurrentResources(String path) {
        char[][] writeMap = mg.getOutlineMap();
        for (ResourceSpecification r : resources) {
            //For each resource, go through all placements and modify its value on the map
            char identifier = r.getName().charAt(0);    //First letter of name will be value on map
            for (Coordinate c : r.getPlacements()) {
                //Set the value on the map of this resource to its identifier
                for (int i = 0; i < r.getHeight(); i++) {
                    for (int j = 0; j < r.getWidth(); j++) {
                        writeMap[c.getY() + i][c.getX() + j] = identifier;
                    }
                }

            }
        }
        MapGenerator.writeMap(writeMap, path, mg.getWidth(), mg.getHeight());
    }

    /**
     * Returns the list of resources - which contain their map placements.
     * @return list of resources
     */
    public List<ResourceSpecification> getResources() {
        return this.resources;
    }

    /**
     * Places resources at valid positions on the map
     */
    private void placeResources() {
        /* Sort the resources list such that the highest size is first -> these will be placed first
        to minimize the chance of running out of island space, requiring a re-generation
         */
        resources.sort((r1, r2) -> (r2.getWidth() * r2.getHeight()) - (r1.getWidth() * r1.getHeight()));
        //Place all resources for each type
        for (ResourceSpecification resource : resources) {
            while(resource.getRemainingPlacements() > 0) {
                //While less than the desired number of resources have been placed, continue placing resources
                //Determine where a resource may be placed
                setPotentialPlacements(resource);

                //If this resource has no valid placing position, try to generate a new map or throw exception
                if (resource.getPotentialPlacements().size() == 0) {
                    if (generationAttempts++ < MAX_GENERATION_ATTEMPTS) {
                        //Re-generate map
                        mg.generateMap();
                        //Re-initialize values
                        resourceSetup();
                        //Recursively call placeResources to try again
                        // (will stop if hits base case of generation attempts exceeding limit)
                        placeResources();
                        //Execute no more code in this run of the function
                        return;
                    } else {
                        //After a number of attempts, stop trying to fill island with resources
                        throw new IllegalArgumentException("Invalid map space to place desired resources");
                    }
                }
                //Choose a potential placement based on preferredDistance
                Coordinate placement = choosePlacement(resource);

                //Add placement to list of placements
                resource.addPlacement(placement);
                //Update copy of map to reflect this
                for (int i = 0; i < resource.getHeight(); i++) {
                    for (int j = 0; j < resource.getWidth(); j++) {
                        map[placement.getY() + i][placement.getX() + j] = RESOURCE_CHAR;
                    }
                }

            }
            //writeCurrentResources("E:\\Deco2800 sprint 2\\Testing\\after_" + resource.getName() + ".txt");
        }
    }

    /**
     * Builds the list of coordinates of all tiles in the island - to speed up iteration
     */
    private void findIslandTiles() {
        for (int i = 0; i < mg.getWidth(); i++) {
            for (int j = 0; j < mg.getHeight(); j++) {
                if (map[j][i] == mg.getIslandChar()) {
                    islandTiles.add(new Coordinate(i,j));
                }
            }
        }
    }

    /**
     * Checks to see if a resource may be placed at a given tile on the map
     * @param resource the resource desired to be placed
     * @return true if the resource may be placed there, false otherwise.
     */
    private boolean checkTile(ResourceSpecification resource, Coordinate location) {
        /* Check tiles around the resource placement location relative to the resource size
           e.g For a map with the following placements
           [*, I, I
            *, *, I,
            *, *, *]
            If you wished to check tile [0, 1] -> Second tile in first row, for a resource
            of width and height 2, it would check the tiles in a 2x2 square around [0,1] ->
            i.e. itself, one to the right, one below, and one to the bottom right
         */
        for (int i = location.getX() - RESOURCE_BUFFER; i < location.getX() + resource.getWidth() + RESOURCE_BUFFER; i++) {
            for (int j = location.getY() - RESOURCE_BUFFER; j < location.getY() + resource.getHeight() + RESOURCE_BUFFER; j++) {
                Coordinate testLocation = new Coordinate(i,j);
                if ((i >= location.getX() && i < location.getX() + resource.getWidth()
                        && j >= location.getY() && j < location.getY() + resource.getHeight())
                        && !(testLocation.inBounds(mg.getWidth(), mg.getHeight())
                            && map[j][i] == mg.getIslandChar())) {
                    //Tested co-ordinate is either out of bounds, or not an open island tile
                    return false;
                } else if ((i < location.getX() || i >= location.getX() + resource.getWidth()
                        || j < location.getY() || j >= location.getY() + resource.getHeight())
                        && testLocation.inBounds(mg.getWidth(), mg.getHeight())
                        && map[j][i] == RESOURCE_CHAR) {
                    //Tested co-ordinate is within RESOURCE_BUFFER distance of another resource
                    return false;
                }
            }
        }

        //No occupied tile detected in square which will house resource, valid placement
        return true;
    }

    /**
     * Sets the potential placements for a given resource in the current map
     * @param resource ResourceSpecification being examined
     */
    private void setPotentialPlacements(ResourceSpecification resource) {
        List<Coordinate> potentialPlacements = new ArrayList<>();
        for (Coordinate c : islandTiles) {
            if (checkTile(resource, c)) {
                potentialPlacements.add(c);
            }
        }
        resource.updatePotentialPlacements(potentialPlacements);
    }

    /**
     * Chooses a placement for this resource from its list of potential placement based on its
     * preferredDistance
     * @param resource The resource which is being placed on the map
     * @return the placement of the resource on the map
     */
    private Coordinate choosePlacement(ResourceSpecification resource) {
        List<Coordinate> potentialPlacements = resource.getPotentialPlacements();
        int totalCount = 0;
        TreeMap<Integer, Coordinate> weightMap = new TreeMap<>();

        //Grab details necessary for weighting placements
        Coordinate ne = cityDetails.get("NE");
        Coordinate nw = cityDetails.get("NW");
        Coordinate se = cityDetails.get("SE");
        int cityYMax = se.getY();
        int cityYMin = ne.getY();
        int cityXMin = nw.getX();
        int cityXMax = ne.getX();

        for (Coordinate c : potentialPlacements) {
            weightMap.put(totalCount, c);
            totalCount += weightPlacement(c, resource.getPreferredDistance(), cityXMin, cityXMax, cityYMin, cityYMax);
        }
        //Return the object correlating to the weight returned - in this case the lowest key
        //closest to the random number rolled correlates to the move chosen
        return weightMap.get(weightMap.floorKey(new Random().nextInt(totalCount)));
    }

    /**
     * Weights a placement location based on its distance from the city centre VS preferred distance
     * from city centre
     * @param placement location being weighted
     * @param preferredDistance goal distance from city centre
     * @return Weighting: higher -> closer to desired distance, and vice versa
     */
    private int weightPlacement(Coordinate placement, int preferredDistance, int cityXMin, int cityXMax, int cityYMin, int cityYMax) {
        //Calculate x and y components of distance based on where resource is relative to city
        int xComponent = 0;
        int yComponent = 0;
        if (placement.getX() < cityXMin) {
            //Resource is left of city
            xComponent = Math.abs(cityXMin - placement.getX());
        } else if (placement.getX() > cityXMax) {
            //Resource is right of city
            xComponent = Math.abs(cityXMax - placement.getX());
        }
        if (placement.getY() < cityYMin) {
            //Resource is above city
            yComponent = Math.abs(cityYMin - placement.getY());
        } else if (placement.getY() > cityYMax) {
            yComponent = Math.abs(cityYMax - placement.getY());
        }

        Coordinate centre = cityDetails.get("Centre");
        Map<String, Coordinate> islandEdges = mg.getIslandEdges();
        int islandMinX = islandEdges.get("Left").getX();
        int islandMaxX = islandEdges.get("Right").getX();

        int maxXDistance = Math.max(islandMaxX - cityXMax, cityXMin - islandMinX);

        //Distance is sum of x and y components of distance
        int distance = xComponent + yComponent;


        //If distance is too high, weight points with higher distance seen as more favourable
        if (preferredDistance >= maxXDistance) {
            return (int)(Math.pow(2, 10.5 * distance / maxXDistance));
        } else if (preferredDistance <= 4) {
            //If distance is too close to centre, weight points with lower distance seen as more favourable
            return distance > 0 ? (int) Math.pow(2, 9/distance) : 0;
        } else {
            //Calculate the difference between the distance between the placement
            // and the centre of the city and the preferred distance
            int distanceDifference = Math.abs(distance - preferredDistance);
            return (int) Math.pow(2, 5/(distanceDifference + 1));
        }
    }
}