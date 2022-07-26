package com.deco2800.game.components.weather;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
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

    /**
     * Holds the frames for the snow and ice animation
     */
    private TextureRegion[] iceFrames;
    private TextureRegion[] rainFrames;

    /**
     * Creates the animation object to track elapsed time and set the frame
     */
    private Animation<TextureRegion> iceAnimation;
    private Animation<TextureRegion> rainAnimation;


    /**
     * Holds the float value for the elapsed time from weather transition.
     * It is used with the animation objects to change the current frame
     */
    private float elapsedTime;

    /**
     * Holds the sounds for all the weather events
     */
    private Sound rainSound;
    private Sound thunderSound;
    private Sound sunnySound;
    private Sound windSound;

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


        Texture img = new Texture("images/snow.png");

        //Sets the frames in the texture array
        TextureRegion[][] snowImages = TextureRegion.split(img, 1400, 1600);
        this.iceFrames = new TextureRegion[4];
        this.rainFrames = new TextureRegion[3];
        this.rainFrames[0] = new TextureRegion(new Texture("images/weather-filter/rain_1.png"));
        this.rainFrames[1] = new TextureRegion(new Texture("images/weather-filter/rain_2.png"));
        this.rainFrames[2] = new TextureRegion(new Texture("images/weather-filter/rain_3.png"));
        int frame=0;
        for (int i=0; i<2; i++){
            for (int j=0; j<2; j++) {

                iceFrames[frame++] = snowImages[j][i];

            }
        }

        //Creating animations with different frame rates
        iceAnimation = new Animation(0.3f, iceFrames);
        rainAnimation = new Animation(0.1f, rainFrames);

        //Creates all the sound objects used for the weather
        rainSound = Gdx.audio.newSound(Gdx.files.internal("sounds/rainsound.mp3"));
        thunderSound = Gdx.audio.newSound(Gdx.files.internal("sounds/thundersound.mp3"));
        sunnySound = Gdx.audio.newSound(Gdx.files.internal("sounds/sunny.mp3"));
        windSound = Gdx.audio.newSound(Gdx.files.internal("sounds/winds.mp3"));
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

        //Stops all current sounds playing when weather changes
        rainSound.stop();
        thunderSound.stop();
        sunnySound.stop();
        windSound.stop();

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
        logger.info("Weather changing");

        //Plays sounds on right weather events
        if (weatherIconProperties == WeatherIconProperties.RAINY || weatherIconProperties == WeatherIconProperties.STORMY){
            rainSound.play();
        }
        if (weatherIconProperties == WeatherIconProperties.STORMY){
            thunderSound.loop();
            windSound.loop();
        }
        if (weatherIconProperties == WeatherIconProperties.SUNNY){
            sunnySound.play();
        }
        if (weatherIconProperties == WeatherIconProperties.CLOUDY || weatherIconProperties == WeatherIconProperties.SNOWY){
            windSound.loop();
        }

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
        //Gets current time from the Gdx library
        elapsedTime+= Gdx.graphics.getDeltaTime();

        //Runs the animation if the current weather event is snowy
        if (weatherIconProperties == WeatherIconProperties.SNOWY) {
            Image temp = new Image(iceAnimation.getKeyFrame(elapsedTime, true));
            //Spawns multiple instances of the current animation frame at different locations
            //This is to reduce the size of rain drop and fill the screen better
            for (int i=0 ; i < 4; i++){
                for (int j=0; j < 3; j++) {
                    temp.setScale(0.3f, 0.3f);
                    temp.setPosition(i*350+200, j*400-200);
                    temp.draw(batch, parentAlpha);
                }
            }

            this.weatherFilter = temp;
        }

        //This starts the rain animation which is used by both the rain and storm weather events
        if (weatherIconProperties == WeatherIconProperties.RAINY || weatherIconProperties == WeatherIconProperties.STORMY){
            Image temp = new Image(rainAnimation.getKeyFrame(elapsedTime, true));
            temp.setScale(0.5f, 0.5f);
            temp.setPosition(0,200);
            //Spawns multiple instances of the current animation frame at different locations
            //This is to reduce the size of rain drop and fill the screen better
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