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

    ShapeRenderer shapeRenderer;
    int touchDownX;
    int touchDownY;

    /**
     * Controls the input of the mouse, whether it be clicked or clicked and dragged so that when the player does so,
     * a trigger is sent to the Selectable component with the location and type of click movement
     */
    public MouseInputComponent() {
        super(5);
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT) {
            this.touchDownX = screenX;
            this.touchDownY = screenY;
        }
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
//        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
//        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
//
//
//
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.RED);
//        shapeRenderer.rect(300,300,20,20); //assuming you have created those x, y, width and height variables
//        shapeRenderer.end();
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
//        shapeRenderer.dispose();
        int touchUpX = screenX;
        int touchUpY = screenY;
//        int touchUpY = Gdx.graphics.getHeight() - screenY;
        if (button == Input.Buttons.LEFT) {
            if (touchDownX == touchUpX && touchDownY == touchUpY) {
                entity.getEvents().trigger("click", touchUpX, touchUpY);
            } else {
                entity.getEvents().trigger("dragAndClick", touchDownX, touchDownY, touchUpX, touchUpY);
            }
        }
        return false;
    }
}
