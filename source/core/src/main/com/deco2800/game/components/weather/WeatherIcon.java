package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.utils.random.PseudoRandom;
import com.deco2800.game.utils.random.Timer;
import com.badlogic.gdx.utils.Align;


public class WeatherIcon extends Actor {
    /**
     * Tracks time interval to change weather icon on display.
     */
    Timer timer;

    /**
     * To display the Timer
     */
    private final Label timerLabel;

    /**
     * Initiate the Image for the weather image.
     */
    private Image weatherImage;

    private Image weatherFilter;

    private final String[] weatherFile = {
            // Does not affect movememnt, affect lighting a little bit
            "images/cloudy.png",
            // Affecting movement a little bit, affect lighting
            "images/rainy.png", // "images/rainy.gif",
            // Affect movement a lot, affect terrain and lighting, must not appear adjacently with sunny
            "images/snowy.png", // "images/snowy.gif",
            // Does not affect movement, does not affect terrain and lighting, must not appear adjacently with snowy
            "images/sunny.png",
            // Affecting movement a little bit, affect lighting
            "images/thunderstorm.png" //"images/thunderstorm.gif"
    };
    private final String[] weatherFilterFile = {
            "images/weather-filter/cloudy-filter.png",
            "images/weather-filter/rainy-filter.png",
            "images/weather-filter/snowy-filter.png",
            "images/weather-filter/sunny-filter.png",
            "images/weather-filter/rainy-filter.png"
    };

    public WeatherIcon(Label countdownTimer) {


        /**
         * Initiate timerLabel with countdownTimer
         */
        this.timerLabel = countdownTimer;

        int index = PseudoRandom.seedRandomInt(0, weatherFile.length);
        /**
         * Initiate weatherImage
         */
        this.weatherImage = new Image(new Texture(weatherFile[index]));

        /**
         * Initiate weatherFilter
         */
        this.weatherFilter = new Image(new Texture(weatherFilterFile[index]));


        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout();
    }

    public Boolean hasTimerExpired() {
        return timer.isTimerExpired();
    }

    public void changeWeatherImage() {
        int index = PseudoRandom.seedRandomInt(0, weatherFile.length);
        this.weatherImage = new Image(new Texture(weatherFile[index]));
        this.weatherFilter = new Image(new Texture(weatherFilterFile[index]));
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout();

    }

    public void layout() {
        /**
         * For weatherFilter
         */
        weatherImage.setSize(1920f, 1080f);
        weatherFilter.setPosition(0,0);

        /**
         * For weatherImage
         */
        weatherImage.setSize(100f, 100f);
        weatherImage.setPosition(Gdx.graphics.getWidth()/2f-weatherImage.getWidth()/2f, Gdx.graphics.getHeight()-120f);

        /**
         * For timer
         */
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