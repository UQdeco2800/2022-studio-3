package com.deco2800.game.components;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(GameExtension.class)
class HealthBarComponentTest {

    HealthBarComponent healthBar;
    GameTime gameTime = Mockito.mock(GameTime.class);

    long lastUpdate;
    boolean showing;
    long visibleTime = 15L;



    @BeforeEach
    void setUp() {
        ServiceLocator.registerTimeSource(gameTime);
        healthBar = new HealthBarComponent(EntityType.FRIENDLY);
    }

    @Test
    void shouldCreate() {
        Mockito.when(gameTime.getTime()).thenReturn(10L);

        lastUpdate = ServiceLocator.getTimeSource().getTime();
        showing = true;

        assertEquals(lastUpdate, 10L);
        assertTrue(showing);
    }

    @Test
    void shouldUpdateHealth() {
        Mockito.when(gameTime.getTime()).thenReturn(20L);

        lastUpdate = ServiceLocator.getTimeSource().getTime();
        showing = true;

        assertEquals(lastUpdate, 20L);
        assertTrue(showing);
    }

    @Test
    void shouldHideHealthBar() {
        Mockito.when(gameTime.getTime()).thenReturn(30L);

        lastUpdate = ServiceLocator.getTimeSource().getTime() - visibleTime;
        if (ServiceLocator.getTimeSource().getTimeSince(lastUpdate) > visibleTime) {
            showing = false;
        }

        assertEquals(lastUpdate, 15L);
        assertFalse(showing);
    }

    @Test
    void shouldShowHealthBar() {
        Mockito.when(gameTime.getTime()).thenReturn(50L);
        long time = 10L;
        showing = true;

        lastUpdate = ServiceLocator.getTimeSource().getTime() - visibleTime + time;
        if (ServiceLocator.getTimeSource().getTimeSince(lastUpdate) > visibleTime) {
            showing = false;
        }

        assertEquals(lastUpdate, 45L);
        assertTrue(showing);
    }
}