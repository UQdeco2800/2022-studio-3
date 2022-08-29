package com.deco2800.game.input;

import com.deco2800.game.components.friendlyunits.MouseInputComponent;
import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.ui.terminal.TouchTerminalInputComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TouchInputFactory extends InputFactory{
  private static final Logger logger = LoggerFactory.getLogger(TouchInputFactory.class);

  /**
   * Creates an input handler for the player
   *
   * @return Player input handler
   */
  @Override
  public InputComponent createForPlayer() {
    logger.debug("Creating player input handler");
    return new TouchPlayerInputComponent();
  }

  @Override
  public InputComponent createForFriendlyUnit() {
    logger.debug("Creating player input handler");
    return new MouseInputComponent();
  }

  /**
   * Creates an input handler for the terminal
   *
   * @return Terminal input handler
   */
  @Override
  public InputComponent createForTerminal() {
    logger.debug("Creating terminal input handler");
    return new TouchTerminalInputComponent();
  }
  
  /**
   * Creates an input handler for the camera
   *
   * @return Camera input handler
   */
  @Override
  public InputComponent createForCamera() {
    logger.debug("Creating camera input handler");
    return new CameraInputComponent();
  }
}
