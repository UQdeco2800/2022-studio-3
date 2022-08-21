package com.deco2800.game.input;

import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.ui.terminal.TouchTerminalInputComponent;
import com.deco2800.game.worker.KeyboardWorkerInputComponent;
import com.deco2800.game.worker.TouchWorkerInputComponent;
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

  /**
   * Creates an input handler for the worker
   *
   * @return Worker input handler
   */
  @Override
  public InputComponent createForWorker() {
    logger.debug("Creating worker input handler");
    return new TouchWorkerInputComponent();
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
}
