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
    /**
     * Handles logging information.
     */
    private static final Logger logger = LoggerFactory.getLogger(FloodTimer.class);

    /**
     * Reference to the flooding timer Image.
     */
    private Image floodTimerImage;

    /**
     * Path for image files
     */
    private final String TIMER20PC = "images/FloodTimer/flood_timer_20pc.png";
    private final String TIMER40PC = "images/FloodTimer/flood_timer_40pc.png";
    private final String TIMER60PC = "images/FloodTimer/flood_timer_60pc.png";
    private final String TIMER80PC = "images/FloodTimer/flood_timer_80pc.png";
    private final String TIMER100PC = "images/FloodTimer/flood_timer_100pc.png";


    public FloodTimer() {
        this.floodTimerImage = new Image(new Texture(TIMER20PC));
        layout();
    }

    public void requestNewImage(int completion) {
        switch (completion) {
            case 80 -> this.changeImage(TIMER100PC);
            case 60 -> this.changeImage(TIMER80PC);
            case 40 -> this.changeImage(TIMER60PC);
            case 20 -> this.changeImage(TIMER40PC);
            case 0 -> this.changeImage(TIMER20PC);
        }
    }

    public void changeImage(String timer) {
        this.floodTimerImage = new Image(new Texture(timer));
        layout();
    }

    public void layout() {
        floodTimerImage.setSize(160f, 40f);
        this.floodTimerImage.setPosition(Gdx.graphics.getWidth()/2f + floodTimerImage.getWidth()/2f + 180f, Gdx.graphics.getHeight()-50f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.floodTimerImage.draw(batch, parentAlpha);
    }
}
