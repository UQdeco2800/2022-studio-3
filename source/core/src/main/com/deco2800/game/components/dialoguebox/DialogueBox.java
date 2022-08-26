package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class DialogueBox extends Actor {

    /**
     * Title text to be displayed
     */
    private final Label dialogueTitle;

    /**
     * Dialogue text to be displayed
     */
    private final Label dialogueText;

    /**
     * Background texture to display
     */
    private Texture dialogueTexture;

    private Image dialogueImage;

    /**
     * Dismiss this dialogueBox
     */
    private final TextButton dismissButton;

    /**
     * Set to true if the dialogue box is currently hidde, false on else
     */
    private boolean hidden;

    /**
     * Represents a dialogue box containing a title and text and allowing
     * overflow-scroll functionality and dismissing
     *
     * @param title - dialogue box title
     * @param text  - dialogue box text
     */
    public DialogueBox(String title, String text) {

        Skin dialogueSkin = new Skin(Gdx.files.internal("flat-earth/skin/flat-earth-ui.json"));

        this.dialogueTitle = new Label(title, dialogueSkin);
        this.dialogueText = new Label(text, dialogueSkin);
        /* TODO: set image to be mutable */
        this.dialogueImage = new Image(new Texture(Gdx.files.internal("images/dialogue_box_image_default.png")));
        this.dismissButton = new TextButton("x", dialogueSkin);
        this.dialogueTexture = new Texture(Gdx.files.internal("images/dialogue_box_background.png"));
        this.hidden = false;

        this.setSize(Gdx.graphics.getWidth(), 150f);
        this.setPosition(0f, 0f);

        /* adjust position of actors */
        layout();
    }

    /**
     * Sets the layout attributes of the dialogue box elements
     */
    public void layout() {

        final float screenWidth = Gdx.graphics.getWidth();

        final float titleWidth = 120f;

        final float imageSize = 128f;

        final float dialogueBoxHeight = 150f;
        final float dialogueBoxWidth = screenWidth - 550f;

        final float dismissButtonHeight = this.dismissButton.getHeight();
        final float dismissButtonWidth = this.dismissButton.getWidth();

        final float spacer = 5f;

        /* dialogue box */
        this.setPosition((screenWidth / 2f) - (screenWidth - 550f) / 2f, 2 * spacer);
        this.setSize(screenWidth - 550f, dialogueBoxHeight);

        final float dialogueBoxX = this.getX();
        final float dialogueBoxY = this.getY();

        /* dismissButton */
        this.dismissButton.setPosition(dialogueBoxX + spacer,
                dialogueBoxY + dialogueBoxHeight - dismissButtonHeight - spacer);

        /* text */
        this.dialogueText.setAlignment(Align.left);
        this.dialogueText.setWrap(true);
        this.dialogueText.setSize(dialogueBoxWidth - dismissButtonWidth - imageSize - titleWidth - spacer,
                dialogueBoxHeight);
        this.dialogueText.setPosition(dialogueBoxX + dismissButtonWidth + 2 * spacer,
                dialogueBoxY);

        /* image */
        this.dialogueImage.setSize(imageSize, imageSize);
        this.dialogueImage.setPosition((dialogueBoxX + dialogueBoxWidth) - titleWidth - imageSize,
                this.getY() + 12f);

        /* title */
        this.dialogueTitle.setAlignment(Align.center);
        this.dialogueTitle.setWrap(true);
        this.dialogueTitle.setSize(titleWidth, dialogueBoxHeight);
        this.dialogueTitle.setPosition(dialogueBoxX + dialogueBoxWidth - titleWidth, dialogueBoxY);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(this.dialogueTexture, this.getX(), this.getY(), this.getWidth(), this.getHeight(), 0, 0,
                this.dialogueTexture.getWidth(), this.dialogueTexture.getHeight(), false, false);
        this.dialogueTitle.draw(batch, parentAlpha);
        this.dialogueText.draw(batch, parentAlpha);
        this.dismissButton.draw(batch, parentAlpha);

        if (this.dialogueImage != null)
            this.dialogueImage.draw(batch, parentAlpha);
    }

    /**
     * Returns the current dialogue box title
     *
     * @return - current title
     */
    public String getDialogueTitle() {

        return this.dialogueTitle.getText().toString();
    }

    /**
     * Sets the current dialogue box title
     *
     * @param title - title to be set
     *              <p>
     *              Note: if title is null or empty, the title is cleared
     */
    public void setDialogueTitle(String title) {

        this.dialogueTitle.setText(title);
    }

    /**
     * Returns the current dialogue box text
     *
     * @return - current text
     */
    public String getDialogueText() {

        return this.dialogueText.getText().toString();
    }

    public boolean isHidden() {

        return this.hidden;
    }

    /**
     * Sets the current dialogue box text
     *
     * @param text - text to be set
     *             <p>
     *             Note: if text is null or empty, the text is cleared
     */
    public void setDialogueText(String text) {

        this.dialogueText.setText(text);
    }

    /**
     * Sets the current dialogue box texture
     *
     * @param texture - texture to be set
     *                <p>
     *                Note: if texture is null, no change to the current texture is made
     */
    public void setDialogueTexture(Texture texture) {

        if (texture == null) return;

        this.dialogueTexture = texture;
    }

    /**
     * Sets the current dialogue box image
     *
     * @param image - image to be set
     *              <p>
     *              Note: if image is null, the dialogue box image is not drawn
     */
    public void setDialogueImage(Texture image) {

        this.dialogueImage = (image != null) ? new Image(image) : null;
    }

    /**
     * Hide the dialogue box if it is currently being shown
     */
    public void hide() {

        if (!this.isHidden())
            this.setVisible(false);
    }

    /**
     * Show the dialogue box if it is currently being hidden
     */
    public void show() {

        if (this.isHidden())
            this.setVisible(true);
    }

    public boolean addListener(EventListener listener) {

        if (listener == null)
            return false;

        this.dismissButton.addListener(listener);

        return true;
    }
}
