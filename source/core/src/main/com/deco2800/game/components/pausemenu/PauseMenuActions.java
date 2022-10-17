package com.deco2800.game.components.pausemenu;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Timer;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class listens to events relevant to the Pause Menu overlay and does something when one of the
 * events is triggered.
 */
public class PauseMenuActions extends Component {
  private static final Logger logger = LoggerFactory.getLogger(com.deco2800.game.components.mainmenu.MainMenuActions.class);

  Sound btn_sound = ServiceLocator.getResourceService().getAsset("sounds/menuclicking.mp3", Sound.class);
  private GdxGame game;
  private boolean loading = false;

  public PauseMenuActions(GdxGame game) {
    this.game = game;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("resume", this::onResume);
    entity.getEvents().addListener("exit-to-menu", this::onMenu);
    entity.getEvents().addListener("exit", this::onExit);
    entity.getEvents().addListener("settings", this::onSettings);
  }

  /**
   * Swaps to the Pause Game screen.
   */
  private void onResume() {
    logger.info("Start game");
    btn_sound.play();
    game.setScreen(GdxGame.ScreenType.MAIN_GAME);
  }

  /**
   * Going back from a game to main menu
   */
  private void onMenu() {
    logger.info("exit a game to main menu");
    btn_sound.play();
    game.setScreen(GdxGame.ScreenType.MAIN_MENU);
  }

  /**
   * Exits the game.
   */
  private void onExit() {
    logger.info("Exit game");
    btn_sound.play();
    game.exit();
  }

  /**
   * Swaps to the Settings screen.
   */
  private void onSettings() {
    logger.info("Launching settings screen");
    btn_sound.play();
    game.setScreen(GdxGame.ScreenType.SETTINGS);
  }
}
