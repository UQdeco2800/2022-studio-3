package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BuildingConfigs;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import org.slf4j.helpers.Util;

import java.security.Provider;

public class InfoBoxDisplay extends UIComponent {

    //Table displayed for unit picture
    Table pictureTable;
    //Table displayed for corresponding stats on units
    Table infoTable;

    Table buildingTable;

    //initial height and width of the box, can be imple
    float initialHeight;
    float initialWidth;
    //The image that is always in the background of our information
    private Image backgroundBoxImage;

    //Button used for leveling up buildings
    private TextButton levelUpBtn = new TextButton("Level Up", skin);

    //Troop selection button for the barracks
    private TextButton troopBtn = new TextButton("Spawn troop", skin);

    //Button to create the magic bubble to save Atlantis
    private TextButton bubbleBtn = new TextButton("Create bubble", skin);

    UserSettings.Settings settings = UserSettings.get();


    @Override
    public void create() {
        super.create();
        addActors();
    }

    /**
     * We add the background information box first and then the tables fill up the whitespace in the information box
     */
    private void addActors() {
        backgroundBoxImage = new Image(ServiceLocator.getResourceService().getAsset("images/Information_Box_Deepsea.png", Texture.class));

        this.initialHeight = backgroundBoxImage.getHeight();
        this.initialWidth = backgroundBoxImage.getWidth();

        backgroundBoxImage.setWidth((float) (initialWidth * 1.5));
        backgroundBoxImage.setHeight((float) (initialHeight * 1.5));
        backgroundBoxImage.setPosition(0f, 0f);



        this.pictureTable = new Table();
        pictureTable.setWidth(135);
        pictureTable.setHeight(135);
        if(settings.fullscreen) {
            pictureTable.setPosition(38, Gdx.graphics.getHeight() - 1020);
        }
        else {
            pictureTable.setPosition(38, Gdx.graphics.getHeight() - 745);
        }

        this.infoTable = new Table();
        infoTable.setWidth(305);
        infoTable.setHeight(135);
        if(settings.fullscreen) {
            infoTable.setPosition(200, Gdx.graphics.getHeight() - 1020);
        }
        else {
            infoTable.setPosition(200, Gdx.graphics.getHeight() - 745);
        }


        this.buildingTable = new Table();
        buildingTable.setWidth(100);
        buildingTable.setHeight(100);
        buildingTable.setPosition(400, Gdx.graphics.getHeight()-680);


        levelUpBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        Util.report("level up");
                        entity.getEvents().trigger("levelUp");
                    }
        });
        bubbleBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Util.report("Create bubble");
                entity.getEvents().trigger("bubble");
            }
        });
        troopBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Util.report("Spawn troop");
                entity.getEvents().trigger("spawnTroop");
            }
        });


    }

    /**
     * At each draw function we should update the tables to see if there are any new selected units
     * and draw them appropriately
     */
    public void updateTables() {
        Array<Entity> selectedEntities = new Array<>();
        //Check for selected units
        for (Entity entity: ServiceLocator.getEntityService().getEntities()) {
            SelectableComponent selectedComponent = entity.getComponent(SelectableComponent.class);
            if (selectedComponent != null && selectedComponent.isSelected()) {
                selectedEntities.add(entity);
            }
        }

        //clear old tables
        pictureTable.clear();
        infoTable.clear();
        buildingTable.clear();
        String entityName = "";
        boolean buildingSelected = false;
        //If there are entities selected
        if (!selectedEntities.isEmpty()) {
            stage.addActor(backgroundBoxImage);
            stage.addActor(pictureTable);
            stage.addActor(infoTable);
            stage.addActor(buildingTable);
            int length = selectedEntities.size;
            int sideLength = (int) Math.ceil(Math.sqrt(length));
            int column = 0;
            int row = 1;


            backgroundBoxImage.setHeight((float) (initialHeight * 1.5));
            backgroundBoxImage.setWidth((float) (initialWidth * 1.5));
            // add pictures to the table. Pictures right now are just hearts but can be updated later on
            // to represent the entity
            for (Entity entity: selectedEntities) {

                if (column == sideLength) {
                    pictureTable.row();
                    column = 0;
                    row++;
                }
                //crashes game when not selecting building
                if (entity.getComponent(BuildingActions.class) != null){
                    buildingTable.add(levelUpBtn);
                    switch (entity.getComponent(BuildingActions.class).getType()){
                        case TOWNHALL:
                            buildingTable.add(bubbleBtn);
                            break;
                        case BARRACKS:
                            buildingTable.add(troopBtn);
                            break;

                    }
                }


                Image dummyImage = new Image(ServiceLocator.getResourceService()
                        .getAsset(entity.getComponent(TextureRenderComponent.class).texturePath, Texture.class));
                pictureTable.add(dummyImage);
                dummyImage.setWidth(135/sideLength);
                dummyImage.setHeight(135/sideLength);
                column++;
                if (buildingSelected){
                    break;
                }
            }


            while (row < sideLength) {
                Image blankImage = new Image(ServiceLocator.getResourceService()
                        .getAsset("images/white.png", Texture.class));
                pictureTable.row();
                blankImage.setHeight(135/sideLength);
                blankImage.setWidth(135/sideLength);
                pictureTable.add(blankImage);
                row++;
            }


            //Added functionality later because there is currently not enough information on the units we will have
            Label dummyText = new Label("This is "+ entityName, skin, "large");
            infoTable.add(dummyText);
            infoTable.row();
            if (buildingSelected){
                Label boxBoyText = new Label(String.format("%s:   %d", entityName, length), skin, "large");
                infoTable.add(boxBoyText);
            } else {
                Label boxBoyText = new Label(String.format("Boxboy:   %d", length), skin, "large");
                infoTable.add(boxBoyText);
            }


        } else {
            pictureTable.remove();
            infoTable.remove();
            backgroundBoxImage.remove();
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
