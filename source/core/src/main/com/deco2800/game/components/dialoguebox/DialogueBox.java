package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
     * Scroll text if overflow has occurred
     */
    private final TextButton scrollTextButton;

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
     * @param skin  - dialogue box ui style
     */
    public DialogueBox(String title, String text, Skin skin) {

        this.dialogueTitle = new Label(title, skin);
        this.dialogueText = new Label(text, skin);
        this.dialogueImage = new Image(new Texture(Gdx.files.internal("images/dialogue_box_image_default.png")));
        this.scrollTextButton = new TextButton("...", skin);
        this.dismissButton = new TextButton("dismiss", skin);
        this.dialogueTexture = new Texture(Gdx.files.internal("images/dialogue_box_background.png"));
        this.hidden = false;

        this.setSize(Gdx.graphics.getWidth(), 150f);
        this.setPosition(0f, 0f);

        layout();
    }

    /**
     * Sets the layout attributes of the dialogue box elements
     */
    public void layout() {

        float titleHeight = this.dialogueTitle.getHeight();
        float textHeight = this.dialogueText.getHeight();
        float dismissButtonWidth = this.dismissButton.getWidth();

        /* title */
        this.dialogueTitle.setAlignment(Align.topLeft);
        this.dialogueTitle.setPosition(0f, 150f - titleHeight);

        /* text */
        this.dialogueText.setAlignment(Align.topLeft);
        this.dialogueText.setPosition(0f, 150f - titleHeight - textHeight);

        /* image */
        this.dialogueImage.setSize(128f, 128f);
        this.dialogueImage.setPosition(Gdx.graphics.getWidth() - dismissButtonWidth - this.dialogueImage.getWidth() - 10f, 10f);

        /* scrollTextButton */
        this.scrollTextButton.setPosition(0f, 0f);

        /* dismissButton */
        this.dismissButton.setPosition(Gdx.graphics.getWidth() - dismissButtonWidth, 0f);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        batch.draw(this.dialogueTexture, 0f, 0f);
        this.dialogueTitle.draw(batch, parentAlpha);
        this.dialogueText.draw(batch, parentAlpha);
        this.scrollTextButton.draw(batch, parentAlpha);
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
     * @param text - text to be set
     *             <p>
     *             Note: if text is null or empty, the text is cleared
     */
    public void setDialogueText(String text) {

        this.dialogueText.setText(text);
    }

    /**
     * Sets the current dialogue box texture
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
}
