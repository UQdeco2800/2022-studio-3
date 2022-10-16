package com.deco2800.game.components.buildingmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.entities.BuildingType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;
import com.deco2800.game.ui.terminal.commands.ConstructionCommand;
import com.deco2800.game.worker.components.type.BaseComponent;

import java.util.ArrayList;

public class BuildingMenuButton extends UIComponent {
    private TextButton buildingButton;
    private int displayFlag = 0;
    private int barrackFlag = 0;
    private int wallFlag = 0;
    private Image backgroundTexture;
    private Image barracks;
    private Image wall;

    private TextButton barracksButton;
    private TextButton wallButton;

    ConstructionCommand commandBarracks = new ConstructionCommand();
    ConstructionCommand commandWall = new ConstructionCommand();
    ArrayList<String> buildBarracks = new ArrayList<String>();
    ArrayList<String> buildWall = new ArrayList<String>();
    BaseComponent resourcePool = null;
    private boolean activeConstruction = false;

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
        backgroundTexture.remove();
        barracks.remove();
        wall.remove();
        buildingButton.remove();
        barracksButton.remove();
        wallButton.remove();
    }

    private void addActors(){
        buildingButton = new TextButton("Building", skin);
        buildingButton.setPosition(Gdx.graphics.getWidth()-buildingButton.getWidth(),Gdx.graphics.getHeight()/2f-buildingButton.getHeight()/2);

        barracksButton = new TextButton("Cost: 10 wood, 4 stone", skin);
        wallButton = new TextButton("Cost: 4 wood", skin);
        barracksButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                        if(barrackFlag == 1){
                            barrackFlag = 0;
                        }else {
                            if(!activeConstruction && tryTakeCost(10, 0, 4)) {
                                activeConstruction = true;
                                commandBarracks.construct(BuildingType.BARRACKS);
                                activeConstruction = false;
                            }

                        }
                    }
                }
        );

        wallButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                        if(wallFlag == 1){
                            //backgroundTexture.remove();
                            wallFlag = 0;
                        }else{
                            if(!activeConstruction && tryTakeCost(4, 0, 0)) {
                                activeConstruction = true;
                                commandBarracks.construct(BuildingType.WALL);
                                activeConstruction = false;
                            }
                        }
                    }
                }
        );

        buildingButton.addListener(
                new ChangeListener() {
                    @Override
                    public void changed(ChangeListener.ChangeEvent changeEvent, Actor actor) {
                        if(displayFlag == 1){
                            backgroundTexture.remove();
                            barracks.remove();
                            wall.remove();

                            barracksButton.remove();
                            wallButton.remove();

                            displayFlag = 0;
                        }else{
                            addSoldierMenuUI();
                            stage.addActor(barracksButton);
                            stage.addActor(wallButton);

                            displayFlag = 1;
                        }
                    }
                }
        );



        stage.addActor(buildingButton);
        //stage.addActor(barracksButton);
        //stage.addActor(wallButton);
    }

    private boolean tryTakeCost(int wood, int metal, int stone) {
        /* uses the same model as the ShopUIFunctionalityComponent but not in
           abstract hell */
        if (resourcePool == null) {
        boolean entityFound = false;
        Array<Entity> entities =
                ServiceLocator.getEntityService().getEntities();
            for (int i = 0; i < entities.size; i++) {
                Entity bEntity = entities.get(i);
                BuildingActions ba =
                        bEntity.getComponent(BuildingActions.class);
                if (ba != null && ba.getType() == Building.TOWNHALL && bEntity.getComponent(BaseComponent.class) != null) {
                    entityFound = true;
                    resourcePool = bEntity.getComponent(BaseComponent.class);
                    break;
                }
            }
            if (!entityFound)
                return false;
        }

        if (resourcePool.getWood() < wood)
            return false;
        if (resourcePool.getMetal() < metal)
            return false;
        if (resourcePool.getStone() < stone)
            return false;

        resourcePool.updateBaseStats(-wood, -metal, -stone);
        resourcePool.updateDisplay();
        return true;
    }

    private void addMenuComponents(){
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
        wall.setPosition(Gdx.graphics.getWidth()/2-backgroundTexture.getWidth()/2 + + barracks.getWidth() + 50f,
                Gdx.graphics.getHeight()/2-backgroundTexture.getHeight()/2 + 50f);

        //barracksButton = new TextButton("Cost: $$$", skin);
        barracksButton.setPosition(Gdx.graphics.getWidth()/2-backgroundTexture.getWidth()/2 + 50f + barracks.getWidth()/4,
                Gdx.graphics.getHeight()/2-backgroundTexture.getHeight()/2 + 50f);

        //wallButton = new TextButton("Cost: $", skin);
        wallButton.setPosition(Gdx.graphics.getWidth()/2-backgroundTexture.getWidth()/2 + + barracks.getWidth() + 50f + wall.getWidth()/4,
                Gdx.graphics.getHeight()/2-backgroundTexture.getHeight()/2 + 50f);
    }

    private void addSoldierMenuUI(){
        addMenuComponents();

        buildBarracks.add("barracks");
        buildWall.add("2");



        stage.addActor(backgroundTexture);
        stage.addActor(barracks);
        stage.addActor(wall);
    }
}
