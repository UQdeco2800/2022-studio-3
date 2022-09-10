package com.deco2800.game.utils.random;

import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.Timer;
import com.deco2800.game.services.GameTime;

public class Interrupt {
    /**
     * Call the GameTime class.
     */
    GameTime gT = new GameTime();

    /**
     * set the interrupt with lower bound and upper bound.
     * @param lowerBound: integer for the fastest time to wait for interrupt
     * @param upperBound: integer for the slowest time to wait for interrupt
     */
    public Interrupt(int lowerBound, int upperBound) {
        Timer t = new Timer(lowerBound,upperBound);
        while (!(t.isTimerExpired())) {
            continue;
        }
        gT.setTimeScale(0f);
    }
    /**
     * Resume the game regardless if it is interrupted or not
     */
    public void resumeGame() {
        gT.setTimeScale(1f);
    }
}

