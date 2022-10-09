package com.deco2800.game.components.soldiermenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;

public class SoldierMenuDisplay extends UIComponent {

    private TextButton soldierDisplayButton;
    private Image backgroundTexture;
    private Image archerTexture;
    private Image swordsmanTexture;
    private Image spearmanTexture;
    private Image hopliteTexture;
    private boolean isClicked = false;

    float initHeight;
    float initWidth;
    
    @Override
    public void create() {
        super.create();

        // Add "Create Soldier Button" with its listener
        soldierDisplayButton = new TextButton("Spawn soldier", skin);
        soldierDisplayButton.addListener(
                new ChangeListener(){
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
                                if(isClicked == false){
                                        isClicked = true;
                                }else{
                                        isClicked = false;
                                }
                        }
                }
        );

        // Place the soldier button at the bottom left corner of the screen
        soldierDisplayButton.setPosition(0, 0);
        stage.addActor(soldierDisplayButton);
        
        createActors();
    }

    private void createActors() {
        Texture texture = ServiceLocator.
                getResourceService().getAsset("images/character-selection-menu.png", Texture.class);
        Texture archer = ServiceLocator.
                getResourceService().getAsset("images/archer_avatar.png", Texture.class);
        Texture swordsman = ServiceLocator.
                getResourceService().getAsset("images/swordsman_avatar.png", Texture.class);
        Texture spearman = ServiceLocator.
                getResourceService().getAsset("images/spearman_avatar.png", Texture.class);
        Texture hoplite = ServiceLocator.
                getResourceService().getAsset("images/hoplite_avatar.png", Texture.class);

        backgroundTexture = new Image(texture);
        archerTexture = new Image(archer);
        swordsmanTexture = new Image(swordsman);
        spearmanTexture = new Image(spearman);
        hopliteTexture = new Image(hoplite);

        this.initHeight = backgroundTexture.getHeight();
        this.initWidth = backgroundTexture.getWidth();
        backgroundTexture.setSize(663, 330);

        archerTexture.setSize(100,100);
        swordsmanTexture.setSize(100,100);
        spearmanTexture.setSize(100,100);
        hopliteTexture.setSize(100,100);

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

    private void addActors(){
        stage.addActor(backgroundTexture);
        stage.addActor(archerTexture);
        stage.addActor(swordsmanTexture);
        stage.addActor(spearmanTexture);
        stage.addActor(hopliteTexture);
    }

    private void removeDisplay(){
        backgroundTexture.remove();
        archerTexture.remove();
        swordsmanTexture.remove();
        spearmanTexture.remove();
        hopliteTexture.remove();
    }

    @Override
    protected void draw(SpriteBatch batch) {

        if(isClicked){
                addActors();
                int screenHeight = Gdx.graphics.getHeight();
                int screenWidth = Gdx.graphics.getWidth();
                backgroundTexture.setPosition(screenWidth - screenWidth*3/4,0);
                archerTexture.setPosition(screenWidth*15/45, screenHeight/6);
                swordsmanTexture.setPosition(screenWidth*19/45, screenHeight/6);
                spearmanTexture.setPosition(screenWidth*23/45, screenHeight/6);
                hopliteTexture.setPosition(screenWidth*27/45, screenHeight/6);
        }else{
                removeDisplay();
        }

    }
    @Override
    public void dispose() {
        super.dispose();
        soldierDisplayButton.remove();
        removeDisplay();
    }
}
