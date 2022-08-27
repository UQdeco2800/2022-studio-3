package com.deco2800.game.events.listeners;

/**
 * An event listener with 4 arguments
 */
@FunctionalInterface
public interface EventListener4<T0, T1, T2, T4> extends EventListener {
    void handle(T0 arg0, T1 arg1, T2 arg2, T4 arg3);
}
