package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class InfoBoxDisplay extends UIComponent {

    Table table;

    private Image backgroundBoxImage;

    @Override
    public void create() {
        super.create();
        addActors();

    }

    private void addActors() {

        backgroundBoxImage = new Image(ServiceLocator.getResourceService().getAsset("images/Information_Box_Deepsea.png", Texture.class));
        stage.addActor(backgroundBoxImage);
    }

    @Override
    public void draw(SpriteBatch batch)  {
        backgroundBoxImage.setWidth(1000);
        backgroundBoxImage.setHeight(700);

        backgroundBoxImage.setPosition(-128f, -307f);
        // draw is handled by the stage
    }
}
