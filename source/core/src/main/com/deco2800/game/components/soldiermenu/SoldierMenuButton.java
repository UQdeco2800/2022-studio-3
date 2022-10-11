package com.deco2800.game.components.soldiermenu;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class SoldierMenuButton extends UIComponent {
    private TextButton buildingButton;
    private int displayFlag = 0;
    private Image backgroundTexture;

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
        buildingButton.setPosition(100f,100f);

        buildingButton.addListener(
            new ChangeListener() {
                @Override
                public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                    if(displayFlag == 1){
                        backgroundTexture.remove();
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
        backgroundTexture.setPosition(100,200);



        stage.addActor(backgroundTexture);
    }
}
