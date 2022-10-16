package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Null;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
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

        if (isSelected) {
            stage.addActor(contextBoxItems);
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
            try {
                buildingImage = new Image(ServiceLocator.getResourceService()
                        .getAsset(entity.getComponent(TextureRenderComponent.class).texturePath, Texture.class));
            } catch (Exception e) {
                buildingImage = new Image(this.getEntity().getComponent(TextureImageComponent.class).getAsset());
            }

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
        } else {
            contextBoxItems.remove();
        }
    }
}
