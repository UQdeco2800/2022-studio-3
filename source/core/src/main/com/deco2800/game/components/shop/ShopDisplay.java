package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShopDisplay extends UIComponent {
    //private static final Logger logger = LoggerFactory.getLogger(ShopDisplay.class);
    private Image backgroundShopImage;
    float initHeight, initWidth;
    private static final String image = "images/buildingSelectionMenu.png";
    Table buildingTable;
    Table costTable;
    private Label woodLabel;
    private Label stoneLabel;
    private Label metalLabel;

    private Image resourceLabel;
    private Image stoneImageLabel;
    private Image woodImageLabel;
    private Image metalImageLabel;

    @Override
    public void create(){
        super.create();
        addActors();
    }

    private void addActors() {

        Texture texture = ServiceLocator.getResourceService().getAsset(image, Texture.class);

        backgroundShopImage = new Image(texture);
        backgroundShopImage.setSize(663, 330);

        stage.addActor(backgroundShopImage);
    }

    @Override
    protected void draw(SpriteBatch batch) {
        int screenHeight = Gdx.graphics.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        float offsetX = 1000f;
        float offsetY = 800f;
        backgroundShopImage.setPosition(screenWidth - offsetX, screenHeight - offsetY);
    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundShopImage.remove();
        buildingTable.remove();
        costTable.remove();
    }
}
