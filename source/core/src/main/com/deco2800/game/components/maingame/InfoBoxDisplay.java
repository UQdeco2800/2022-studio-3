package com.deco2800.game.components.maingame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CombatStatsComponent;
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
    private Image healthFrame = new Image(ServiceLocator.getResourceService().getAsset("images/health bar_6.png", Texture.class));
    private ProgressBar healthBar;
    private Label health;

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
        buildingTable.setHeight(200);
        buildingTable.setPosition(400, Gdx.graphics.getHeight()-680);


        levelUpBtn.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {
                        entity.getEvents().trigger("levelUp");
                    }
        });
        bubbleBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entity.getEvents().trigger("bubble");
            }
        });
        troopBtn.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                entity.getEvents().trigger("spawnTroop");
            }
        });

        // health bar

        Pixmap pixmap = new Pixmap(1, (int) (healthFrame.getHeight() * 0.8f), Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.knobBefore = drawable;

        healthBar = new ProgressBar(0, 1, 1f, false, style);
        healthBar.setRound(true);
        health = new Label("", skin);
        health.setFontScale(0.95f);

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
        if (selectedEntities.isEmpty()){
            buildingTable.clear();
        }
        pictureTable.clear();
        infoTable.clear();

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
                    buildingSelected = true;


                    int hp = entity.getComponent(CombatStatsComponent.class).getHealth();
                    int maxHp = entity.getComponent(CombatStatsComponent.class).getMaxHealth();
                    healthBar.setValue(hp);
                    healthBar.setRange(0 , maxHp);

                    Stack stack = new Stack();
                    stack.add(healthBar);
                    stack.add(healthFrame);
                    infoTable.add(stack);
                    infoTable.row();

                    entityName = entity.getComponent(BuildingActions.class).getType().toString();
                    health.setText(String.format("%s: %d/%dHP", entityName, hp, maxHp));
                    infoTable.add(health);
//                    buildingTable.add(levelUpBtn);
//                    switch (entity.getComponent(BuildingActions.class).getType()){
//                        case TOWNHALL:
//                            buildingTable.add(bubbleBtn);
//                            break;
//                        case BARRACKS:
//                            buildingTable.add(troopBtn);
//                            break;
//
//                    }
                } else {
                    buildingTable.clear();
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
            if (!buildingSelected) {
                Label dummyText = new Label("This is "+ entityName, skin, "large");
                infoTable.add(dummyText);
                infoTable.row();
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
