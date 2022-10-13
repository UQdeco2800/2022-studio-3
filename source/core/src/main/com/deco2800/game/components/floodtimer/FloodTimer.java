package com.deco2800.game.components.floodtimer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.areas.MapGenerator.FloodingGenerator;
import com.deco2800.game.components.weather.WeatherIconDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FloodTimer extends Actor {
    private static final Logger logger = LoggerFactory.getLogger(FloodTimer.class);

    /**
     * Reference to the flooding timer Image.
     */
    private Image floodTimerImage;

    public FloodTimer() {
        this.floodTimerImage = new Image(new Texture("images/flood_timer_1.png"));
    }

    public void layout() {
        this.floodTimerImage.setPosition(Gdx.graphics.getWidth()/2f + floodTimerImage.getWidth()/2f + 30f, Gdx.graphics.getHeight()-50f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.floodTimerImage.draw(batch, parentAlpha);
    }
}
