package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.components.weather.WeatherIconProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.deco2800.game.utils.random.PseudoRandom;
import com.badlogic.gdx.utils.Align;


public class WeatherIcon extends Actor {
    /**
     * To create logging information.
     */
    private static final Logger logger = LoggerFactory.getLogger(WeatherIconDisplay.class);

    /**
     * To display the Timer
     */
    private Label timerLabel;

    /**
     * Initiate the Image for the weather image.
     */
    private Image weatherImage;

    /**
     * Stores the value of the current index.
     */
    private int currentIndex;

    /**
     * Initiate the Image for the weather filter.
     */
    private Image weatherFilter;

    /**
     * Initiate the speed factor
     */
    private float speedFactor;

    /**
     * Holds information for the current weather type.
     */
    private WeatherIconProperties weatherIconProperties;

    private TextureRegion iceFrames[];

    private Animation<TextureRegion> iceAnimation;

    private TextureRegion rainFrames[];

    private Animation<TextureRegion> rainAnimation;

    private SpriteBatch batch;

    private float elapsedTime;

    /**
     * Stores possible weather types.
     */
    private final WeatherIconProperties[] weatherTypes = {
            WeatherIconProperties.CLOUDY,
            WeatherIconProperties.RAINY,
            WeatherIconProperties.SNOWY,
            WeatherIconProperties.SUNNY,
            WeatherIconProperties.STORMY
    };

    /**
     * Instantiates WeatherIcon class. Upon creation, a random index is selected
     * to pick the initial weather and corresponding effects in the game.
     *
     * @param countdownTimer A Label representing a countdown timer to be
     *                       displayed in the game.
     */
    public WeatherIcon(Label countdownTimer) {
        // Initiate timerLabel with countdownTimer
        this.timerLabel = countdownTimer;

        // Randomly select first index
        this.currentIndex = PseudoRandom.seedRandomInt(0, weatherTypes.length);

        // Stores weather properties
        this.weatherIconProperties = this.weatherTypes[currentIndex];

        // Initiate weatherImage
        this.weatherImage = new Image(new Texture(this.weatherIconProperties.getImageLocation()));

        // Initiate weatherFilter
        this.weatherFilter = new Image(new Texture(this.weatherIconProperties.getFilterLocation()));

        // Initiate speedFactor
        this.speedFactor = this.weatherIconProperties.getSpeedFactor();

        Texture img = new Texture("images/weather-filter/ice-frames.png");
        Texture rainEffects = new Texture("images/Rain.png");
        TextureRegion[][] tmpFrames = TextureRegion.split(img,200,200);
        TextureRegion[][] rainImages = TextureRegion.split(rainEffects, 1000, 1000);
        this.iceFrames = new TextureRegion[3];
        this.iceFrames[0] = new TextureRegion(new Texture("images/weather-filter/snowfall_1.png"));
        this.iceFrames[1] = new TextureRegion(new Texture("images/weather-filter/snowfall_2.png"));
        this.iceFrames[2] = new TextureRegion(new Texture("images/weather-filter/snowfall_3.png"));
        this.rainFrames = new TextureRegion[4];
        int frame=0;
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++) {
                rainFrames[frame++] = rainImages[j][i];

            }
        }
        iceAnimation = new Animation(0.5f, iceFrames);
        rainAnimation = new Animation(0.5f, rainFrames);
        layout();
    }

    /**
     * Getter method to access the current index of the Weather Icon.
     *
     * @return Int representing index of the weather icon.
     */
    public int getCurrentIndex() {
        return this.currentIndex;
    }

    /**
     * This method is called when the timer for the weather expires. It
     * pseudo-randomly seeds a new index that is not the same as the
     * previous index, and changes the relevant properties based on the
     * new selected index.
     */
    public void changeWeatherImage() {
        // Obtain a new index
        int index;
        do {
            index = PseudoRandom.seedRandomInt(0, weatherTypes.length);
        } while (index == this.currentIndex);
        this.currentIndex = index;
        this.weatherIconProperties = this.weatherTypes[currentIndex];

        // Update display from new index
        this.weatherImage = new Image(new Texture(this.weatherIconProperties.getImageLocation()));
        this.weatherFilter = new Image(new Texture(this.weatherIconProperties.getFilterLocation()));
        this.speedFactor = this.weatherIconProperties.getSpeedFactor();
        layout();
    }

    /**
     * Getter method to obtain the movement speed factor.
     *
     * @return Float value representing movement speed.
     */
    public float getMovementSpeed() {
        return this.speedFactor;
    }

    /**
     * Sets the layout of the weather, displaying the weather icon and the
     * filter for a given index.
     */
    public void layout() {
        //  Layout for weatherFilter
        weatherFilter.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        weatherFilter.setPosition(0,0);

        //  Layout for weatherImage
        weatherImage.setSize(75f, 75f);
        weatherImage.setPosition(Gdx.graphics.getWidth()/2f-weatherImage.getWidth()/2f, Gdx.graphics.getHeight()-85f);

        //  Layout for timer
        this.timerLabel.setAlignment(Align.left);
        this.timerLabel.setWrap(false);
        this.timerLabel.setSize(3f,3f);
        this.timerLabel.setPosition(Gdx.graphics.getWidth()/2f + weatherImage.getWidth()/2f + 15f, Gdx.graphics.getHeight()-50f);
    }

    /**
     * Draws the weather layout to the game screen.
     *
     * @param batch
     * @param parentAlpha
     */
    @Override
    public void draw(Batch batch, float parentAlpha) {
        elapsedTime+= Gdx.graphics.getDeltaTime();
        if (weatherIconProperties == WeatherIconProperties.SNOWY) {
            Image temp = new Image(iceAnimation.getKeyFrame(elapsedTime, true));
            for (int i=0 ; i < 3; i++){
                for (int j=0; j < 2; j++) {
                    temp.setScale(0.3f, 0.3f);
                    temp.setPosition(i*500+50, j*500);
                    temp.draw(batch, parentAlpha);
                }
            }

            this.weatherFilter = temp;
        }
        if (weatherIconProperties == WeatherIconProperties.RAINY || weatherIconProperties == WeatherIconProperties.STORMY){
            Image temp = new Image(rainAnimation.getKeyFrame(elapsedTime, true));
            temp.setScale(0.5f, 0.2f);
            for (int i=0 ; i < 5; i++){
                for (int j=0; j < 7; j++) {
                    temp.setPosition(300*i, j*200);
                    temp.draw(batch, parentAlpha);
                }
            }
            if (weatherIconProperties == WeatherIconProperties.RAINY){
                this.weatherFilter = temp;
                this.weatherFilter.setPosition(700,0);
            }

        }


        this.weatherFilter.draw(batch, parentAlpha);
        this.weatherImage.draw(batch, parentAlpha);
        this.timerLabel.draw(batch, parentAlpha);
    }

    /**
     * Getter method to obtain the timer label.
     *
     * @return A timer label representing the timer label of the object.
     */
    public Label getTimerLabel() {
        return this.timerLabel;
    }

    /**
     * Getter method to obtain WeatherIconProperties.
     * @return WeatherIconProperties
     */
    public WeatherIconProperties getWeatherIconProperties() {
        return this.weatherIconProperties;
    }

    /**
     * Setter method to set the timer label.
     *
     * @param countdownTimer A new Label to set the timer label to.
     */
    public void setTimerLabel(Label countdownTimer) {
        this.timerLabel = countdownTimer;
    }

}