package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
    private float initialHeight;
    private float initialWidth;
    private Image healthBarFrame;
    private ProgressBar healthBar;
    private Label hp;
    private final String[] textures = {
            "images/context_box.png",
            "images/health bar_6.png",
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
        createHealthBar();
    }

    private void createHealthBar() {
        float width = 180f, height = 36f;
        float x = 220f, y = 60f;
        float progressBarOffset = 25f; // Progress bar x offset from health bar frame
        float xLabelOffset = 45f, yLabelOffset = 18f; // HP label x and y offset from health bar frame
        healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/health bar_6.png",
                Texture.class));
        healthBarFrame.setPosition(x, y);
        healthBarFrame.setWidth(width);
        healthBarFrame.setHeight(height);

        Pixmap pixmap = new Pixmap(1, (int) height, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.RED);
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.knobBefore = drawable;

        healthBar = new ProgressBar(0, 1, 1f, false, style);
        healthBar.setPosition(x + progressBarOffset, y + height / 2f);
        healthBar.setWidth(width - progressBarOffset);

        hp = new Label("", skin);
        hp.setFontScale(0.8f);
        hp.setPosition(x + xLabelOffset, y + yLabelOffset);
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
//                statsString = "Health: " + combatStats.getHealth() +
//                              " Attack: " + combatStats.getBaseAttack() +
//                              " Defence: " + combatStats.getBaseDefence();
                statsString = "Attack: " + combatStats.getBaseAttack() +
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

            attributesLabel.setPosition(220f, 20f);
            attributesLabel.setSize(158f, 217f);
            attributesLabel.setWrap(true);

            int health = combatStats.getHealth(), maxHealth =  combatStats.getMaxHealth();
            healthBar.setRange(0, maxHealth);
            healthBar.setValue(health);
            hp.setText(String.format("%d/%d HP", health, maxHealth));

            buildingImage.setPosition(40f, 50f);
            buildingImage.setSize(130f, 130f);

            contextBoxItems.addActor(contextBoxSprite);
            contextBoxItems.addActor(healthBar);
            contextBoxItems.addActor(healthBarFrame);
            contextBoxItems.addActor(hp);
            contextBoxItems.addActor(buildingNameLabel);
            contextBoxItems.addActor(attributesLabel);
            contextBoxItems.addActor(buildingImage);
        } else {
            contextBoxItems.remove();
        }
    }
}
