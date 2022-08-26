package com.deco2800.game.utils.random;

import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.RandomTimer;
import com.deco2800.game.services.GameTime;

public class RandomInterrupt {

    /**
     * Interrupt the game if the timer is expired.
     * @return timeScale to 0 if the time runs out.
     */
    public void interruptGame() {
        RandomTimer rT = new RandomTimer(0,300); // Initial for example
        GameTime gT = new GameTime();
        if (rT.isRandomTimerExpired()) {
            gT.setTimeScale(0f); // No way to revert it back for now.
        }
    }

    /**
     * Resume the game regardles if it interrupted or not
     * @return timeScale to 1
     */
    public void resumeGame() {
        GameTime gT = new GameTime();
        gT.setTimeScale(1f);
    }
}

