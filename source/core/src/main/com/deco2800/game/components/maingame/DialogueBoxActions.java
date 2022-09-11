package com.deco2800.game.components.maingame;

import com.deco2800.game.components.Component;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DialogueBoxActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(DialogueBoxActions.class);

    DialogueBoxDisplay dialogueBox;

    public DialogueBoxActions(UIComponent dialogueBox) {

        this.dialogueBox = (DialogueBoxDisplay) dialogueBox;
    }

    @Override
    public void create() {

        entity.getEvents().addListener("dialogueDismiss", this::dismiss);
        entity.getEvents().addListener("dialogueAlert", this::alert);
    }

    private void dismiss() {

        dialogueBox.hide();
    }

    private void alert() {

        dialogueBox.show();
    }
}
