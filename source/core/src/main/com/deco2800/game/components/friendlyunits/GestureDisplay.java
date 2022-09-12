package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.deco2800.game.components.Component;
import com.deco2800.game.ui.UIComponent;

import java.awt.*;

public class GestureDisplay extends UIComponent {

    ShapeRenderer shapeRenderer;
    int xPos;
    int yPos;
    int width;
    int height;

    @Override
    public void create() {
        super.create();
        shapeRenderer = new ShapeRenderer();
        entity.getEvents().addListener("updateBox", this::updateBox);
        entity.getEvents().addListener("stopBox", this::stopBox);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(0.5f, 0.5f, 0.5f, 0.5f);
        shapeRenderer.rect(xPos, yPos, width, height);
        shapeRenderer.end();
    }

    @Override
    public void update() {

    }


    public void updateBox(int oneX, int oneY, int twoX, int twoY) {
        width = Math.abs(oneX - twoX);
        height = Math.abs(oneY - twoY);
        xPos = Math.min(oneX, twoX);
        yPos = Math.min(oneY, twoY);
    }

    public void stopBox() {
        width = 0;
        height = 0;
    }
}
