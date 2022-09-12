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
import com.deco2800.game.components.weather.WeatherIcon;

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
        this.timer = new Timer(5000, 10001);
        this.timerCountdown = new Label(String.valueOf(this.timer.timeLeft()), countdownSkin);
        this.weatherIcon = new WeatherIcon(this.timerCountdown);
    }

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateWeatherCountdown", this::updateWeatherCountdown);
    }

    private void addActors() {
        stage.addActor(this.weatherIcon);
    }

    @Override
    public void update() {
        ServiceLocator.getEntityService().trigger("changeWeather", this.weatherIcon.getMovementSpeed());
        if (timer.isTimerExpired()) {
            this.weatherIcon.changeWeatherImage();
            this.timer = new Timer(5000, 10001);
        }
        int timeLeft = (int) timer.timeLeft() / 1000;
        CharSequence text = String.format("%d", timeLeft);
        timerCountdown.setText(text);
    }

    @Override
    public void dispose() {
        super.dispose();
        weatherIcon.remove();
        timerCountdown.remove();
    }

    public void updateWeatherCountdown(int health) {
        CharSequence text = String.format("%d", timer.timeLeft());
        timerCountdown.setText(text);
    }

    @Override
    public void draw(SpriteBatch batch) {}
}