package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.security.Provider;

public class InfoBoxDisplay extends UIComponent {

    Table pictureTable;
    Table infoTable;

    float initialHeight;
    float initialWidth;
    private Image backgroundBoxImage;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        backgroundBoxImage = new Image(ServiceLocator.getResourceService().getAsset("images/Information_Box_Deepsea.png", Texture.class));

        this.initialHeight = backgroundBoxImage.getHeight();
        this.initialWidth = backgroundBoxImage.getWidth();

        backgroundBoxImage.setWidth((float) (initialWidth * 1.5));
        backgroundBoxImage.setHeight((float) (initialHeight * 1.5));
        backgroundBoxImage.setPosition(-0f, -0f);

        stage.addActor(backgroundBoxImage);

        this.pictureTable = new Table();
        pictureTable.setWidth(135);
        pictureTable.setHeight(135);
        pictureTable.setPosition(38, Gdx.graphics.getHeight() - 748);

        this.infoTable = new Table();
        infoTable.setWidth(305);
        infoTable.setHeight(135);
        infoTable.setPosition(200, Gdx.graphics.getHeight() - 748);

        stage.addActor(pictureTable);
        stage.addActor(infoTable);



    }

    public void updateTables() {
        Array<Entity> selectedEntities = new Array<>();
        for (Entity entity: ServiceLocator.getEntityService().getEntities()) {
            SelectableComponent selectedComponent = entity.getComponent(SelectableComponent.class);
            if (selectedComponent != null && selectedComponent.isSelected()) {
                selectedEntities.add(entity);
            }
        }

        pictureTable.clear();
        if (!selectedEntities.isEmpty()) {
            int length = selectedEntities.size;
            int sideLength = (int) Math.ceil(Math.sqrt(length));
            int row = 0;

            for (Entity entity: selectedEntities) {
                if (row == sideLength) {
                    pictureTable.row();
                    row = 0;
                }
                Image dummyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/heart.png", Texture.class));
                pictureTable.add(dummyImage);
                dummyImage.setWidth(135/sideLength);
                dummyImage.setHeight(135/sideLength);
                row++;
            }


        }
    }

    @Override
    public void draw(SpriteBatch batch)  {
        updateTables();

    }

    @Override
    public void dispose() {
        super.dispose();
        backgroundBoxImage.remove();
        pictureTable.remove();
        infoTable.remove();
    }
}
