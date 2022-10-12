package com.deco2800.game.components.floodtimer;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.areas.MapGenerator.FloodingGenerator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloodTimerDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(FloodTimerDisplay.class);
    private final FloodingGenerator floodingGenerator;
    private final FloodTimer floodTimer;

    public FloodTimerDisplay(FloodingGenerator floodingGenerator, FloodTimer floodTimer) {
        this.floodingGenerator = floodingGenerator;
        this.floodTimer = floodTimer;
    }

    /**
     * Creates weather icon object for the display.
     */
    @Override
    public void create() {
        super.create();
        stage.addActor(this.floodTimer);
    }

    @Override
    public void update() {
        Boolean hasUpdated = floodingGenerator.updateFlags();
        if (hasUpdated) {
            if (floodingGenerator.status100p) {
                //TODO - display 100p on screen
            } else if (floodingGenerator.status80p) {
                //TODO - display 80p on screen
            } else if (floodingGenerator.status60p) {
                //TODO - display 60p on screen
            } else if (floodingGenerator.status40p) {
                //TODO - display 40p on screen
            } else if (floodingGenerator.status20p) {
                //TODO - display 20p on screen
            } else {
                //TODO - add empty timer
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        //this.floodTimer.dispose();
    }

    /**
     * Draws to the terminal.
     * @param batch SpriteBatch batch
     */
    @Override
    public void draw(SpriteBatch batch) {}
}
