package com.deco2800.game.components.buildingmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;


public class BuildingMenuDisplay extends UIComponent{
    private Image backgroundTexture;
    float initHeight;
    float initWidth;
    @Override
    public void create() {
        super.create();
        addActors();
    }
    private void addActors() {
    Texture texture = ServiceLocator.
            getResourceService().getAsset("images/building-selection-menu.png", Texture.class);

    backgroundTexture = new Image(texture);
        this.initHeight = backgroundTexture.getHeight();
        this.initWidth = backgroundTexture.getWidth();
        backgroundTexture.setSize(663, 330);

        stage.addActor(backgroundTexture);
}
    @Override
    protected void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        float offsetX = 500f;
        float offsetY = 500f;
        backgroundTexture.setPosition(screenWidth - offsetX, screenHeight - offsetY);
    }
    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.remove();
    }
}

