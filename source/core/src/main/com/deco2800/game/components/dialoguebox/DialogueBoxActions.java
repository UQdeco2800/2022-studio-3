package com.deco2800.game.components.dialoguebox;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Dialogue Box and does something when one of the
 * events is triggered
 */
public class DialogueBoxActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxActions.class);
    private DialogueBox dialogueBox;

    public DialogueBoxActions(DialogueBox dialogueBox) {

        this.dialogueBox = dialogueBox;
    }

    @Override
    public void create() {

        entity.getEvents().addListener("dialogueBoxDismiss", this::onDismiss);
    }

    /**
     * hides the dialogueBox when it is currently being shown
     */
    public void onDismiss() {

        logger.info("Hiding dialogue box");
        this.dialogueBox.hide();
    }
}
