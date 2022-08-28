package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.input.InputComponent;

public class MouseInputComponent extends InputComponent {

    int touchDownX;
    int touchDownY;

    public MouseInputComponent() {
        super(5);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        this.touchDownX = screenX;
        this.touchDownY = Gdx.graphics.getHeight() - screenY;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        entity.getEvents().trigger("dragSelect", screenX, screenY);
        return true;
    }
    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int touchUpX = screenX;
        int touchUpY = Gdx.graphics.getHeight() - screenY;

        if (touchDownX == touchUpX && touchDownY == touchUpY) {
            entity.getEvents().trigger("click", touchUpX, touchUpY);
        } else {
            entity.getEvents().trigger("dragAndClick", touchDownX, touchDownX, touchUpX, touchUpY);
        }
        return false;
    }
}
