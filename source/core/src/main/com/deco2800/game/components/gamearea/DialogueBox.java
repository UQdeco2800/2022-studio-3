package com.deco2800.game.components.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

public class DialogueBox extends Actor {

    /**
     * Title text to be displayed
     */
    private Label dialogueTitle;

    /**
     * Dialogue text to be displayed
     */
    private Label dialogueText;

    /**
     * Background texture to display
     */
    private Texture dialogueTexture;

    /**
     * Scroll text if overflow has occurred
     */
    private final TextButton scrollTextButton;

    /**
     * Dismiss this dialogueBox
     */
    private final TextButton dismissButton;

    /**
     * Represents a dialogue box containing a title and text and allowing
     * overflow-scroll functionality and dismissing
     *
     * @param title - dialogue box title
     * @param text - dialogue box text
     * @param skin - dialogue box ui style
     */
    public DialogueBox(String title, String text, Skin skin) {

        this.dialogueTitle = new Label(title, skin);
        this.dialogueText = new Label(text, skin);
        this.scrollTextButton = new TextButton("...", skin);
        this.dismissButton = new TextButton("dismiss", skin);
        this.dialogueTexture = new Texture(Gdx.files.internal("images/dialogue_box_background.png"));

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
    }
}
