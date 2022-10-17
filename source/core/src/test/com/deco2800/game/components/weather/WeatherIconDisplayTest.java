package com.deco2800.game.components.weather;

import com.deco2800.game.components.weather.WeatherIconDisplay;

import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.components.weather.WeatherIconProperties;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.components.weather.WeatherIcon;
import com.deco2800.game.services.GameTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class WeatherIconDisplayTest {

    WeatherIconDisplay weatherIconDisplay;
    @BeforeEach
    public void setup() {
        ServiceLocator.registerTimeSource(new GameTime());
        weatherIconDisplay  = new WeatherIconDisplay();
    }

    @Test
    void checkNotNull() {
        assertNotNull(weatherIconDisplay);
    }

    @Test
    void checkTimerNotNull() {
        assertNotNull(weatherIconDisplay.getTimer());
    }

    @Test
    void checkWeatherIconNotNull() {
        assertNotNull(weatherIconDisplay.getWeatherIcon());
    }

    @Test
    void checkTimerCountdownNotNull() {
        ServiceLocator.registerTimeSource(new GameTime());
        assertNotNull(weatherIconDisplay.getTimerCountdown());
    }
}
