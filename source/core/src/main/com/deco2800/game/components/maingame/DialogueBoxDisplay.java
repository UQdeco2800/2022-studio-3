package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.areas.terrain.MinimapComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents a Dialogue Box to be displayed randomly
 */
public class DialogueBoxDisplay extends UIComponent {

    /** logger for debugging purposes */
    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxDisplay.class);

    /** current dialogue text to be displayed */
    private Label dialogue;

    /** current dialogue title to be displayed */
    private Label title;

    /** current background texture to be displayed */
    private Image backgroundTexture;

    /** current image to be displayed */
    private Image image;

    /** current dismiss button to be displayed */
    private TextButton dismissBtn;

    /** stores dialogue box's current state of visibility */
    private boolean hidden;

    /* external values */

    /** current width of infoBox being displayed */
    private float infoWidth;

    /** reference to current minimap being displayed */
    private MinimapComponent minimap;

    public DialogueBoxDisplay(float infoWidth) {

        logger.debug("Creating DialogueBoxDisplay");
        this.infoWidth = infoWidth;
    }

    /**
     * Set current minimap for reference
     * @param minimap - current minimap
     */
    public void setMinimap(MinimapComponent minimap) {

        this.minimap = minimap;
    }

    @Override
    public void create() {

        super.create();
        addActors();
    }

    /**
     * Add all necessary dialogue box components
     */
    public void addActors() {

        logger.debug("Creating DialogueBox Actors");
        this.backgroundTexture = new Image(
                ServiceLocator
                        .getResourceService()
                        .getAsset("images/dialogue_box_pattern2_background.png", Texture.class)
        );

        this.title = new Label("EXAMPLE TITLE", skin);

        this.dialogue = new Label("EXAMPLE DIALOGUE TEXT", skin);

        this.dismissBtn = new TextButton("", new Skin(Gdx.files.internal("atlantis/exitButtonSkin.json")));
        this.dismissBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entity.getEvents().trigger("dialogueDismiss");
            }
        });

        this.image = new Image(
                ServiceLocator
                        .getResourceService()
                        .getAsset("images/dialogue_box_image_default.png", Texture.class)
        );

        logger.debug("Adding DialogueBox Actors to stage");
        stage.addActor(this.backgroundTexture);
        stage.addActor(this.dialogue);
        stage.addActor(this.title);
        stage.addActor(this.image);
        stage.addActor(this.dismissBtn);

        this.show();
    }

    /**
     * Set current dialogue text to be displayed
     *
     * @param dialogue - dialogue text to be displayed
     */
    // TODO: update render to display new text
    public void setDialogue(String dialogue) {

        this.dialogue = new Label(dialogue, skin);
    }

    /**
     * Set current title to be displayed
     *
     * @param title - title to be displayed
     */
    // TODO: update render to display new text
    public void setTitle(String title) {

        this.title = new Label(title, skin);
    }

    /**
     * Set current image to be displayed
     *
     * @param imagePath - image to be displayed
     */
    public void setImage(String imagePath) {

        this.image = new Image(new Texture(Gdx.files.internal(imagePath)));
    }

    /**
     * Query if the dialogue box is currently invisible
     *
     * @return - true if invisible, false if else
     */
    public boolean isHidden() {

        return this.hidden;
    }

    /**
     * Make invisible if not already
     */
    public void hide() {

        if (this.isHidden())
            return;

        this.hidden = true;
    }

    /**
     * Make visible if not already
     */
    public void show() {

        if (!this.isHidden())
            return;

        this.hidden = true;
    }

    /**
     * Query current sizing of window and UI items and size/position appropriately
     *
     * @param batch Batch to render to.
     */
    @Override
    public void draw(SpriteBatch batch) {

        logger.debug("Re-Drawing DialogueBox");
        /* FIXME: take map width from minimapComponent so that dialogueBox doesnt cover water edge on resize */
        float screenWidth = Gdx.graphics.getWidth();

        /* FIXME: temp value */
        float mapWidth = 200f;

        /* calculate sizes and positions */
        float[] backgroundSize = {Math.min(((screenWidth - mapWidth) - infoWidth) - 20f, 750f), 143f * 1.5f};
        float availableSpace = screenWidth - (infoWidth + mapWidth);
        float[] backgroundPosition = {infoWidth + ((availableSpace - backgroundSize[0]) / 2f), 0f};

        float[] titlePosition = {backgroundPosition[0] + 25f, backgroundSize[1] - 50f};

        float[] imageSize = {128f, 128f};
        float[] imagePosition = {backgroundSize[0] + backgroundPosition[0] - this.image.getWidth() - 30f,
                (backgroundSize[1] - this.image.getHeight()) / 2f};

        float[] dialogueSize = {backgroundSize[0] - 60f - imageSize[0], backgroundSize[1] - 50f};
        float[] dialoguePosition = {backgroundPosition[0] + 25f, 0f};

        float[] dismissButtonSize = {45f, 25f};
        float[] dismissButtonPosition = {backgroundSize[0] + backgroundPosition[0] - this.dismissBtn.getWidth(),
                backgroundSize[1] - this.dismissBtn.getHeight()};

        /* assign sizes and positions */
        this.backgroundTexture.setSize(backgroundSize[0], backgroundSize[1]);
        this.backgroundTexture.setPosition(backgroundPosition[0], backgroundPosition[1]);

        this.dismissBtn.setSize(dismissButtonSize[0], dismissButtonSize[1]);
        this.dismissBtn.setPosition(dismissButtonPosition[0], dismissButtonPosition[1]);

        this.title.setPosition(titlePosition[0], titlePosition[1]);

        this.dialogue.setSize(dialogueSize[0], dialogueSize[1]);
        this.dialogue.setPosition(dialoguePosition[0], dialoguePosition[1]);

        this.image.setSize(imageSize[0], imageSize[1]);
        this.image.setPosition(imagePosition[0], imagePosition[1]);

        /* TODO: scale appropriately */
        if (backgroundSize[0] <= 450f)
            this.hide();

        this.backgroundTexture.setVisible(!this.isHidden());
        this.dialogue.setVisible(!this.isHidden());
        this.title.setVisible(!this.isHidden());
        this.image.setVisible(!this.isHidden());
        this.dismissBtn.setVisible(!this.isHidden());
    }

    @Override
    public void dispose() {

        logger.debug("Destroying DialogueBox");
        super.dispose();
        this.title.remove();
        this.dialogue.remove();
        this.backgroundTexture.remove();
        this.image.remove();
    }
}
