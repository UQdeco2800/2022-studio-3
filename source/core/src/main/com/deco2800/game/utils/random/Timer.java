package com.deco2800.game.utils.random;
import com.deco2800.game.services.GameTime;


public class Timer {

    GameTime gt = new GameTime();

    /**
     * Initiating the start variable.
     */
    long start;

    /**
     * Initiating the delay variable.
     */
    public long delay;

    /**
     * Setting up timer to the class variable.
     *
     * @param lowerIntervalSeconds is for the lowest possible randomization value
     * @param upperIntervalSeconds is for the highest possible randomization value
     *
     * @return setting delay for the Timer class.
     */
    public Timer(int lowerIntervalSeconds, int upperIntervalSeconds) {
        int randomIntervalInt = PseudoRandom.seedRandomInt(lowerIntervalSeconds, upperIntervalSeconds);
        long delay = Long.valueOf(randomIntervalInt);
        this.delay = delay;
    }

    /**
     * Setting up the start variable to the current time.
     * @return setting start variable with the current time for the Timer class.
     */
    public void start() {
        this.start = gt.getTime();
    }

    /**
     * Checking whether if the timer is already expired or not.
     * @return boolean of time expiration.
     */
    public boolean isTimerExpired() {
        return (gt.getTime() - this.start) > this.delay;
    }

    /**
     * Checking whether if the timer is already expired or not.
     * @return Time left if it exists or time expiration info if it is not.
     */
    public long timeLeft() {
        if (isTimerExpired()) {
            System.out.println("Timer expired");
            return 0;
        } else {
//            System.out.println("Time left = " + (this.delay - (gt.getTime() - this.start)));
            return this.delay - (gt.getTime() - this.start);
        }
    }
}