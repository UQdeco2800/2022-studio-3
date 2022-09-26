package com.deco2800.game.components.tutorial;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for handling tutorial actions
 */
public class TutorialActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(TutorialActions.class);

    private GdxGame game;


    public TutorialActions(GdxGame game) {
        this.game = game;
    }
    @Override
    public void create() {

        entity.getEvents().addListener("skip", this::onSkip);
    }

    private void onSkip() {
        logger.info("Skipping to game");
        game.setScreen(GdxGame.ScreenType.MAIN_GAME);
    }

}
