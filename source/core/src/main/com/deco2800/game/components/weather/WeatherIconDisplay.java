package com.deco2800.game.components.weather;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherIconDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(WeatherIconDisplay.class);
    private WeatherIcon weatherIcon;

    @Override
    public void create() {
        super.create();
        addActors();
    }


    private void addActors() {
        this.weatherIcon = new WeatherIcon();
        stage.addActor(this.weatherIcon);
    }

    @Override
    public void draw(SpriteBatch batch) {}
}