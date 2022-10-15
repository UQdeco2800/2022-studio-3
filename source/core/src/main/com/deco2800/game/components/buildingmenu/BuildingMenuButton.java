package com.deco2800.game.components.buildingmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class BuildingMenuButton extends UIComponent {
    private TextButton buildingButton;
    private int displayFlag = 0;
    private Image backgroundTexture;
    private Image barracks;
    private Image wall;

    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    protected void draw(SpriteBatch batch) {

    }

    @Override
    public void dispose(){
        super.dispose();
        buildingButton.remove();
    }

    private void addActors(){
        buildingButton = new TextButton("Building",skin);
        buildingButton.setPosition(Gdx.graphics.getWidth()-buildingButton.getWidth(),Gdx.graphics.getHeight()/2-buildingButton.getHeight()/2);

        buildingButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                        if(displayFlag == 1){
                            backgroundTexture.remove();
                            barracks.remove();
                            wall.remove();
                            displayFlag = 0;
                        }else{
                            addSoldierMenuUI();
                            displayFlag = 1;
                        }
                    }
                }
        );

        stage.addActor(buildingButton);

    }

    private void addSoldierMenuUI(){
        backgroundTexture = new Image(ServiceLocator.
                getResourceService().getAsset("images/building-selection-menu.png", Texture.class));
        backgroundTexture.setSize(663, 330);
        backgroundTexture.setPosition(Gdx.graphics.getWidth()/2-backgroundTexture.getWidth()/2,Gdx.graphics.getHeight()/2-backgroundTexture.getHeight()/2);

        barracks = new Image(ServiceLocator.getResourceService().getAsset("images/barracks_level_1.0.png", Texture.class));
        barracks.setSize(barracks.getWidth()/4, barracks.getHeight()/4);
        barracks.setPosition(Gdx.graphics.getWidth()/2-backgroundTexture.getWidth()/2 + 50f,
                Gdx.graphics.getHeight()/2-backgroundTexture.getHeight()/2 + 50f);

        wall = new Image(ServiceLocator.getResourceService().getAsset("images/wooden_wall.png", Texture.class));
        wall.setSize(wall.getWidth()/4, wall.getHeight()/4);
        //wall.setPosition(100f,150f);
        wall.setPosition(Gdx.graphics.getWidth()/2-backgroundTexture.getWidth()/2 + + barracks.getWidth() + 50f,
                Gdx.graphics.getHeight()/2-backgroundTexture.getHeight()/2 + 50f);

        stage.addActor(backgroundTexture);
        stage.addActor(barracks);
        stage.addActor(wall);
    }
}
