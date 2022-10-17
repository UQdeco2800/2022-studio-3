package com.deco2800.game.services;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.TimeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/** Controls the game time */
public class GameTime {
  private static Logger logger = LoggerFactory.getLogger(GameTime.class);
  private final long startTime;
  private float timeScale = 1f;
  private static boolean ifPaused = false;
  private long pausedTime = 0;
  private long lastPaused;

  public GameTime() {
    startTime = TimeUtils.millis();
    logger.debug("Setting game start time to {}", startTime);
  }

  /**
   * Set the speed of time passing. This affects getDeltaTime()
   *
   * @param timeScale Time scale, where normal speed is 1.0, no time passing is 0.0
   */
  public void setTimeScale(float timeScale) {
    logger.debug("Setting time scale to {}", timeScale);
    this.timeScale = timeScale;
  }

  /** @return time passed since the last frame in seconds, scaled by time scale. */
  public float getDeltaTime() {
    return Gdx.graphics.getDeltaTime() * timeScale;
  }

  /** @return time passed since the last frame in seconds, not affected by time scale. */
  public float getRawDeltaTime() {
    return Gdx.graphics.getDeltaTime();
  }

  /** @return time passed since the game started in milliseconds */
  public long getTime() {
    return TimeUtils.timeSinceMillis(startTime);
  }

  public long getGameTime() {
    if (ifPaused()) {
      return lastPaused - pausedTime;
    }
    return getTime() - pausedTime;
  }

  public long getTimeSince(long lastTime) {
    return getTime() - lastTime;
  }

  /**
   * @return whether the game is paused
   */
  public static boolean ifPaused() {
      return ifPaused;
  }

  /**
   * Pauses the game
   */
  public void paused() {
    timeScale = 0f;
    ifPaused = true;
    lastPaused = getTime();
  }

  /**
   * Unpauses the game
   */
  public void unpaused() {
    timeScale = 1f;
    ifPaused = false;
    pausedTime += getTime() - lastPaused;
  }
}
