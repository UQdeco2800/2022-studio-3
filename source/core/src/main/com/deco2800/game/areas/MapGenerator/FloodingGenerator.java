package com.deco2800.game.areas.MapGenerator;

import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.map.MapService;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.utils.random.Timer;
import com.deco2800.game.components.Component;
import com.deco2800.game.utils.random.PseudoRandom;

import java.util.Map;
import java.util.Random;

public class FloodingGenerator extends Component {
    /**
     * Terrain factory of the game to be updated as flooding occurs.
     */
    private final AtlantisTerrainFactory atlantisTerrainFactory;
    /**
     * The game area for the main screen, re-renders tiles when flooding occurs.
     */
    private AtlantisGameArea atlantisGameArea;
    /**
     * Flooding timer to signal flooding event.
     */
    public Timer timer;
    /**
     * Flag for 20% complete
     */
    public Boolean status20p;
    /**
     * Flag for 40% complete
     */
    public Boolean status40p;
    /**
     * Flag for 60% complete
     */
    public Boolean status60p;
    /**
     * Flag for 80% complete
     */
    public Boolean status80p;
    /**
     * Flag for 100% complete
     */
    public Boolean status100p;
    /**
     * Constant value of flood timer (ms)
     */
    private final int floodDuration = 4000;
    /**
     * Stores current progress update
     */
    private byte progress;

    /**
     * Creates the entity that manages flooding for the game.
     * @param atlantisTerrainFactory Terrain factory for the game area.
     * @param atlantisGameArea Main game area.
     */
    public FloodingGenerator(AtlantisTerrainFactory atlantisTerrainFactory, AtlantisGameArea atlantisGameArea) {
        this.atlantisTerrainFactory = atlantisTerrainFactory;
        this.atlantisGameArea = atlantisGameArea;
        this.resetFlags();
        this.startTimer();

        //TODO - How do we pause the timer when the game is paused?
        //TODO - IDEAS: Flash tile that is picked to be flooded next.
        //TODO - Visual Timer on the screen.
    }

    /**
     * Sets all completion flags to false.
     */
    public void resetFlags() {
        this.status20p = false;
        this.status40p = false;
        this.status60p = false;
        this.status80p = false;
        this.status100p = false;
        this.progress = 0b00000000;
    }

    /**
     * Starts the countdown for flooding to occur.
     */
    public void startTimer() {
        this.timer = new Timer(floodDuration, floodDuration + 1);
        this.timer.start();
    }

    /**
     * Create the component in the game.
     */
    @Override
    public void create() {
        super.create();
    }

    /**
     * To be called each time update is called.
     */
    @Override
    public void update() {
        this.updateFlags();
        if (this.timer.isTimerExpired()) {
            triggerFloodEvent();
            this.resetFlags();
            this.startTimer();
        }
    }

    public Boolean updateFlags() {

        //Obtain completion status
        byte currentProgress = timer.getFlagStatus();

        if (progress == currentProgress) {
            return false;
        } else {
            progress = currentProgress;
            this.setFlags();
            return true;
        }
    }

    public void setFlags() {
        //Flags for completion
        byte flag20p  = 0b00000001;
        byte flag40p  = 0b00000010;
        byte flag60p  = 0b00000100;
        byte flag80p  = 0b00001000;
        byte flag100p = 0b00010000;

        //Update flags
        if ((progress & flag100p) == flag100p) {
            this.status100p = true;
        } else if ((progress & flag80p) == flag80p) {
            this.status80p = true;
        } else if ((progress & flag60p) == flag60p) {
            this.status60p = true;
        } else if ((progress & flag40p) == flag40p) {
            this.status40p = true;
        } else if ((progress & flag20p) == flag20p) {
            this.status20p = true;
        }
    }

    /**
     * Remove the entity from the game.
     */
    @Override
    public void dispose() {
        super.dispose();
    }

    /**
     * Algorithm that determines which tiles to be flooded on the next flooding event.
     * The call to atlantisTerrainFactory updates the structure of mapGenerator.
     */
    public void triggerFloodEvent() {
        this.atlantisTerrainFactory.floodTiles();
        this.atlantisGameArea.flood();
    }
}