package com.deco2800.game.components.tutorial;

import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.TutorialGameArea;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for handling tutorial actions
 * TODO: integrate with actions
 */
public class TutorialActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(TutorialActions.class);
    private int count;

    private GdxGame game;
    private DialogueBoxDisplay display;

    /* Change this to change the dialogues within the tutorial*/
    private static final String[] tutorialDialogues = {
            "Hello! My name is Fred, and I will be guiding you in the \ntutorial",
            "If you know how to play, press skip!",
            "Navigate through the map using the 4 sides \n Zoom in and out using your mouse scroll",
            "Great! Now that we can navigate \n Lets talk about gameplay",
            "The top right hand corner shows your resources,\n the bottom right shows where you are on the map",
            "If you want to skip any character dialogues,\n press exit",
            "The town hall is where the Atlanteans live, and\n must be protected at all costs!",
            "The barracks is where you can train troops \nto defend Atlantis!",
            "Mining camps are used to gather resources to \nupgrade the city",
            "Start protecting Atlantis by clicking the\n town hall and creating the bubble!"

    };


    public TutorialActions(GdxGame game, DialogueBoxDisplay display) {
        this.game = game;
        this.display = display;
        this.count = 0;
    }
    @Override
    public void create() {

        entity.getEvents().addListener("skip", this::onSkip);
        entity.getEvents().addListener("next", this::onNext);
    }

    private void onSkip() {
        logger.info("Skipping to game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

    private void onNext() {
        logger.info("change dialogue");

        if (this.count < tutorialDialogues.length) {
            display.setDialogue(tutorialDialogues[count]);
            // go to next dialogue
            this.count++;
        }




    }


}
