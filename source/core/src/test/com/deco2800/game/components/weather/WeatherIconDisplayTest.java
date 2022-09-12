package com.deco2800.game.components.weather;

import com.deco2800.game.components.weather.WeatherIconDisplay;

import com.deco2800.game.extensions.GameExtension;
import main.com.deco2800.game.components.weather.WeatherIconProperties;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.deco2800.game.components.weather.WeatherIcon;
import com.deco2800.game.services.GameTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class WeatherIconDisplayTest {
    WeatherIconDisplay weatherIconDisplay = new WeatherIconDisplay();

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
        assertNotNull(weatherIconDisplay.getTimerCountdown());
    }
}
