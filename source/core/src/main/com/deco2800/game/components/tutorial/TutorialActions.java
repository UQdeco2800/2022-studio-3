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
            "Hello! My name is Fred, and I\nwill be guiding you in the tutorial",
            "If you know how to play, press skip!",
            "Navigate through the map using the 4 sides\nZoom in and out using your mouse scroll",
            "Great! Now that we can navigate\nLets talk about gameplay",
            "The top right corner shows your resources,\nthe bottom right shows where you are on the map",
            "If you want to skip any character dialogues,\npress exit",
            "The town hall is where the Atlanteans live, and\nmust be protected at all costs!",
            "The barracks is where you can train troops\nto defend Atlantis!",
            "Mining camps are used to gather resources to\nupgrade the city",
            "Oh no! Poseidon's army is invading!\nWe are being attacked by a titan!",
            "Looks like Amphitrite lured them here\nWe have to defend our home!",
            "Dont worry\nI'll teach you how to do that!",
            "Release the spell over\nthe titan to defeat it!",
            "Build am Army and send the foes\nback to where they came from!",
            "I wish you the best of luck\nNow go save Atlantis!"
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
        game.setScreen(GdxGame.ScreenType.LOADING);
    }

    private void onNext() {
        logger.info("change dialogue");
        this.count++;

        if (this.count < tutorialDialogues.length) {
            display.setDialogue(tutorialDialogues[count]);
            switch (this.count) {
                case 2 -> display.setImage("images/Teach_Fred.png");
                case 3 -> display.setImage("images/Teach_Fred.png");
                case 4 -> display.setImage("images/Teach_Fred.png");
                case 9 -> display.setImage("images/Scared_Fred.png");
                case 10 -> display.setImage("images/Fredmogus.png");
                case 11 -> display.setImage("images/Teach_Fred.png");
                case 12 -> display.setImage("images/Attack_Fred.png");
                case 13 -> display.setImage("images/Attack_Fred.png");
                case 14 -> display.setImage("images/Good_Luck_Fred.png");
                default -> display.setImage("images/SirFred.png");
            }
        }

        if (this.count >= tutorialDialogues.length) {
            game.setScreen(GdxGame.ScreenType.LOADING);
        }
    }
}
