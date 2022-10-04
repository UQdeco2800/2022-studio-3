package com.deco2800.game.input;

public class InputType {
    public final static short DEFAULT = (1 << 0);
    public final static short UI = (1 << 1); // use of menus, controls on the stage
    public final static short GAME = (1 << 2); // Pausing, exiting etc.
    public final static short FRIENDLY = (1 << 3); // selection of units/buildings,
                                                   // sending commands to them
}
