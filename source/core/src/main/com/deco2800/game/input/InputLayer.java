package com.deco2800.game.input;

public class InputLayer {
    public final static int DEFAULT = (1 << 0);
    public final static int UI = (1 << 1); // use of menus, controls on the
    // stage
    public final static int GAME = (1 << 2); // Pausing, exiting etc.
    public final static int FRIENDLY = (1 << 3); // selection of
    // units/buildings, sending commands to them
    public final static int IN_USE = 0xf;
}
