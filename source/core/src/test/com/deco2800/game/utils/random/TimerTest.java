package com.deco2800.game.utils.random;

import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.random.Timer;
import com.deco2800.game.services.GameTime;

import com.deco2800.game.extensions.GameExtension;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class TimerTest {

    @BeforeEach
    public void setup() {
        ServiceLocator.registerTimeSource(new GameTime());
    }

    @Test
    void checkTimeLeftBetweenRange() {
        Timer timer = new Timer(0, 1000);
        assertTrue(timer.timeLeft() > 0 &&  timer.timeLeft() < 1000);
    }

    @Test
    void checkTimerExpires() {
        Boolean check = false;
        GameTime gt = new GameTime();
        long start = gt.getTime();
        Timer timer = new Timer(0, 1000);
        long timeLeft = timer.timeLeft() + 10;
        while ((gt.getTime() - start) < timeLeft) {
            if (timer.isTimerExpired()) {
                check = true;
            }
        }
        assertTrue(check);
    }
}
