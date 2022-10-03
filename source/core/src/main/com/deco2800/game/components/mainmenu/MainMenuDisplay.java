package com.deco2800.game.components.mainmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A ui component for displaying the Main menu.
 */
public class MainMenuDisplay extends UIComponent {
    private static final Logger logger = LoggerFactory.getLogger(MainMenuDisplay.class);
    private static final float Z_INDEX = 2f;
    private static final String[] buttonImages = {"images/start-button.PNG", "images/exit-button.PNG"};
    private static final String[] buttonImageHovers = {"images/start-button-hover.PNG", "images/exit-button-hover.PNG"};

    private Table table;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        table = new Table();
        table.setFillParent(true);
        Image titleImage =
                new Image(
                        ServiceLocator.getResourceService()
                                .getAsset("images/title-atlantis.png", Texture.class));

        titleImage.setFillParent(true);
        
        /* Load textures to create image buttons */
        Texture startTexture = new Texture(Gdx.files.internal("images/start-button.PNG"));
        Texture startTextureHover = new Texture(Gdx.files.internal("images/start-button-hover.PNG"));
        Texture exitTexture = new Texture(Gdx.files.internal("images/exit-button.PNG"));
        Texture exitTextureHover = new Texture(Gdx.files.internal("images/exit-button-hover.PNG"));

        ImageButton startBtn = new ImageButton(new TextureRegionDrawable(startTexture));
        ImageButton exitBtn = new ImageButton(new TextureRegionDrawable(exitTexture));
        ImageButton startBtnHover = new ImageButton(new TextureRegionDrawable(startTextureHover));
        ImageButton exitBtnHover = new ImageButton(new TextureRegionDrawable(exitTextureHover));

        // Triggers an event when the button is pressed
        startBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        logger.debug("Start button clicked");
                        entity.getEvents().trigger("start");
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



        table.left();
        table.add(startBtn).left().width(600f);
        table.row();
        table.add(exitBtn).left().width(600f);
        table.pack();


        stage.addActor(titleImage);
        stage.addActor(table);
    }

    @Override
    public void draw(SpriteBatch batch) {
        // draw is handled by the stage
    }

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