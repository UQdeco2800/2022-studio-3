package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
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

        Texture contextBoxTexture = ServiceLocator
                .getResourceService().getAsset("images/context_box.png", Texture.class);

        contextBoxSprite = new Image(contextBoxTexture);
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
        contextBoxItems.clear();
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
                    case BARRACKS -> buildingName = "Barracks";
                    case TOWNHALL -> buildingName = "Town Hall";
                    case WALL, WALL_NE, WALL_SE -> buildingName = "Wall";
                    case GATE_EW -> buildingName = "Gate";
                    case TREBUCHET -> buildingName = "Canon (" + buildingInfo.getType() + ")";
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
            contextBoxItems.setPosition(contextBoxSprite.getX(), contextBoxSprite.getY());
            contextBoxItems.setSize(contextBoxSprite.getWidth(), contextBoxSprite.getHeight());
            buildingNameLabel.setPosition(100f, 300f);
            statsLabel.setPosition(14f, 239f);
            statsLabel.setSize(158f, 25f);
            statsLabel.setWrap(true);
            attributesLabel.setPosition(14f, 11f);
            attributesLabel.setSize(158f, 217f);
            attributesLabel.setWrap(true);
            inventoryLabel.setPosition(184f, 68f);
            inventoryLabel.setSize(208f, 85f);
            inventoryLabel.setWrap(true);
            buildingImage.setPosition(183f, 166f);
            buildingImage.setSize(209f, 96f);

            contextBoxItems.addActor(contextBoxSprite);
            contextBoxItems.addActor(buildingNameLabel);
            contextBoxItems.addActor(statsLabel);
            contextBoxItems.addActor(attributesLabel);
            contextBoxItems.addActor(buildingImage);
            contextBoxItems.addActor(inventoryLabel);
        } else {
            contextBoxItems.remove();
        }
    }
}
