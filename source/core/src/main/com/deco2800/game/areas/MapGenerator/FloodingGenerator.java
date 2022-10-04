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
    private Timer timer;

    /**
     * Creates the entity that manages flooding for the game.
     * @param atlantisTerrainFactory Terrain factory for the game area.
     * @param atlantisGameArea Main game area.
     */
    public FloodingGenerator(AtlantisTerrainFactory atlantisTerrainFactory, AtlantisGameArea atlantisGameArea) {
        this.atlantisTerrainFactory = atlantisTerrainFactory;
        this.atlantisGameArea = atlantisGameArea;
        this.startTimer();

        //TODO - How do we pause the timer when the game is paused?
        //TODO - IDEAS: Flash tile that is picked to be flooded next.
        //TODO - Visual Timer on the screen.
    }

    /**
     * Starts the countdown for flooding to occur.
     */
    private void startTimer() {
        this.timer = new Timer(900, 901);
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
        if (this.timer.isTimerExpired()) {
            triggerFloodEvent();
            this.startTimer();
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