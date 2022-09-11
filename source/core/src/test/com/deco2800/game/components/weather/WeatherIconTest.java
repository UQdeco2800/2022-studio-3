package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.utils.random.Timer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class WeatherIconTest {
    Skin countdownSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));
    Timer timer = new Timer(10000, 10001);
    Label timerCountdown = new Label(String.valueOf(timer.timeLeft()), countdownSkin);
    WeatherIcon weatherIcon = new WeatherIcon(timerCountdown);

//    @Test
//    void createIcon() {
//        Label sampleCountdown = new Label(weatherIcon.getTimerLabel(), countdownSkin);
//        assertEquals(weatherIcon, new WeatherIcon(sampleCountdown));
//    }
}
