package com.deco2800.game.utils.random;

import java.time.*;
import java.lang.*;
import com.deco2800.game.utils.random.PseudoRandom;


public class RandomTimer {

    // Initiating the start and delay variable
    long start;
    long delay;

    // Assigning the delay to the lower bound and upperbound.
    public RandomTimer(int lowerIntervalSeconds, int upperIntervalSeconds) {
        int randomIntervalInt = PseudoRandom.seedRandomInt(lowerIntervalSeconds, upperIntervalSeconds);
        long delay = Long.valueOf(randomIntervalInt);
        this.delay = delay;
    }

    // Initiating start time
    public void start() {
        this.start = System.currentTimeMillis();
    }

    // Initiating if the countdown will expire or not
    public boolean isRandomTimerExpired() {
        return (System.currentTimeMillis() - this.start) > this.delay;
    }

    // Initiating method to display on debugger how many times left if it is ready
    public void timeLeft() {
        if (isRandomTimerExpired()) {
            System.out.println("Timer expired");
        } else{
            System.out.println("Time left = " + (this.delay - (System.currentTimeMillis() - this.start)));
        }
    }
}