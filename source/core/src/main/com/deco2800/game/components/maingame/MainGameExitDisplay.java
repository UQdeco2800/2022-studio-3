package com.deco2800.game.components.maingame;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.components.mainmenu.InsertButtons;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Displays a button to exit the Main Game screen to the Main Menu screen.
 */
public class MainGameExitDisplay extends UIComponent {
  private static final Logger logger = LoggerFactory.getLogger(MainGameExitDisplay.class);

  Sound btn_sound = ServiceLocator.getResourceService().getAsset("sounds/menuclicking.mp3", Sound.class);
  private static final float Z_INDEX = 2f;
  private Table table;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    table = new Table();
    table.top().left();
    table.setFillParent(true);

//    TextButton mainMenuBtn = new TextButton("Exit", skin);
//    TextButton  pauseMenuBtn = new TextButton("Pause", skin);
    // Triggers an event when the button is pressed.
//    mainMenuBtn.addListener(
//      new ChangeListener() {
//        @Override
//        public void changed(ChangeEvent changeEvent, Actor actor) {
//          logger.debug("Exit button clicked");
//          entity.getEvents().trigger("exit");
//        }
//      });

    InsertButtons insertButtons = new InsertButtons();
    ImageButton pauseMenuBtn = insertButtons.draw("images/CogWheel/Esc Menu/SettingsCogWheel_small.png", "images/CogWheel/Esc Menu/SettingsCogWheel-highlighted_small.png");
    pauseMenuBtn.addListener(
      new ChangeListener() {
        @Override
        public void changed(ChangeEvent changeEvent, Actor actor) {
          logger.debug("Pause button clicked");
          entity.getEvents().trigger("togglepm");
          btn_sound.play();
        }
      });


//    table.add(mainMenuBtn).padTop(10f).padRight(10f);
    table.add(pauseMenuBtn).padTop(140f).padRight(30f);
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
