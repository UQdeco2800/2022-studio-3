package com.deco2800.game.components.building;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(GameExtension.class)
public class HealthAnimationTest {

    @Test
    void getStateAnimationTest() {
        HealthAnimation buildingState = new HealthAnimation();
        buildingState.updateHealth(Health.NORMAL);
        assertEquals("100-idle", buildingState.getAnimation());
        buildingState.updateHealth(Health.HALF);
        assertEquals("50", buildingState.getAnimation());
        buildingState.updateHealth(Health.DEAD);
        assertEquals("collapse", buildingState.getAnimation());
    }

    @Test
    void updateHealthTest(){
        HealthAnimation buildingState = new HealthAnimation();
        assertEquals(Health.NORMAL, buildingState.getHealth());
        buildingState.updateHealth(Health.HALF);
        assertEquals(Health.HALF, buildingState.getHealth());
    }

    @Test
    void getHealthTest(){
        HealthAnimation buildingState = new HealthAnimation();
        assertEquals(Health.NORMAL, buildingState.getHealth());
        buildingState.health = Health.DEAD;
        assertEquals(Health.DEAD, buildingState.getHealth());
    }
}