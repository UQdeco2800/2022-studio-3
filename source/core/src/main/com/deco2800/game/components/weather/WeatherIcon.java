package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.Timer;
import com.badlogic.gdx.utils.Align;


public class WeatherIcon extends Actor {
    /**
     * To display the Timer
     */
    private final Label timerLabel;

    /**
     * Initiate the Image for the weather image.
     */
    private Image weatherImage;

    /**
     * Stores the value of the current index.
     */
    private int currentIndex;

    /**
     * Initiate the Image for the weather filter.
     */
    private Image weatherFilter;

    /**
     * Initiate the speed factor
     */
    private float speedFactor = 1f;

    private final String[] weatherFile = {
            // Does not affect movement, affect lighting of the environment
            "images/cloudy.png",
            // Affecting movement, affect lighting of the environment
            "images/rainy.png", // "images/rainy.gif"
            // Affect movement a lot, affect terrain and lighting of the environment, must not appear adjacently with sunny
            "images/snowy.png", // "images/snowy.gif"
            // Does not affect movement, does not affect terrain and lighting of the environment,
            // must not appear adjacently with snowy
            "images/sunny.png",
            // Affecting movement a bit, affect lighting of the environment
            "images/thunderstorm.png" //"images/thunderstorm.gif"
    };
    private final String[] weatherFilterFile = {
            "images/weather-filter/cloudy-filter.png",
            "images/weather-filter/rainy-filter.png",
            "images/weather-filter/snowy-filter.png",
            "images/weather-filter/sunny-filter.png",
            "images/weather-filter/thunderstorm-filter.png"
    };
    private final float[] movementSpeedFactor = {
            1.5f,
            0.5f,
            0.4f,
            2f,
            0.6f
    };

    public WeatherIcon(Label countdownTimer) {

        // Initiate timerLabel with countdownTimer
        this.timerLabel = countdownTimer;

        this.currentIndex = PseudoRandom.seedRandomInt(0, weatherFile.length);

        // Initiate weatherImage
        this.weatherImage = new Image(new Texture(weatherFile[this.currentIndex]));

        // Initiate weatherFilter
        this.weatherFilter = new Image(new Texture(weatherFilterFile[this.currentIndex]));

        // Initiate speedFactor
        this.speedFactor = movementSpeedFactor[this.currentIndex];

        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        layout();
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public void changeWeatherImage() {
        while (true) {
            int index = PseudoRandom.seedRandomInt(0, weatherFile.length);
            if (index != this.currentIndex) {
                this.currentIndex = index;
                break;
            }
        }
        this.weatherImage = new Image(new Texture(weatherFile[this.currentIndex]));
        this.weatherFilter = new Image(new Texture(weatherFilterFile[this.currentIndex]));
        this.speedFactor = this.movementSpeedFactor[this.currentIndex];
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout();
    }

    public float getMovementSpeed() {
        return this.speedFactor;
    }

    public void layout() {
        //  Layout for weatherFilter
        weatherImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        weatherFilter.setPosition(0,0);

        //  Layout for weatherImage
        weatherImage.setSize(100f, 100f);
        weatherImage.setPosition(Gdx.graphics.getWidth()/2f-weatherImage.getWidth()/2f, Gdx.graphics.getHeight()-120f);

        //  Layout for timer
        this.timerLabel.setAlignment(Align.left);
        this.timerLabel.setWrap(true);
        this.timerLabel.setSize(3f,3f);
        this.timerLabel.setPosition(Gdx.graphics.getWidth()/2f + weatherImage.getWidth()/2f + 10f, Gdx.graphics.getHeight()-75f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.weatherFilter.draw(batch, parentAlpha);
        this.weatherImage.draw(batch, parentAlpha);
        this.timerLabel.draw(batch, parentAlpha);
    }


    public String getTimerLabel() {
        return this.timerLabel.getText().toString();
    }

    public void setTimerLabel(String countdownTimer) {
        this.timerLabel.setText(countdownTimer);
    }

}