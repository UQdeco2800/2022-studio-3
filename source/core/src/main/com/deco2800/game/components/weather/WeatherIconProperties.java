package com.deco2800.game.components.weather;

public enum WeatherIconProperties{
    // Does not affect movement, affect lighting of the environment
    CLOUDY ("images/cloudy.png", "images/weather-filter/cloudy-filter.png", 1.2f),

    // Affecting movement, affect lighting of the environment
    // "images/rainy.gif"
    RAINY ("images/rainy.png","images/weather-filter/rain_1.png", 0.9f),

    // Affect movement a lot, affect terrain and lighting of the environment, must not appear adjacently with sunny
    // "images/snowy.gif"
    SNOWY ("images/snowy.png", "images/weather-filter/ice-frames.png", 0.7f),

    // Does not affect movement, does not affect terrain and lighting of the environment,
    // must not appear adjacently with snowy
    SUNNY ("images/sunny.png", "images/weather-filter/sunny-filter.png", 1.7f),

    // Affecting movement a bit, affect lighting of the environment
    //"images/thunderstorm.gif"
    STORMY ("images/thunderstorm.png", "images/weather-filter/thunderstorm-filter.png", 0.8f);

    /**
     * Location of weather image.
     */
    private final String imageLocation;

    /**
     * Location of filter image.
     */
    private final String filterLocation;

    /**
     * Speed factor for a weather type.
     */
    private final float speedFactor;

    /**
     * Creates a WeatherIconProperties object for a given weather type.
     *
     * @param imageLocation     Location of the image file.
     * @param filterLocation    Location of the filter file.
     * @param speedFactor       Speed factor for weather type.
     */
    WeatherIconProperties(String imageLocation, String filterLocation, float speedFactor) {
        this.imageLocation = imageLocation;
        this.filterLocation = filterLocation;
        this.speedFactor = speedFactor;
    }

    /**
     * Access image location for a weather type.
     */
    public String getImageLocation() {
        return this.imageLocation;
    }

    /**
     * Access filter location for a weather type.
     */
    public String getFilterLocation() {
        return this.filterLocation;
    }

    /**
     * Access speed factor for a weather type.
     */
    public float getSpeedFactor() {
        return this.speedFactor;
    }
}
