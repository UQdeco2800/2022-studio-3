package com.deco2800.game.entities.factories;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BuildingConfigs;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class BuildingFactoryTest {
    @Mock
    PhysicsService physicsService;


    @BeforeEach
    void setUp() {
        ServiceLocator.registerPhysicsService(physicsService);
    }

    @Test
    void createBaseBuilding() {
//        Entity building = BuildingFactory.createBaseBuilding();

    }
    @Test
    void createTownHall() {
    }

    @Test
    void createBarracks() {
    }

    @Test
    void createWall() {
    }
}