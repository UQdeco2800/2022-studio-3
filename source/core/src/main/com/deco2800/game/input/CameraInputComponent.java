package com.deco2800.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.CameraComponent;
import com.badlogic.gdx.graphics.OrthographicCamera;
import java.awt.color.CMMException;

public class CameraInputComponent extends InputComponent {
    /**
     * Not used (set to 0) currently, designed to stop scrolling in resizable mode
     */
    private final float buffer2 = 0;
    /**
     * Distance in pixels between the edge of the screen and a camera movement trigger
     */
    private final float buffer = 100;
    /**
     * Default scrolling speed of camera
     */
    private final float defaultSpeed = 0.1f;
    /**
     * Scrolling speed of camera diagonally
     */
    private final float fastSpeed = 0.12f;

    /**
     * Current speed being scrolled at
     */
    private float currentSpeed = defaultSpeed;
    /**
     * Direction to scroll in horizontally (-1, 0, 1)
     */
    private float horizontalChange = 0;
    /**
     * Direction to scroll in vertically (-1, 0, 1)
     */
    private float verticalChange = 0;

    public CameraInputComponent() {
        super(5);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //Mouse moved, check if it is at a screen extremity
        updateDirection();
        return true;
    }

    /**
     * Moves the camera relative to the change and current speed
     */
    @Override
    public void update() {
        float cameraX = super.entity.getPosition().x;
        float cameraY = super.entity.getPosition().y;

        super.entity.setPosition(cameraX + horizontalChange * currentSpeed,
                cameraY + verticalChange * currentSpeed);
    }

    /**
     * Checks to see if the cursor is hovered to a screen edge, and if so updates the direction it
     * must move in
     */
    public void updateDirection() {
        int currentX = Gdx.input.getX();
        int currentY = Gdx.input.getY();
        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        //If the mouse is at either of the horizontal extremities
        if (currentX >= screenWidth - buffer && !(currentX > screenWidth - buffer2)) {
            horizontalChange = 1;
        } else if (currentX <= buffer && !(currentX < buffer2)) {
            horizontalChange = -1;
        } else {
            horizontalChange = 0;
        }

        //If the mouse is at either of the vertical extremities
        if (currentY >= screenHeight - buffer && !(currentY > screenHeight - buffer2)) {
            verticalChange = -1;
        } else if (currentY <= buffer && !(currentY < buffer2)) {
            verticalChange = 1;
        } else {
            verticalChange = 0;
        }

        //If the camera is to move diagonally, increase the speed
        if (verticalChange != 0 && horizontalChange != 0) {
            currentSpeed = fastSpeed;
        } else {
            currentSpeed = defaultSpeed;
        }
    }
    
    /**
     * Adjusts the camera zoom amount according to scroll speed.
     *
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
      OrthographicCamera camera = (OrthographicCamera) super.entity.getComponent(CameraComponent.class).getCamera();
      float newZoom = camera.zoom + -1 * amountY;
      if (newZoom > 0) {
    	  camera.zoom = newZoom;
    	  camera.update();
      }
      
      return true;
    }

}
