package com.deco2800.game.components.shop;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ShopDisplay extends UIComponent {
    //private final ShopDisplay shopBox;
    //private static final Logger logger = LoggerFactory.getLogger(ShopDisplay.class);
    private Image backgroundShopImage;
    float initHeight, initWidth;
    private static final String image = "images/buildingSelectionMenu.png";
    Table buildingTable;
    Table costTable;
    //UserSettings.Settings settings = UserSettings.get();
    @Override
    public void create(){
        super.create();
        addActors();
    }

    private void addActors() {
        Texture texture = ServiceLocator.getResourceService().getAsset(image, Texture.class);

        backgroundShopImage = new Image(texture);
        this.initHeight = backgroundShopImage.getHeight();
        this.initHeight = backgroundShopImage.getWidth();
        backgroundShopImage.setWidth((float) (initWidth * 1.5));
        backgroundShopImage.setHeight((float) (initHeight * 1.5));
        backgroundShopImage.setPosition(0f, 0f);

        this.buildingTable = new Table();
        buildingTable.setDebug(true);
        buildingTable.setWidth(256);
        buildingTable.setHeight(256);
        buildingTable.setPosition(150, Gdx.graphics.getHeight() - 1020);
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundShopImage.remove();
        buildingTable.remove();
        costTable.remove();
    }
}
