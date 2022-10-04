package com.deco2800.game.worker.components.duration;

import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class DurationBarUiComponent extends UIComponent{

    private float x_axis;
    private float y_axis;
    private float duration;
    private DurationBar durationBar;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    public void update(){
        durationBar.setValue(duration);
    }

    @Override
    public void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        float offsetX = 80f;
        float offsetY = 80f;
        durationBar.setPosition(screenWidth - offsetX + x_axis, screenHeight - offsetY + y_axis);
    }

    private void addActors() {
        durationBar = new DurationBar(100, 20);
        stage.addActor(durationBar);
    }

    public void setDuration(float durationValue) {
        duration = durationValue;
    }

    public void setHeightAndWidth(float x ,float y){
        x_axis = x;
        y_axis = y;
    }

    @Override
    public void dispose() {
        durationBar.remove();
        super.dispose();
    }
}
