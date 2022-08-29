package com.deco2800.game.components.weather;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.utils.random.Timer;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
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
            this.dispose();
            addActors();
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