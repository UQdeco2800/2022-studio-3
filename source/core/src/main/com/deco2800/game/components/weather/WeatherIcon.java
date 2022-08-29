package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.Timer;

public class WeatherIcon extends Actor {
    /**
     * Tracks time interval to change weather icon on display.
     */
    Timer timer;

    /**
     * Initiate the Image for the weather image.
     */
    private final Image weatherImage;

    private final String[] weatherFile = {
            "images/cloudy.png",
            "images/flood.png",
            "images/foggy.png",
            "images/heavyrain.png",
            "images/lighting.png",
            "images/night.png",
            "images/partlycloudy.png",
            "images/rainy.png",
            "images/sleet.png",
            "images/snowy.png",
            "images/stormy.png",
            "images/sunny.png",
            "images/thunderstorm.png"
    };

    public WeatherIcon() {
        this.timer = new Timer(0, 30);
        this.weatherImage = new Image(new Texture(getRandomWeatherFileLocation()));
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout();
    }

    public Boolean hasTimerExpired() {
        return timer.isTimerExpired();
    }

    public void layout() {
        weatherImage.setSize(128f, 128f);
        weatherImage.setPosition(Gdx.graphics.getWidth()/2f - weatherImage.getWidth()/2f, Gdx.graphics.getHeight()-160f);
    }

    public String getRandomWeatherFileLocation() {
        int randomIndex = PseudoRandom.seedRandomInt(0, weatherFile.length);
        return weatherFile[randomIndex];
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.weatherImage.draw(batch, parentAlpha);
    }
}