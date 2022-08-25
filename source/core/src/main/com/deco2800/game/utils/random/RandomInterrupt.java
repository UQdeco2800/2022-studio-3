package com.deco2800.game.utils.random;

import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.RandomTimer;
import com.deco2800.game.services.GameTime;

public class RandomInterrupt {

    // Interrupt the game when the timer is reached
    public void interruptGame() {
        RandomTimer rT = new RandomTimer(0,300); // Initial for example
        GameTime gT = new GameTime();
        if (rT.isRandomTimerExpired()) {
            gT.setTimeScale(0f); // No way to revert it back for now.
        }
    }
}

