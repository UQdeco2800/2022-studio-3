package com.deco2800.game.components.endgame;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.components.mainmenu.InsertButtons;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

public class EndGameDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(EndGameDisplay.class);
    private Table table;
    private ArrayList<String> storyImages;
    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        storyImages = new ArrayList<>();
        storyImages.add("images/EndGameScreen.png");
        int start = 0;

        Texture storyLine = new Texture(Gdx.files.internal(storyImages.get(start)));
        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
        table.setBackground(storyBackground);
        stage.addActor(table);

//        TextButton playAgainBtn = new TextButton("Play Again", skin);
//        TextButton exitBtn = new TextButton("Quit", skin);

        InsertButtons bothButtons = new InsertButtons();

        String playAgainTexture = "images/playAgain.PNG";
        String playAgainTextureHover = "images/playAgainHover.PNG";
        ImageButton playAgainBtn;
        playAgainBtn = bothButtons.draw(playAgainTexture, playAgainTextureHover);

        String exitTexture = "images/onexit.PNG";
        String exitTextureHover = "images/onexithover.PNG";
        ImageButton exitBtn;
        exitBtn = bothButtons.draw(exitTexture, exitTextureHover);

        playAgainBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Play Again button clicked");
                        entity.getEvents().trigger("playAgain");
                    }
                });

        exitBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Exit button clicked");
                        entity.getEvents().trigger("exit");
                    }
                });

        playAgainBtn.setPosition(400f, 290f);
        exitBtn.setPosition(800f, 290f);
        stage.addActor(table);
        stage.addActor(playAgainBtn);
        stage.addActor(exitBtn);

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

    @Override
    public void dispose() {
        table.clear();
        stage.clear();
        super.dispose();
    }
}
