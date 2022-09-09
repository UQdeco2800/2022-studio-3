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
    private static final Logger logger = LoggerFactory.getLogger(WeatherIconDisplay.class);
    private Timer timer;
    private WeatherIcon weatherIcon;
    private Label timerCountdown;

    public WeatherIconDisplay() {
        Skin countdownSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
        this.timer = new Timer(0, 3000);
        this.timerCountdown = new Label(String.valueOf(this.timer.timeLeft()), countdownSkin);
    }

    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("updateWeatherCountdown", this::updateWeatherCountdown);
    }

    private void addActors() {
        this.weatherIcon = new WeatherIcon(this.timerCountdown);
        this.timer = new Timer(0, 10000);
        stage.addActor(this.weatherIcon);
    }

    @Override
    public void update() {
        if (timer.isTimerExpired()) {
            this.weatherIcon.changeWeatherImage();
            ServiceLocator.getEntityService().trigger("changeWeather", this.weatherIcon.getMovementSpeed());
            this.timer = new Timer(0, 10000);
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