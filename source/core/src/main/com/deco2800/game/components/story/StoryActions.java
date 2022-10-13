package com.deco2800.game.components.story;

import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class StoryActions extends Component {
    private static final Logger logger = LoggerFactory.getLogger(StoryActions.class);

    private GdxGame game;


    public StoryActions(GdxGame game) {
        this.game = game;
    }
    @Override
    public void create() {
        //entity.getEvents().addListener("next", this::onNext);
        entity.getEvents().addListener("skip", this::onSkip);
        //entity.getEvents().addListener("previous", this::onPrevious);
    }


   private void onNext(){
       logger.info("Load next");
       game.setScreen(GdxGame.ScreenType.TUTORIAL);
   }



    private void onSkip() {
        logger.info("Skipping to game");
        game.setScreen(GdxGame.ScreenType.TUTORIAL);
    }



}
