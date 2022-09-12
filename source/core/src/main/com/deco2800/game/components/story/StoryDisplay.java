package com.deco2800.game.components.story;

/**
import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static com.deco2800.game.GdxGame.ScreenType.MAIN_GAME;
 **/
import com.badlogic.gdx.Gdx;
import com.deco2800.game.GdxGame;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.components.mainmenu.MainMenuDisplay;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

/**
 * A ui component for displaying the Main menu.
 */
public class StoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(StoryDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;
    private Table backTable;
    //private Stack stack;

    private Texture texture;
    private Image storyImage1;
    private Image storyImage2;
    private Image storyImage3;
    private Image storyImage4;
    private Image storyImage5;




    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        backTable = new Table();

        backTable.bottom().right();
        table.setFillParent(true);
        backTable.setFillParent(true);

        Image storyImage1 =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/cutscene-3.png", Texture.class));

        Image storyImage2 =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/cutscene-3.png", Texture.class));
        Image storyImage3 =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/cutscene-3.png", Texture.class));
        Image storyImage4 =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/cutscene-3.png", Texture.class));
        Image storyImage5 =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/cutscene-3.png", Texture.class));

        storyImage1.setFillParent(true);


        TextButton nextBtn = new TextButton("Next", skin);
        TextButton skipBtn = new TextButton("Skip", skin);

        // Triggers an event when the button is pressed
        nextBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Next button clicked");
                        entity.getEvents().trigger("next");
                    }
                });



        skipBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Skip button clicked");
                        entity.getEvents().trigger("skip");
                    }
                });

        backTable.add(nextBtn).pad(25f);
        backTable.add(skipBtn).pad(25f);
        stage.addActor(storyImage1);
        stage.addActor(backTable);
        stage.addActor(table);

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }




    //public void skipScene(){
    //  Gdxgame.setScreen(GdxGame.ScreenType.MAIN_GAME);
    //}


        @Override

    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        super.dispose();
    }
}
