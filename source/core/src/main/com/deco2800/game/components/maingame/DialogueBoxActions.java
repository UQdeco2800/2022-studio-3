package com.deco2800.game.components.maingame;

import com.deco2800.game.components.Component;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents all actions bound to a dialogue box
 */
public class DialogueBoxActions extends Component {

    /** logger for debugging purposes */
    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxActions.class);

    /** reference to current dialogue box being displayed */
    private final DialogueBoxDisplay dialogueBox;

    public DialogueBoxActions(UIComponent dialogueBox) {

        logger.debug("Creating DialogueBoxActions");
        this.dialogueBox = (DialogueBoxDisplay) dialogueBox;
    }

    @Override
    public void create() {

        entity.getEvents().addListener("dialogueDismiss", this::dismiss);
        entity.getEvents().addListener("dialogueAlert", this::alert);
    }

    /**
     * Hide the current dialogue box, if not already
     */
    private void dismiss() {

        logger.debug("Hiding DialogueBox");
        dialogueBox.hide();
    }

    /**
     * Show the current dialogue box, if not already
     */
    private void alert() {

        logger.debug("Showing DialogueBox");
        dialogueBox.show();
    }
}
