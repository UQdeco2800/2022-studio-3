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

    private final String[] weatherFile = {
            "images/cloudy.png",			// Does not affect movememnt 			// Affect lighting a little bit
            "images/rainy.png",			    // Affecting movement a little bit		// Affect lighting
            "images/snowy.png",			    // Affect movement a lot			    // Affect terrain and lighting 		        // Must not appear adjacently with sunny
            "images/sunny.png",			    // Does not affect movement			    // Does not affect terrain and lighting 	// Must not appear adjacently with snowy
            "images/thunderstorm.png"		// Affecting movement a little bit		// Affect lighting
    };

    public WeatherIcon(Label countdownTimer) {

        /**
         * Initiate timerLabel with countdownTimer
         */
        this.timerLabel = countdownTimer;

        /**
         * Initiate weatherImage
         */
        this.weatherImage = new Image(new Texture(getRandomWeatherFileLocation()));

        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout();
    }

    public Boolean hasTimerExpired() {
        return timer.isTimerExpired();
    }

    public void changeWeatherImage() {
        this.weatherImage = new Image(new Texture(getRandomWeatherFileLocation()));
        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        layout();

    }

    public void layout() {
        /**
         * For weatherImage
         */
        weatherImage.setSize(100f, 100f);
        weatherImage.setPosition(Gdx.graphics.getWidth()/2f - weatherImage.getWidth()/2f, Gdx.graphics.getHeight()-120f);

        /**
         * For timer
         */
        this.timerLabel.setAlignment(Align.left);
        this.timerLabel.setWrap(true);
        this.timerLabel.setSize(3f,3f);
        this.timerLabel.setPosition(Gdx.graphics.getWidth()/2f + weatherImage.getWidth()/2f + 10f, Gdx.graphics.getHeight()-75f);
    }

    public String getRandomWeatherFileLocation() {
        int randomIndex = PseudoRandom.seedRandomInt(0, weatherFile.length);
        return weatherFile[randomIndex];
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
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