package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

import static java.lang.Math.abs;

public class MouseInputComponent extends InputComponent {

    //used for touchDown
    int touchDownX;
    //used for touchDown
    int touchDownY;

    boolean leftPressed;

    /**
     * Controls the input of the mouse, whether it be clicked or clicked and dragged so that when the player does so,
     * a trigger is sent to the Selectable component with the location and type of click movement
     */
    public MouseInputComponent() {
        super(5);
        leftPressed = false;
    }

    /**
     * Stores value in this class as the starting point of left mouse button being clicked
     *
     * @param screenX The x coordinate, origin is in the upper left corner
     * @param screenY The y coordinate, origin is in the upper left corner
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            this.leftPressed = true;
            this.touchDownX = screenX;
            this.touchDownY = screenY;
            entity.getEvents().trigger("release spell");
        } else if (button == Input.Buttons.RIGHT) {
            entity.getEvents().trigger("moveLocation", screenX, screenY);
        }
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        if (!this.leftPressed) {
            entity.getEvents().trigger("singleHover", screenX, screenY);
        }

        entity.getEvents().trigger("update pointer", screenX, screenY);
        return false;
    }


    /**
     * To be completed/doesn't work yet:
     * When mouse is clicked and dragged, a rectangle is drawn to tell you the area of units that will be selected.
     * More of a cosmetic function
     *
     * @param screenX X-coordinate of the pointer on screen
     * @param screenY Y-coordinate of the pointer on screen
     * @param pointer the pointer for the event.
     * @return
     */
    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (this.leftPressed) {
            entity.getEvents().trigger("multipleHover", touchDownX, touchDownY, screenX, screenY);
            entity.getEvents().trigger("updateBox", touchDownX,
                    Gdx.graphics.getHeight() - touchDownY, screenX, Gdx.graphics.getHeight() - screenY);
        }
        return false;
    }

    /**
     * handles the finish of the click.
     * If it's a single tap, then we want to select a single unit in out game, if there is an area, then we send
     * this area onto the selectComponent function so that it can determine the units in the area and then select it
     *
     * @param screenX X-coordinate of the pointer on screen
     * @param screenY Y-coordinate of the pointer on screen
     * @param pointer the pointer for the event.
     * @param button the button
     * @return
     */
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int touchUpX = screenX;
        int touchUpY = screenY;

        if (button == Input.Buttons.LEFT) {
            if (touchDownX == touchUpX && touchDownY == touchUpY) {
                entity.getEvents().trigger("singleSelect", touchUpX, touchUpY);
            } else {
                entity.getEvents().trigger("multipleSelect", touchDownX, touchDownY, touchUpX, touchUpY);
            }
            entity.getEvents().trigger("stopBox");
        }
        this.leftPressed = false;
        return false;
    }
}
