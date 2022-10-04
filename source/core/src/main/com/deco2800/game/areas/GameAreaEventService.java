package com.deco2800.game.areas;

import com.deco2800.game.events.EventHandler;

/**
 * A global event handler for the game, this can be used to invoke events that trigger
 * functions dependent on access to objects in the game area, for example, spawning
 * units and buildings. Since it's part of the service locator.
 */
public class GameAreaEventService {
    private final EventHandler gameAreaEventHandler = new EventHandler();

    public EventHandler getEventHandler() {
        return gameAreaEventHandler;
    }
}
