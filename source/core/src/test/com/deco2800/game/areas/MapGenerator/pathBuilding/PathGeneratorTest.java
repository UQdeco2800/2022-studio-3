package com.deco2800.game.areas.MapGenerator.pathBuilding;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingSpecification;
import com.deco2800.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
class PathGeneratorTest {
    private BuildingGenerator bg;
    private PathGenerator pg;

    @BeforeAll
    void setup() {
        int mapWidth = 200;
        int mapHeight = 90;
        int cityWidth = 50;
        int cityHeight = 44;
        int islandSize = 80;
        this.bg = new BuildingGenerator(new MapGenerator(mapWidth, mapHeight, cityWidth, cityHeight, islandSize));
        this.pg = new PathGenerator(this.bg);
    }

    @Test
    void noPathsThroughBuildingsTest() {
        char[][] original = this.bg.getCharMap();
        int numBuildingChars = 0;
        for (int row = 0; row < original.length; row ++) {
            for (int col = 0; col < original[row].length; col++) {
                char c = original[row][col];
                if (c != '*') {
                    numBuildingChars++;
                }
            }
        }
        char[][] paths = this.pg.getCity();
        int numBuildingChars2 = 0;
        for (int row = 0; row < paths.length; row ++) {
            for (int col = 0; col < paths[row].length; col++) {
                char c = paths[row][col];
                if (c != '*' && c != 'P' && c != 'N') {
                    numBuildingChars2++;
                }
            }
        }
        assertTrue(numBuildingChars == numBuildingChars2, "Paths are being constructde through buildings");
    }

    @Test
    void shouldPlaceADoorForEveryBuilding() {
        List<BuildingSpecification> l = this.bg.getBuildings();
        int nb = 0;
        for (BuildingSpecification b : l) {
            if (b.numToBuild() > 0) {
                nb++;
            }
        }
        char[][] paths = this.pg.getCity();
        int numDoors = 0;
        for (int row = 0; row < paths.length; row ++) {
            for (int col = 0; col < paths[row].length; col++) {
                char c = paths[row][col];
                if (c == 'D') {
                    numDoors++;
                }
            }
        }
        assertTrue(numDoors == nb, "A door has not been placed for every building");
    }

    @Test
    void shouldPlacePathAfterDoor() {
        char[][] paths = this.pg.getCity();
        for (int row = 0; row < paths.length; row ++) {
            for (int col = 0; col < paths[row].length; col++) {
                char c = paths[row][col];
                if (c == 'D') {
                    boolean flag = false;
                    for (int i = -1; i <= 1; i++) {
                        for (int j = -1; j <= 1; j++) {
                            if (!(i == 0 && j == 0) && (i == 0 || j == 0) && paths[row + i][col + j] == 'P') {
                                flag = true;
                            }
                        }
                    }
                    assertTrue(flag, "Paths are not being placed correctly in front of doors");
                }
            }
        }
    }
}
