package com.deco2800.game.components.friendlyunits.gamearea;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

/**
 * Displays performance stats about the game for debugging purposes.
 */
public class PerformanceDisplay extends UIComponent {
  private static final float Z_INDEX = 5f;
  private Label profileLabel;

  @Override
  public void create() {
    super.create();
    addActors();
  }

  private void addActors() {
    profileLabel = new Label(getStats(), skin, "small");
    stage.addActor(profileLabel);
  }

  @Override
  public void draw(SpriteBatch batch) {
    if (ServiceLocator.getRenderService().getDebug().getActive()) {
      profileLabel.setVisible(true);
      profileLabel.setText(getStats());

      int screenHeight = stage.getViewport().getScreenHeight();
      float offsetX = 5f;
      float offsetY = 350f;
      profileLabel.setPosition(offsetX, screenHeight - offsetY);
    } else {
      profileLabel.setVisible(false);
    }
  }

  private String getStats() {
    String message = "Debug\n";
    message =
        message
            .concat(String.format("FPS: %d fps%n", Gdx.graphics.getFramesPerSecond()))
            .concat(String.format("RAM: %d MB%n", Gdx.app.getJavaHeap() / 1000000))
            .concat(String.format("Width: %d px%n", Gdx.graphics.getWidth()))
            .concat(String.format("Height: %d px%n", Gdx.graphics.getHeight()))
            .concat(String.format("Mouse X: %d%n", Gdx.input.getX()))
            .concat(String.format("Mouse Y: %d%n", Gdx.input.getY()));
    return message;
  }

  @Override
  public float getZIndex() {
    return Z_INDEX;
  }

  @Override
  public void dispose() {
    super.dispose();
    profileLabel.remove();
  }
}
