package com.deco2800.game.utils.random;

import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.RandomTimer;
import com.deco2800.game.services.GameTime;

public class RandomInterrupt {
    /**
     * Call the GameTime class.
     */
    GameTime gT = new GameTime();

    /**
     * set the random interrupt with lower bound and upper bound.
     * @return pause the game after random time between lowerBound and upperBound
     */
    public RandomInterrupt(int lowerBound, int upperBound) {
        RandomTimer rT = new RandomTimer(lowerBound,upperBound);
        while (!(rT.isRandomTimerExpired())) {
            continue;
        }
        gT.setTimeScale(0f);
    }
    /**
     * Resume the game regardles if it is interrupted or not
     * @return timeScale to 1
     */
    public void resumeGame() {
        gT.setTimeScale(1f);
    }
}

