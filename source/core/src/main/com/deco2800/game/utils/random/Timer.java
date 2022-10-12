package com.deco2800.game.utils.random;
import com.deco2800.game.services.GameTime;


public class Timer {
    /**
     * Reference to the game time.
     */
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
     * Finish time for the timer
     */
    private final long finish;

    /**
     * Completion flags.
     */
    private final byte flag20p  = 0b00000001;
    private final byte flag40p  = 0b00000011;
    private final byte flag60p  = 0b00000111;
    private final byte flag80p  = 0b00001111;
    private final byte flag100p = 0b00011111;

    /**
     * Completion values.
     */
    private long val20p;
    private long val40p;
    private long val60p;
    private long val80p;
    private long val100p;

    /**
     * Setting up timer to the class variable.
     *
     * @param lowerIntervalSeconds is for the lowest possible randomization value
     * @param upperIntervalSeconds is for the highest possible randomization value
     */
    public Timer(int lowerIntervalSeconds, int upperIntervalSeconds) {
        int randomIntervalInt = PseudoRandom.seedRandomInt(lowerIntervalSeconds, upperIntervalSeconds);
        long delay = Long.valueOf(randomIntervalInt);
        this.delay = delay;
        this.finish = delay + gt.getTime();
        this.initCompletionValues();
    }

    public void initCompletionValues() {
        long currentTime = gt.getTime();
        long interval = this.delay / 5;
        val20p = currentTime + (interval);
        val40p = currentTime + (interval * 2);
        val60p = currentTime + (interval * 3);
        val80p = currentTime + (interval * 4);
        val100p = currentTime + (interval * 5);
    }

    /**
     * Setting up the start variable to the current time.
     */
    public void start() {
        this.start = gt.getTime();
    }

    /**
     * Checking whether if the timer is already expired or not.
     */
    public boolean isTimerExpired() {
        return (gt.getTime() > this.finish);
    }

    /**
     * Checking whether if the timer is already expired or not.
     */
    public long timeLeft() {
        if (isTimerExpired()) {
            return 0;
        } else {
            return this.finish - gt.getTime();
        }
    }

    /**
     * Updates flags and returns them.
     */
    public byte getFlagStatus() {
        long currentTime = gt.getTime();
        if (currentTime > val100p) {
            return flag100p;
        } else if (currentTime > val80p) {
            return flag80p;
        } else if (currentTime > val60p) {
            return flag60p;
        } else if (currentTime > val40p) {
            return flag40p;
        } else if (currentTime > val20p) {
            return flag20p;
        } else {
            return 0b00000000;
        }
    }
}