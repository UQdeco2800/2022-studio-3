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
    private Label timerLabel;

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

    /**
     * Access image files, ordered relative to index.
     */
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

    /**
     * Access image filter files, ordered relative to index.
     */
    private final String[] weatherFilterFile = {
            "images/weather-filter/cloudy-filter.png",
            "images/weather-filter/rainy-filter.png",
            "images/weather-filter/snowy-filter.png",
            "images/weather-filter/sunny-filter.png",
            "images/weather-filter/thunderstorm-filter.png"
    };

    /**
     * Access weather movement speed factor, ordered relative to index.
     */
    private final float[] movementSpeedFactor = {
            1.5f,
            0.5f,
            0.4f,
            2f,
            0.6f
    };

    /**
     * Instantiates WeatherIcon class. Upon creation, a random index is selected
     * to pick the initial weather and corresponding effects in the game.
     *
     * @param countdownTimer A Label representing a countdown timer to be
     *                       displayed in the game.
     */
    public WeatherIcon(Label countdownTimer) {
        // Initiate timerLabel with countdownTimer
        this.timerLabel = countdownTimer;

        // Randomly select first index
        this.currentIndex = PseudoRandom.seedRandomInt(0, weatherFile.length);

        // Initiate weatherImage
        this.weatherImage = new Image(new Texture(weatherFile[this.currentIndex]));

        // Initiate weatherFilter
        this.weatherFilter = new Image(new Texture(weatherFilterFile[this.currentIndex]));

        // Initiate speedFactor
        this.speedFactor = movementSpeedFactor[this.currentIndex];
        layout();
    }

    /**
     * Getter method to access the current index of the Weather Icon.
     *
     * @return Int representing index of the weather icon.
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * This method is called when the timer for the weather expires. It
     * pseudo-randomly seeds a new index that is not the same as the
     * previous index, and changes the relevant properties based on the
     * new selected index.
     */
    public void changeWeatherImage() {
        // Obtain a new index
        int index;
        do {
            index = PseudoRandom.seedRandomInt(0, weatherFile.length);
        } while (index == this.currentIndex);
        this.currentIndex = index;

        // Update display from new index
        this.weatherImage = new Image(new Texture(weatherFile[this.currentIndex]));
        this.weatherFilter = new Image(new Texture(weatherFilterFile[this.currentIndex]));
        this.speedFactor = this.movementSpeedFactor[this.currentIndex];
        layout();
    }

    /**
     * Getter method to obtain the movement speed factor.
     *
     * @return Float value representing movement speed.
     */
    public float getMovementSpeed() {
        return this.speedFactor;
    }

    /**
     * Sets the layout of the weather, displaying the weather icon and the
     * filter for a given index.
     */
    public void layout() {
        //  Layout for weatherFilter
        weatherFilter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
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

    /**
     * Draws the weather layout to the game screen.
     *
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        this.weatherFilter.draw(batch, parentAlpha);
        this.weatherImage.draw(batch, parentAlpha);
        this.timerLabel.draw(batch, parentAlpha);
    }

    /**
     * Getter method to obtain the timer label.
     *
     * @return A timer label representing the timer label of the object.
     */
    public Label getTimerLabel() {
        return this.timerLabel;
    }

    /**
     * Setter method to set the timer label.
     *
     * @param countdownTimer A new Label to set the timer label to.
     */
    public void setTimerLabel(Label countdownTimer) {
        this.timerLabel = countdownTimer;
    }

}