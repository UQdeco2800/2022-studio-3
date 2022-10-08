package com.deco2800.game.components.soldiermenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class SoldierMenuDisplay extends UIComponent {
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
        /*
        backgroundShopImage = new Image(texture);
        this.initHeight = backgroundShopImage.getHeight();
        this.initHeight = backgroundShopImage.getWidth();
        backgroundShopImage.setWidth((float) (initWidth * 1.5));
        backgroundShopImage.setHeight((float) (initHeight * 1.5));
        backgroundShopImage.setPosition(0f, 0f);
        this.buildingTable = new Table();
        buildingTable.setWidth(256);
        buildingTable.setHeight(256);
        buildingTable.setPosition(150, Gdx.graphics.getHeight() - 1020);
        buildingTable.setDebug(true);
         */
    }
    @Override
    protected void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        float offsetX = 1000f;
        float offsetY = 800f;
        backgroundTexture.setPosition(screenWidth - offsetX, screenHeight - offsetY);
    }
    @Override
    public void dispose() {
        super.dispose();
        backgroundTexture.remove();
    }
}
