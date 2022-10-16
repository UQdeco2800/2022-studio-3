package com.deco2800.game.components.story;

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

/**
 * A ui component for displaying the Main menu.
 */
public class StoryDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(StoryDisplay.class);
    private static final float Z_INDEX = 2f;
    private Table table;

    private ArrayList<String> storyImages;
    private int start;
    private int end;

    private static final String[] buttonImages = {"images/next_cut.png", "images/prev-cut.png"};




    @Override
    public void create() {
        super.create();
        addActors();
        entity.getEvents().addListener("next", this::nextScene);
        entity.getEvents().addListener("previous", this::prevScene);
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);

        table.bottom().right();

        storyImages = new ArrayList<>();
        storyImages.add("images/cut1.png");
        storyImages.add("images/cut2.png");
        storyImages.add("images/cut3.png");
        storyImages.add("images/cut4.png");
        storyImages.add("images/cut5.png");
        storyImages.add("images/cut6.png");
        start = 0;
        end = 6;

        Texture storyLine = new Texture(Gdx.files.internal(storyImages.get(start)));
        TextureRegionDrawable storyBackground = new TextureRegionDrawable(storyLine);
        table.setBackground(storyBackground);

        start += 1;
        stage.addActor(table);
        InsertButtons bothButtons = new InsertButtons();

        // next button

        String nextTexture = "images/next_cut.png";
        String nextTextureHover = "images/next_cut_hover.PNG";

        ImageButton nextBtn;
        nextBtn = bothButtons.draw(nextTexture, nextTextureHover);



        // prev buttons
        String prevTexture = "images/prev_cut.png";
        String prevTextureHover = "images/prev_cut_hover.png";
        ImageButton prevBtn;
        prevBtn = bothButtons.draw(prevTexture, prevTextureHover);


        //TextButton nextBtn = new TextButton("Next", skin);
        TextButton skipBtn = new TextButton("Skip", skin);
        //TextButton prevBtn = new TextButton("Previous", skin);

        /*
        if (start == 0){
            prevBtn.setVisible(false);
            Gdx.gl.glClearColor(0, 0, 0, 0);
            //Gdx.gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        } else{
            prevBtn.setVisible(true);

        }
*/
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

        prevBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        logger.debug("Previous button clicked");
                        entity.getEvents().trigger("previous");
                    }
                });



        table.add(skipBtn).expand().top().right().pad(25f).width(100);
        table.row();
        table.add(prevBtn).pad(25f).left();
        table.add(nextBtn).pad(25f).right();


        stage.addActor(table);

    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }




    private void nextScene() {
        if (start < end) {
            Drawable next = new TextureRegionDrawable(new Texture(Gdx.files.internal(storyImages.get(start))));
            table.setBackground(next);
            start += 1;
        } else {
            entity.getEvents().trigger("skip");
        }}

    private void prevScene(){
        if (end - start > 0){
            Drawable prev = new TextureRegionDrawable(new Texture(Gdx.files.internal(storyImages.get(start-1))));
            table.setBackground(prev);
            start -=1;
        }
    }



    @Override
    public float getZIndex() {
        return Z_INDEX;
    }

    @Override
    public void dispose() {
        table.clear();
        stage.clear();
        super.dispose();
    }
}
