package com.deco2800.game.components.flooding;

import com.deco2800.game.components.Component;
import com.deco2800.game.utils.random.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloodingComponent extends Component {
    /**
     * To create logging information.
     */
    private static final Logger logger = LoggerFactory.getLogger(FloodingComponent.class);

    /**
     * Timer that determines when a flooding event will occur.
     */
    private Timer timer;

    /**
     * Initialize the flooding timer to flood every 10 seconds.
     */
    public FloodingComponent () {
        this.timer = new Timer(10000, 10000);
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void update() {
        if (this.timer.isTimerExpired()) {
            //TODO - Trigger flooding event

            // Reset the timer for next flooding event.
            this.timer = new Timer(10000, 10000);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}
