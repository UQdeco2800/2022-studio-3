package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Null;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

public class BuildingUIDataComponent extends UIComponent {
    private CombatStatsComponent combatStats;
    private BuildingActions buildingInfo;
    private SelectableComponent selectableComponent;
    private boolean isSelected = false;
    private static Image contextBoxSprite;
    private Group contextBoxItems;
    // private Table table;
    private Label shopLabel;
    private TextButton upgrade;
    private TextButton levelUp;
    private float initialHeight;
    private float initialWidth;
    private final String[] textures = {
            "images/context_box.png",
    };

    private void loadAssets() {

        ResourceService resourceService = ServiceLocator.getResourceService();
        if (resourceService == null)
            return;
        resourceService.loadTextures(this.textures);
        resourceService.loadAll();
    }

    @Override
    public void create() {
        super.create();
        loadAssets();

        combatStats = this.entity.getComponent(CombatStatsComponent.class);
        buildingInfo = this.entity.getComponent(BuildingActions.class);
        selectableComponent = this.entity.getComponent(SelectableComponent.class);
        isSelected = selectableComponent.isSelected();
        contextBoxItems = new Group();
        // table = new Table();
        shopLabel = new Label("", skin);
        shopLabel.setName("shopLabel");
        upgrade = new TextButton("", skin);
        upgrade.setName("upgrade");
        levelUp = new TextButton("", skin);
        levelUp.setName("levelUp");

    }

    /**
     * Draw the render-able. Should be called only by the renderer, not manually.
     *
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {

        /* TODO:
                - get building attributes and display
                - get building name and display
                - get building actions and generate buttons accordingly
         */
        Label attributesLabel;
        Label inventoryLabel;
        Label statsLabel;
        Label buildingNameLabel;
        Image buildingImage;

        if (selectableComponent != null)
            isSelected = selectableComponent.isSelected();

        Texture contextBoxTexture = ServiceLocator
                .getResourceService().getAsset("images/Information_Box_Deepsea.png", Texture.class);
        contextBoxSprite = new Image(contextBoxTexture);

        contextBoxItems.clear();
        contextBoxSprite.clear();
        // shopLabel.remove();
        // upgrade.remove();
        // levelUp.remove();
        // table.clear();

        // Label shopLabel = new Label("", skin);
        // TextButton upgrade = new TextButton("", skin);
        // TextButton levelUp = new TextButton("", skin);
        // Table table = new Table();

        if (isSelected) {
            stage.addActor(contextBoxItems);
            // stage.addActor(table);
            String statsString = "";

            try {
                statsString = "Health: " + combatStats.getHealth() +
                              " Attack: " + combatStats.getBaseAttack() +
                              " Defence: " + combatStats.getBaseDefence();
            } catch (NullPointerException nullPointerException) {
                // pass
            }
            String buildingName = "";
            try {
                switch (buildingInfo.getType()) {
                    case BARRACKS -> buildingName = "Barracks:";
                    case TOWNHALL -> buildingName = "Town Hall:";
                    case WALL, CONNECTOR_EW, CONNECTOR_NS -> buildingName = "Wall:";
                    case GATE_EW, GATE_NS -> buildingName = "Gate";
                    case TREBUCHET -> buildingName = "Canon (" + buildingInfo.getLevel() + "):";
                    case LIBRARY -> buildingName = "Library:";
                    case FARM -> buildingName = "Farm:";
                    case BLACKSMITH -> buildingName = "Blacksmith:";
                }
            } catch (NullPointerException nullPointerException) {
                System.out.println(nullPointerException.getMessage());
                // pass
            }
            buildingNameLabel = new Label(buildingName, skin);
            statsLabel = new Label("Stats: ", skin);
            attributesLabel = new Label(statsString, skin);
            inventoryLabel = new Label("inventory things...", skin);
            buildingImage = new Image(ServiceLocator.getResourceService()
                    .getAsset(entity.getComponent(TextureRenderComponent.class).texturePath, Texture.class));

            /* TODO: create small context box for smaller window sizes */
            contextBoxSprite.setWidth(contextBoxSprite.getWidth() * 1.5f);
            contextBoxSprite.setHeight(contextBoxSprite.getHeight() * 1.5f);

            contextBoxItems.setPosition(contextBoxSprite.getX(), contextBoxSprite.getY());
            contextBoxItems.setSize(contextBoxSprite.getWidth(), contextBoxSprite.getHeight());
            buildingNameLabel.setPosition(220f, 160f);

            attributesLabel.setPosition(220f, 2f);
            attributesLabel.setSize(158f, 217f);
            attributesLabel.setWrap(true);

            buildingImage.setPosition(40f, 50f);
            buildingImage.setSize(130f, 130f);

            contextBoxItems.addActor(contextBoxSprite);
            contextBoxItems.addActor(buildingNameLabel);
            contextBoxItems.addActor(attributesLabel);
            contextBoxItems.addActor(buildingImage);

            // create the shop interface
            if (buildingName.equals("Library:")) {
                shopLabel = new Label("Shop", skin);
                upgrade = new TextButton("Unit\nupgrade", skin);
                upgrade.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            // logger.debug("Skip button clicked");
                            entity.getEvents().trigger("unit upgrade");
                        }
                    }
                );
                shopLabel.setPosition(370f, 160f);
                upgrade.setPosition(360f, 107f);

                levelUp = new TextButton("Level Up", skin);
                levelUp.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            // logger.debug("Skip button clicked");
                            entity.getEvents().trigger("levelUp");
                        }
                    }
                );
                levelUp.setPosition(360f, 60f);
                levelUp.getLabel().setFontScale(0.9f);
                levelUp.setTransform(true);
                levelUp.setScaleX(0.8f);
                shopLabel.setName("shopLabel");
                upgrade.setName("upgrade");
                levelUp.setName("levelUp");
                stage.addActor(shopLabel);
                stage.addActor(upgrade);
                stage.addActor(levelUp);

            } else if (buildingName.equals("Blacksmith:")) {

                shopLabel = new Label("Shop", skin);
                upgrade = new TextButton("Wall\nupgrade", skin);
                upgrade.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            // logger.debug("Skip button clicked");
                            entity.getEvents().trigger("wall upgrade");
                        }
                    }
                );
                shopLabel.setPosition(370f, 160f);
                upgrade.setPosition(360f, 107f);
                levelUp = new TextButton("Level Up", skin);
                levelUp.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            // logger.debug("Skip button clicked");
                            entity.getEvents().trigger("levelUp");
                        }
                    }
                );
                levelUp.setPosition(360f, 60f);
                levelUp.getLabel().setFontScale(0.9f);
                levelUp.setTransform(true);
                levelUp.setScaleX(0.8f);
                shopLabel.setName("shopLabel");
                upgrade.setName("upgrade");
                levelUp.setName("levelUp");
                stage.addActor(shopLabel);
                stage.addActor(upgrade);
                stage.addActor(levelUp);

            } else if (buildingName.equals("Barracks:")) {

                shopLabel = new Label("Shop", skin);
                upgrade = new TextButton("Spawn\nunit M10", skin);
                upgrade.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            // logger.debug("Skip button clicked");
                            entity.getEvents().trigger("spawn unit");
                        }
                    }
                );
                shopLabel.setPosition(370f, 160f);
                upgrade.setPosition(360f, 107f);
                levelUp = new TextButton("Level Up W10", skin);
                levelUp.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            // logger.debug("Skip button clicked");
                            entity.getEvents().trigger("levelUp");
                        }
                    }
                );
                levelUp.setPosition(360f, 60f);
                levelUp.getLabel().setFontScale(0.9f);
                levelUp.setTransform(true);
                levelUp.setScaleX(0.8f);
                shopLabel.setName("shopLabel");
                upgrade.setName("upgrade");
                levelUp.setName("levelUp");
                stage.addActor(shopLabel);
                stage.addActor(upgrade);
                stage.addActor(levelUp);
            }
        } else {
            contextBoxItems.remove();
            // table.remove();
            // shopLabel.remove();
            // upgrade.remove();
            // levelUp.remove();
        }
    }
}
