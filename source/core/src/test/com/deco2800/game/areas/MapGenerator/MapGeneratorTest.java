package com.deco2800.game.areas.MapGenerator;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class MapGeneratorTest {
    /**
     * Creates a MapGenerator with pre-defined values
     * @return map generator with static values for testing
     */
    private static MapGenerator createMapGenerator() {
        int mapWidth = 100;
        int mapHeight = 45;
        int citySize = 7;
        int islandSize = 80;
        return new MapGenerator(mapWidth, mapHeight, citySize, islandSize);
    }

    @Test
    public void newMapGeneratorTest() {
        //Ensures a MapGenerator is constructed successfully
        try {
            createMapGenerator();
        } catch (Exception e) {
            fail(); //Valid creation should throw no exception
        }
        MapGenerator mg;
        try {
            //Invalid island size
            mg = new MapGenerator(5, 5, 5, 1);
            fail();
        } catch (IllegalArgumentException e) {
            //Correct exception thrown
        } catch (Exception e) {
            //Incorrect exception thrown
            fail();
        }

        try {
            //Invalid width and height
            mg = new MapGenerator(-1, 0, 5, 3);
            fail();
        } catch (IllegalArgumentException e) {
            //Correct exception thrown
        } catch (Exception e) {
            //Incorrect exception thrown
            fail();
        }

        try {
            //Invalid city size
            mg = new MapGenerator(5, 5, 0, 1);
            fail();
        } catch (IllegalArgumentException e) {
            //Correct exception thrown
        } catch (Exception e) {
            //Incorrect exception thrown
            fail();
        }
    }

    @Test
    public void mapGetWidthTest() {
        //Ensures width getter functions
        MapGenerator mg = createMapGenerator();
        assertEquals(100, mg.getWidth());
    }

    @Test
    public void mapGetHeightTest() {
        //Ensures height getter functions
        MapGenerator mg = createMapGenerator();
        assertEquals(45, mg.getHeight());
    }

    @Test
    public void mapGetOceanCharTest() {
        //Ensures getOceanChar getter functions
        MapGenerator mg = createMapGenerator();
        assertEquals('*', mg.getOceanChar());
    }

    @Test
    public void mapGetIslandCharTest() {
        //Ensures getIslandChar getter functions
        MapGenerator mg = createMapGenerator();
        assertEquals('I', mg.getIslandChar());
    }

    @Test
    public void mapGetCityCharTest() {
        //Ensures getCityChar getter functions
        MapGenerator mg = createMapGenerator();
        assertEquals('c', mg.getCityChar());
    }

    @Test
    public void getMapTest() {
        //Ensures the map is returned as a valid map of char
        MapGenerator mg = createMapGenerator();
        char[][] map = mg.getMap();
        try {
            for (int i = 0; i < mg.getWidth(); i++) {
                for (int j = 0; j < mg.getHeight(); j++) {
                    char currentChar = map[j][i];
                    if (!(currentChar == mg.getOceanChar() || currentChar == mg.getIslandChar()
                            || currentChar == mg.getCityChar())) {
                        //An invalid char was returned in the map - fail
                        fail();
                    }
                }
            }
        } catch (Exception e) {
            //Any exception such as array index out of bounds exception means map was generated
            //improperly
            fail();
        }
    }
}
