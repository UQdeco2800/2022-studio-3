package com.deco2800.game.areas.MapGenerator.pathBuilding;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.areas.MapGenerator.Buildings.BuildingGenerator;
import com.deco2800.game.extensions.GameExtension;

@ExtendWith(GameExtension.class)
public class PathGeneratorTest {
    private BuildingGenerator bg;
    private PathGenerator pg;

    @BeforeEach
    void setup() {
        this.bg = new BuildingGenerator(new MapGenerator(10, 10, 10, 10, 10));
        this.pg = new PathGenerator(bg);
    }
}
