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
        backgroundBoxImage.setWidth(Gdx.graphics.getWidth());
        backgroundBoxImage.setHeight(Gdx.graphics.getHeight());

        backgroundBoxImage.setPosition(0f, 0f);
        // draw is handled by the stage
    }
}
