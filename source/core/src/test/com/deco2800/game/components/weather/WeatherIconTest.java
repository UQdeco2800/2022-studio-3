package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.random.Timer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.components.weather.WeatherIcon;
import com.deco2800.game.services.GameTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class WeatherIconTest {
    Skin countdownSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    Timer timer;
    Label timerCountdown;
    Label timerCountdown2;
    WeatherIcon weatherIcon;

    @BeforeEach
    public void setup() {
        ServiceLocator.registerTimeSource(new GameTime());
        timer  = new Timer(5000, 10001);
        timerCountdown  = new Label(String.valueOf(timer.timeLeft()),
                countdownSkin);
        timerCountdown2 = new Label(String.valueOf(timer.timeLeft() + 2),
                countdownSkin);
        weatherIcon = new WeatherIcon(timerCountdown);
    }

    @Test
    void checkTimerExpires() {
        Boolean check = false;
        GameTime gt = new GameTime();
        long start = gt.getTime();
        long timeLeft = timer.timeLeft() + 100;
        while ((gt.getTime() - start) < timeLeft) {
            if (timer.isTimerExpired()) {
                check = true;
            }
        }
        assertTrue(check);
    }

    @Test
    void checkTimerLabelNotNull() {
        assertNotNull(weatherIcon.getTimerLabel());
    }

    @Test
    void checkCountdownSkinNotNull() {
        assertTrue(countdownSkin != null);
    }

    @Test
    void checkSpeedFactorChanges() {
        assertTrue(weatherIcon.getMovementSpeed() != 1f);
    }

    @Test
    void checkWeatherImageChanges() {
        int initial_index = this.weatherIcon.getCurrentIndex();
        this.weatherIcon.changeWeatherImage();
        int new_index = this.weatherIcon.getCurrentIndex();
        assertTrue(initial_index != new_index);
    }

    @Test
    void createIcon() {
        assertNotNull(weatherIcon);
    }

    @Test
    void checkGetTimerLabel() {
        assertTrue(weatherIcon.getTimerLabel() == timerCountdown);
    }

    @Test
    void checkSetTimerLabel() {
        Label init = weatherIcon.getTimerLabel();
        weatherIcon.setTimerLabel(timerCountdown2);
        assertTrue(init != weatherIcon.getTimerLabel());
    }
}
