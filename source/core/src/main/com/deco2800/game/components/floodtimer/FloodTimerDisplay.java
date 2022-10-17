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

    public FloodTimerDisplay(FloodingGenerator floodingGenerator) {
        this.floodingGenerator = floodingGenerator;
        this.floodTimer = new FloodTimer();
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
                floodTimer.requestNewImage(100);
            } else if (floodingGenerator.status80p) {
                floodTimer.requestNewImage(80);
            } else if (floodingGenerator.status60p) {
                floodTimer.requestNewImage(60);
            } else if (floodingGenerator.status40p) {
                floodTimer.requestNewImage(40);
            } else if (floodingGenerator.status20p) {
                floodTimer.requestNewImage(20);
            } else {
                floodTimer.requestNewImage(0);
            }
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        this.floodTimer.remove();
    }

    /**
     * Draws to the terminal.
     * @param batch SpriteBatch batch
     */
    @Override
    public void draw(SpriteBatch batch) {
        // ensures super draw method is not run
    }
}
