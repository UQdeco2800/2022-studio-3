package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.utils.random.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherIconDisplay extends UIComponent {
    /**
     * To create logging information.
     */
    private static final Logger logger = LoggerFactory.getLogger(WeatherIconDisplay.class);

    /**
     * Timer for the object.
     */
    private Timer timer;

    /**
     * WeatherIcon object for the display.
     */
    private final WeatherIcon weatherIcon;

    /**
     * Countdown Label to display properties to the screen.
     */
    private final Label timerCountdown;

    public WeatherIconDisplay() {
        Skin countdownSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        this.timer = new Timer(10000, 20000);
        this.timerCountdown = new Label(String.valueOf(this.timer.timeLeft()), countdownSkin);
        this.weatherIcon = new WeatherIcon(this.timerCountdown);
    }

    /**
     * Creates weather icon object for the display.
     */
    @Override
    public void create() {
        super.create();
        stage.addActor(this.weatherIcon);
    }

    /**
     * Checks the status of the weather each time update is called.
     */
    @Override
    public void update() {
        ServiceLocator.getEntityService().trigger("changeWeather", this.weatherIcon.getMovementSpeed());
        if (timer.isTimerExpired()) {
            this.weatherIcon.changeWeatherImage();
            this.timer = new Timer(10000, 20000);
        }
        int timeLeft = (int) timer.timeLeft() / 1000;
        CharSequence text = String.format("%02d", timeLeft);
        this.timerCountdown.setText(text);
    }

    /**
     * Removes the weather icon from the screen when called.
     */
    @Override
    public void dispose() {
        super.dispose();
        weatherIcon.remove();
        timerCountdown.remove();
    }

    /**
     * Getter method to get the timer object associated with the WeatherDisplay.
     * @return Timer object.
     */
    public Timer getTimer() {
        return this.timer;
    }

    /**
     * Getter method to get the WeatherIcon associated with the object.
     * @return WeatherIcon object.
     */
    public WeatherIcon getWeatherIcon() {
        return this.weatherIcon;
    }

    /**
     * Getter method to get the timer countdown associated with the object.
     * @return Label timer-countdown.
     */
    public Label getTimerCountdown() {
        return this.timerCountdown;
    }

    /**
     * Draws to the terminal.
     * @param batch SpriteBatch batch
     */
    @Override
    public void draw(SpriteBatch batch) {}
}