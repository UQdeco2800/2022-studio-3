package com.deco2800.game.components.endgame;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EndGameActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(EndGameActions.class);
    private GdxGame game;
    public EndGameActions(GdxGame game) {
        this.game = game;
    }
    @Override
    public void create() {
        entity.getEvents().addListener("playAgain", this::playAgain);
        entity.getEvents().addListener("exit", this::onExit);
    }

    private void playAgain() {
        logger.info("Skipping to game");
        game.setScreen(GdxGame.ScreenType.LOADING);
    }

    private void onExit() {
        logger.info("Game closed");
        game.exit();
    }
}
