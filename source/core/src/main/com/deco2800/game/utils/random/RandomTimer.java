package com.deco2800.game.utils.random;

import java.time.*;
import java.lang.*;
import com.deco2800.game.utils.random.PseudoRandom;


public class RandomTimer {

    /**
     * Initiating the start variable.
     */
    long start;

    /**
     * Initiating the delay variable.
     */
    long delay;

    /**
     * Setting up random timer to the class variable.
     * @return setting delay for the RandomTimer class.
     */
    public RandomTimer(int lowerIntervalSeconds, int upperIntervalSeconds) {
        int randomIntervalInt = PseudoRandom.seedRandomInt(lowerIntervalSeconds, upperIntervalSeconds);
        long delay = Long.valueOf(randomIntervalInt);
        this.delay = delay;
    }

    /**
     * Setting up the start variable to the current time.
     * @return setting start variable with the current time for the RandomTimer class.
     */
    public void start() {
        this.start = System.currentTimeMillis();
    }

    /**
     * Checking whether or not if the timer is already expired or not.
     * @return boolean of time expiration.
     */
    public boolean isRandomTimerExpired() {
        return (System.currentTimeMillis() - this.start) > this.delay;
    }

    /**
     * Checking whether or not if the timer is already expired or not.
     * @return Time left if it exists or time expiration info if it is not.
     */
    public void timeLeft() {
        if (isRandomTimerExpired()) {
            System.out.println("Timer expired");
        } else {
            System.out.println("Time left = " + (this.delay - (System.currentTimeMillis() - this.start)));
        }
    }
}