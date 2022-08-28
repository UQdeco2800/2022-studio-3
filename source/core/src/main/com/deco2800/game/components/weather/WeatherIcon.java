//package com.deco2800.game.components.weather;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.scenes.scene2d.ui.Image;
//import com.badlogic.gdx.scenes.scene2d.Actor;
//import com.deco2800.game.utils.random.PseudoRandom;
//
//public class WeatherIcon extends Actor {
//
//    /**
//     * Initiate the Image for the weather image.
//     */
//    private final Image weatherImage;
//
//    private final String[] weatherFile = {
//            "cloud.png",
//            "flood.png",
//            "foggy.png",
//            "heavyrain.png",
//            "lighting.png",
//            "night.png",
//            "partlycloudy.png",
//            "rainy.png",
//            "sleet.png",
//            "snowy.png",
//            "stormy.png",
//            "sunny.png",
//            "thunderstorm.png"
//    }
//
//    public WeatherIcon() {
//        this.weatherImage = new Image(new Texture("images/"+getRandomWeatherFile()));
//        this.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
//        this.setPosition(0f, 0f);
//        position();
//    }
//
//    private void position() {
//        weatherImage.setPosition(Gdx.graphics.getWidth()/2 - weatherImage.getWidth()/2, 1f);
//    }
//
//    private int getRandomWeatherFile() {
//        randomIndex = PseudoRandom.seedRandomInt(0, weatherFile.length);
//        return weatherFile[randomIndex];
//    }
//
//    @Override
//    public void draw(Batch batch) {
//        batch.draw(this.weatherImage);
//    }
//}