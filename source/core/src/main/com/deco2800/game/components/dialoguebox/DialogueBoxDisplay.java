package com.deco2800.game.components.dialoguebox;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the game dialogue box
 */
public class DialogueBoxDisplay extends UIComponent {

    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxDisplay.class);
    private DialogueBox dialogueBox;

    @Override
    public void create() {

        super.create();
        addActors();
    }

    private void addActors() {

        this.dialogueBox = new DialogueBox("Sample Title", "Sample Text");

        this.dialogueBox.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        logger.debug("Dismiss button clicked");
                        entity.getEvents().trigger("dialogueBoxDismiss");
                    }
                });

        stage.addActor(this.dialogueBox);
    }

    @Override
    public void draw(SpriteBatch batch) {
        /* handled by stage */
    }
}
