package com.deco2800.game.areas.MapGenerator;
import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Map;

import static org.junit.Assert.*;

@ExtendWith(GameExtension.class)
public class ResourceGeneratorTest {
    /**
     * Creates a MapGenerator with pre-defined values
     * @return map generator with static values for testing
     */
    private static MapGenerator createMapGenerator() {
        int mapWidth = 100;
        int mapHeight = 45;
        int cityWidth = 7;
        int cityHeight = 7;
        int islandSize = 80;
        return new MapGenerator(mapWidth, mapHeight, cityWidth, cityHeight, islandSize);
    }

    @Test
    public void readJsonTest() {
        MapGenerator mg = createMapGenerator();
        ResourceGenerator rg = new ResourceGenerator(mg);
    }
}
