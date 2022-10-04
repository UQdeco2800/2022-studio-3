package com.deco2800.game.areas;

import com.badlogic.gdx.math.GridPoint2;
import com.deco2800.game.areas.MapGenerator.Coordinate;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

/** Class used to spawn a random point within a certain range of the city center */
public class RandomPointGenerator {

    /**
     * Get the centre of the city as a GridPoint2
     *
     * @param terrainFactory the factory for the Atlantis map
     *
     * @return the grid point representing the city center
     */
    public static GridPoint2 getCityCenter(AtlantisTerrainFactory terrainFactory) {
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Store centre of city
        Coordinate centre = cityDetails.get("Centre");
        return new GridPoint2(centre.getX(), mg.getHeight() - centre.getY());
    }

    /**
     * Gets the re-scaled width of the city map
     *
     * @param scale the scaling factor for the city map (between 0 and 1)
     * @param terrainFactory the factory for the Atlantis map
     *
     * @return the re-scaled width
     */
    public static int getRescaledWidth(AtlantisTerrainFactory terrainFactory, double scale)
            throws IllegalArgumentException {
        if (scale < 0 || scale > 1) {
            throw new IllegalArgumentException("Must be a ratio between 0.0 and 1.0!");
        }
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Get top-left corner of city
        Coordinate topLeftCorner = cityDetails.get("NW");
        // Get bottom-right corner of city
        Coordinate bottomRightCorner = cityDetails.get("SE");
        // Get width and height of city
        float width = bottomRightCorner.getX() - topLeftCorner.getX();
        return (int) (scale * width);
    }

    /**
     * Gets the re-scaled height of the city map
     *
     * @param scale the scaling factor for the city map (between 0 and 1)
     * @param terrainFactory the factory for the Atlantis map
     *
     * @return the re-scaled height
     */
    public static int getRescaledHeight(AtlantisTerrainFactory terrainFactory, double scale)
            throws IllegalArgumentException {
        if (scale < 0 || scale > 1) {
            throw new IllegalArgumentException("Must be a ratio between 0.0 and 1.0!");
        }
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Get top-left corner of city
        Coordinate topLeftCorner = cityDetails.get("NW");
        // Get bottom-right corner of city
        Coordinate bottomRightCorner = cityDetails.get("SE");
        // Get width and height of city
        float height = bottomRightCorner.getY() - topLeftCorner.getY();
        return (int) (scale * height);
    }

    /**
     * Gets the top-left corner of the city map after re-scaling
     *
     * @param scale the scaling factor for the city map (between 0 and 1)
     * @param terrainFactory the factory for the Atlantis map
     *
     * @return the re-scaled top-left corner
     */
    public static GridPoint2 getRescaledTopLeftCorner(AtlantisTerrainFactory terrainFactory, double scale)
            throws IllegalArgumentException {
        if (scale < 0 || scale > 1) {
            throw new IllegalArgumentException("Must be a ratio between 0.0 and 1.0!");
        }
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Re-scale width and height and divide by 2
        int rescaledHalfWidth = getRescaledWidth(terrainFactory, scale) / 2;
        int rescaledHalfHeight = getRescaledHeight(terrainFactory, scale) / 2;
        // Get city center
        Coordinate center = cityDetails.get("Centre");
        // Re-scale corners
        Coordinate rescaledTopLeftCorner = new Coordinate(
                center.getX() - rescaledHalfWidth,
                center.getY() - rescaledHalfHeight
        );
        Coordinate rescaledBottomRightCorner = new Coordinate(
                center.getX() + rescaledHalfWidth,
                center.getY() + rescaledHalfHeight
        );
        return new GridPoint2(
                rescaledTopLeftCorner.getX(),
                mg.getHeight() - rescaledBottomRightCorner.getY()
        );
    }

    /**
     * Gets the bottom-right corner of the city map after re-scaling
     *
     * @param scale the scaling factor for the city map (between 0 and 1)
     * @param terrainFactory the factory for the Atlantis map
     *
     * @return the re-scaled bottom-right corner
     */
    public static GridPoint2 getRescaledBottomRightCorner(AtlantisTerrainFactory terrainFactory, double scale)
            throws IllegalArgumentException {
        if (scale < 0 || scale > 1) {
            throw new IllegalArgumentException("Must be a ratio between 0.0 and 1.0!");
        }
        MapGenerator mg = terrainFactory.getMapGenerator();
        // Get details of where the city is located
        Map<String, Coordinate> cityDetails = mg.getCityDetails();
        // Re-scale width and height and divide by 2
        int rescaledHalfWidth = getRescaledWidth(terrainFactory, scale) / 2;
        int rescaledHalfHeight = getRescaledHeight(terrainFactory, scale) / 2;
        // Get city center
        Coordinate center = cityDetails.get("Centre");
        // Re-scale corners
        Coordinate rescaledTopLeftCorner = new Coordinate(
                center.getX() - rescaledHalfWidth,
                center.getY() - rescaledHalfHeight
        );
        Coordinate rescaledBottomRightCorner = new Coordinate(
                center.getX() + rescaledHalfWidth,
                center.getY() + rescaledHalfHeight
        );
        return new GridPoint2(
                rescaledBottomRightCorner.getX(),
                mg.getHeight() - rescaledTopLeftCorner.getY()
        );
    }

    /**
     * Finds a random point within a certain range of the city centre, based on the scale used.
     * If scale = 0.0, then the city center is returned.
     * If scale = 1.0, then the entire city map is used.
     *
     * @param scale the scaling factor for the city map (between 0 and 1)
     * @param terrainFactory the factory for the Atlantis map
     *
     * @return a random point in the range
     */
    public static GridPoint2 getRandomPointInRange(AtlantisTerrainFactory terrainFactory, double scale)
            throws IllegalArgumentException {
        if (scale < 0 || scale > 1) {
            throw new IllegalArgumentException("Must be a ratio between 0.0 and 1.0!");
        }
        // Return random point in range
        return RandomUtils.random(
                getRescaledTopLeftCorner(terrainFactory, scale),
                getRescaledBottomRightCorner(terrainFactory, scale)
        );
    }

    /**
     * Gets a position to deploy entities in the sea, the range value
     * is to choose a point such that the entity being placed fits
     * in only sea tiles and doesn't end up being spawned at the edges.
     * @param terrainFactory the factory for the Atlantis map
     * @param range points within this range must be ocean tiles.
     * @return
     */
    public static GridPoint2 getRandomPointInSea(AtlantisTerrainFactory terrainFactory, int range) {
        MapGenerator mg = terrainFactory.getMapGenerator();
        char[][] map = mg.getMap();
        char[][] smallerMap = new char[range][range];

        List<GridPoint2> oceanTiles = new ArrayList<>();
        for (int y = 0; y < mg.getHeight(); y++) {
            for (int x = 0; x < mg.getWidth(); x++) {
                if (map[y][x] == mg.getOceanChar()) {
                    GridPoint2 point = new GridPoint2(x, (mg.getHeight()-1)-y);
                    oceanTiles.add(point);
                }
            }
        }

        boolean notFound = true;

        while (notFound) {
            GridPoint2 point1 = oceanTiles.get(0);
            GridPoint2 point2 = oceanTiles.get(oceanTiles.size()-1);
            Random rand = new Random();

            GridPoint2 point = oceanTiles.get(rand.nextInt(oceanTiles.size()-1));
            int x = point.x;
            int y = (mg.getHeight()-1) - point.y;

            try {
                for (int i = y; i < y + range; i++) {
                    for (int j = x; j < x + range; j++) {
                        if (map[j][i] != mg.getOceanChar()) {
                            notFound = true;
                            break;
                        } else {
                            System.out.println("All good");
                        }
                    }
                }
                return point;
            } catch (IndexOutOfBoundsException e) {
                notFound = true;
            }
        }
        return new GridPoint2(0 , 0);
    }

    /**
     * Gets a position to deploy entities on the island, the range value
     * is to choose a point such that the entity being placed fits
     * in only island tiles and doesn't end up being spawned at the edges.
     * @param terrainFactory the factory for the Atlantis map
     * @param range points within this range must be island tiles.
     * @return
     */
    public static GridPoint2 getRandomPointInIsland(AtlantisTerrainFactory terrainFactory, int range) {
        MapGenerator mg = terrainFactory.getMapGenerator();
        char[][] map = mg.getMap();
        char[][] smallerMap = new char[range][range];

        List<GridPoint2> islandTiles = new ArrayList<>();
        for (int y = 0; y < mg.getHeight(); y++) {
            for (int x = 0; x < mg.getWidth(); x++) {
                if (map[y][x] == mg.getIslandChar()) {
                    GridPoint2 point = new GridPoint2(x, (mg.getHeight()-1)-y);
                    islandTiles.add(point);
                }
            }
        }

        boolean notFound = true;

        while (notFound) {
            GridPoint2 point1 = islandTiles.get(0);
            GridPoint2 point2 = islandTiles.get(islandTiles.size()-1);
            Random rand = new Random();

            GridPoint2 point = islandTiles.get(rand.nextInt(islandTiles.size()-1));
            int x = point.x;
            int y = (mg.getHeight()-1) - point.y;

            try {
                for (int i = y; i < y + range; i++) {
                    for (int j = x; j < x + range; j++) {
                        if (map[j][i] != mg.getIslandChar()) {
                            notFound = true;
                            break;
                        } else {
                            System.out.println("All good");
                        }
                    }
                }
                return point;
            } catch (IndexOutOfBoundsException e) {
                notFound = true;
            }
        }
        return new GridPoint2(0 , 0);
    }
}
