package com.deco2800.game.components.pausemenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.GdxGame;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.services.GameTime;
import com.sun.jdi.connect.spi.TransportService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.event.KeyEvent;

/**
 * A ui component for displaying the Pause menu.
 */
public class PauseMenuDisplay extends UIComponent {
  private static final float MAX_PAUSE_MENU_WIDTH = 500;
  private static final float MAX_PAUSE_MENU_HEIGHT = 600;
  private static final float PAUSE_MENU_WIDTH_TO_SCREEN_RATIO = 2.7f / 3f;
  private static final float PAUSE_MENU_HEIGHT_TO_SCREEN_RATIO = 2.7f / 3f;
  private static final Logger logger = LoggerFactory.getLogger(PauseMenuDisplay.class);
  private static final float Z_INDEX = 2f;
  private static final String BACKGROUND_FILE_PATH = "images/bigblack.png";
  private Table mainTable;
  private Table pauseWindow;
  private Image background;
  private final GdxGame game;
  private boolean pmEnabled = false;
  public PauseMenuDisplay(GdxGame game) { this.game = game; }

  @Override
  public void create() {
    super.create();
    logger.debug("Creating Pause Menu Screen");
    addActors();
    logger.debug("Finished creating Pause Menu Screen");
  }

  public void togglePauseScreen() {
      GameTime timeSource = ServiceLocator.getTimeSource();

      if (!pmEnabled) {
          timeSource.paused();
          pauseWindow.setVisible(true);
          background.setVisible(true);
          pmEnabled = true;
      } else {
          timeSource.unpaused();
          pauseWindow.setVisible(false);
          background.setVisible(false);
          pmEnabled = false;
      }
    }

  private void addActors() {
    pauseWindow = new Table();
    background = new Image(ServiceLocator.getResourceService().getAsset(BACKGROUND_FILE_PATH, Texture.class));
    createMainTable();
    pauseWindow.add(mainTable);


   pauseWindow.padTop(0);
   pauseWindow.padBottom(0);
   pauseWindow.setVisible(false);
   background.setVisible(false);
   setPauseMenuSize();

   stage.addActor(background);
   stage.addActor(pauseWindow);

   entity.getEvents().addListener("togglepm", this::togglePauseScreen);
  }

  private void createMainTable() {
    TextButton resumeBtn = new TextButton("Resume", skin);
    TextButton settingsBtn = new TextButton("Settings", skin);
    TextButton menuBtn = new TextButton("Exit to Menu", skin);
    TextButton exitBtn = new TextButton("Exit Game", skin);

    // Triggers an event when the button is pressed
    resumeBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Resume button clicked");
                    entity.getEvents().trigger("resume");
                }
            });

    menuBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("exit to menu button clicked");
                    entity.getEvents().trigger("exit-to-menu");
                }
            });

    settingsBtn.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeEvent changeEvent, Actor actor) {
                    logger.debug("Settings button clicked");
                    entity.getEvents().trigger("settings");
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

    mainTable = new Table();
    mainTable.row();
    mainTable.add(resumeBtn);
    mainTable.row();
    mainTable.add(menuBtn).padTop(15f);
    mainTable.row();
    mainTable.add(settingsBtn).padTop(15f);
    mainTable.row();
    mainTable.add(exitBtn).padTop(15f);
  }
    private void setPauseMenuSize() {
        float stageWidth = stage.getWidth();
        float stageHeight = stage.getHeight();
        float pauseMenuWidth = stageWidth * PAUSE_MENU_WIDTH_TO_SCREEN_RATIO;
        float pauseMenuHeight = stageHeight * PAUSE_MENU_HEIGHT_TO_SCREEN_RATIO;

        // Checks if pause menu should be a max width and height
        if (pauseMenuWidth > MAX_PAUSE_MENU_WIDTH) {
            pauseMenuWidth = MAX_PAUSE_MENU_WIDTH;
        }
        if (pauseMenuHeight > MAX_PAUSE_MENU_HEIGHT) {
            pauseMenuHeight = MAX_PAUSE_MENU_HEIGHT;
        }

        // Set and position pause window screen
        pauseWindow.setSize(pauseMenuWidth, pauseMenuHeight);
        background.setSize(pauseMenuWidth, pauseMenuHeight);
        pauseWindow.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
                stage.getHeight() / 2 - pauseWindow.getHeight() / 2);
        background.setPosition(stage.getWidth() / 2 - pauseWindow.getWidth() / 2,
                stage.getHeight() / 2 - pauseWindow.getHeight() / 2);

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
    mainTable.clear();
    mainTable.remove();
    super.dispose();
  }
}