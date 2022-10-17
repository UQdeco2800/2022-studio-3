package com.deco2800.game.components;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.building.ShopUIFunctionalityComponent;
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
    public static boolean isSelected = false;
    private static Image contextBoxSprite;
    private Group contextBoxItems;
    private Label shopLabel;
    private TextButton upgrade;
    private Label upgradeCost;
    private TextButton levelUp;
    private Label levelUpCost;
    private float initialHeight;
    private float initialWidth;
    private Image healthBarFrame;
    private ProgressBar healthBar;
    private Label healthPoints;
    private Image attackIcon;
    private Label attackPoints;
    private Image defenseIcon;
    private Label defensePoints;

    private final String[] textures = {
            "images/context_box.png",
            "images/health bar_6.png",
            "images/attack.png",
            "images/defense.png",
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
        shopLabel = new Label("Shop", skin);
        shopLabel.setName("shopLabel");
        upgrade = new TextButton("", skin);
        upgrade.setName("upgrade");
        levelUp = new TextButton("", skin);
        levelUp.setName("levelUp");
        levelUpCost = new Label("", skin);
        levelUpCost.setName("levelUpCost");
        upgradeCost = new Label("", skin);
        upgradeCost.setName("upgradeCost");
        createAttackInfo();
        createDefenseInfo();
        createHealthBar();
    }

    /**
     * Create and configures the actors used for the Health Bar.
     */
    private void createHealthBar() {
        // Size of Health Bar
        float width = 180f;
        float height = 36f;
        // X and Y position of Health Bar
        float x = 210f;
        float y = 140f;
        float progressBarOffset = 25f; // Progress bar x offset from health bar frame
        // HP label x and y offset from health bar frame
        float xLabelOffset = 40f;
        float yLabelOffset = 18f;

        healthBarFrame = new Image(ServiceLocator.getResourceService().getAsset("images/health bar_6.png",
                Texture.class));
        healthBarFrame.setPosition(x, y);
        healthBarFrame.setWidth(width);
        healthBarFrame.setHeight(height);

        Pixmap pixmap = new Pixmap(1, (int) height, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(255, 0, 0, 0.8f));
        pixmap.fill();
        TextureRegionDrawable drawable = new TextureRegionDrawable(new Texture(pixmap));
        pixmap.dispose();
        ProgressBar.ProgressBarStyle style = new ProgressBar.ProgressBarStyle();
        style.knobBefore = drawable;

        healthBar = new ProgressBar(0, 1, 1f, false, style);
        healthBar.setPosition(x + progressBarOffset, y + height / 2f);
        healthBar.setWidth(width - progressBarOffset);

        healthPoints = new Label("", skin);
        healthPoints.setFontScale(0.8f);
        healthPoints.setPosition(x + xLabelOffset, y + yLabelOffset);
    }

    /**
     * Create and configures the actors used for the Attack stats.
     */
    private void createAttackInfo() {
        // Size of Icon
        float width = 36f;
        float height = 36f;
        // X and Y position of Icon
        float x = 210f;
        float y = 100f;
        // Label x and y offset from icon
        float xLabelOffset = 40f;
        float yLabelOffset = 18f;

        attackIcon = new Image(ServiceLocator.getResourceService().getAsset("images/attack.png",
                Texture.class));
        attackIcon.setPosition(x, y);
        attackIcon.setSize(width, height);

        attackPoints = new Label("", skin);
        attackPoints.setFontScale(0.8f);
        attackPoints.setPosition(x + xLabelOffset, y + yLabelOffset);
    }

    /**
     * Create and configures the actors used for the Defense stats.
     */
    private void createDefenseInfo() {
        // Size of Icon
        float width = 31f;
        float height = 36f;
        // X and Y position of Icon
        float x = 210f;
        float y = 60f;
        // Label x and y offset from icon
        float xLabelOffset = 40f;
        float yLabelOffset = 18f;

        defenseIcon = new Image(ServiceLocator.getResourceService().getAsset("images/defense.png",
                Texture.class));
        defenseIcon.setPosition(x, y);
        defenseIcon.setSize(width, height);

        defensePoints = new Label("", skin);
        defensePoints.setFontScale(0.8f);
        defensePoints.setPosition(x + xLabelOffset, y + yLabelOffset);
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
            buildingNameLabel.setPosition(50f, 165f);

            int health = combatStats.getHealth(), maxHealth =  combatStats.getMaxHealth();
            healthBar.setRange(0, maxHealth);
            healthBar.setValue(health);
            healthPoints.setText(String.format("%d/%d HP", health, maxHealth));
            attackPoints.setText(String.format("+%d", combatStats.getBaseAttack()));
            defensePoints.setText(String.format("+%d", combatStats.getBaseDefence()));

            buildingImage.setPosition(45f, 50f);
            buildingImage.setSize(120f, 120f);

            contextBoxItems.addActor(contextBoxSprite);
            contextBoxItems.addActor(attackIcon);
            contextBoxItems.addActor(attackPoints);
            contextBoxItems.addActor(defenseIcon);
            contextBoxItems.addActor(defensePoints);
            contextBoxItems.addActor(healthBar);
            contextBoxItems.addActor(healthBarFrame);
            contextBoxItems.addActor(healthPoints);
            contextBoxItems.addActor(buildingNameLabel);
            contextBoxItems.addActor(buildingImage);

            boolean shop = false;

            // create the shop interface
            if (buildingName.equals("Library:")) {
                shop = true;
                shopLabel = new Label("Shop", skin);
                upgrade = new TextButton("Unit\nupgrade", skin);
                upgrade.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            entity.getEvents().trigger("unit upgrade");
                        }
                    }
                );

                levelUp = new TextButton("Level Up", skin);
                levelUp.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            entity.getEvents().trigger("levelUp");
                        }
                    }
                );
                levelUpCost = new Label("-30\nWood", skin);
                upgradeCost = new Label("-30\nMetal", skin);

            } else if (buildingName.equals("Blacksmith:")) {

                shop = true;
                shopLabel = new Label("Shop", skin);
                upgrade = new TextButton("Wall\nupgrade", skin);
                upgrade.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            entity.getEvents().trigger("wall upgrade");
                        }
                    }
                );
                levelUp = new TextButton("Level Up", skin);
                levelUp.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            entity.getEvents().trigger("levelUp");
                        }
                    }
                );
                levelUpCost = new Label("-30\nWood", skin);
                upgradeCost = new Label("-30\nStone", skin);

            } else if (buildingName.equals("Barracks:")) {

                shop = true;
                shopLabel = new Label("Shop", skin);
                upgrade = new TextButton("Spawn\nunit", skin);
                upgrade.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {
    
                            entity.getEvents().trigger("spawn unit");
                        }
                    }
                );
                levelUp = new TextButton("Level Up", skin);
                levelUp.addListener(
                    new ChangeListener() {
                        @Override
                        public void changed(ChangeEvent changeEvent, Actor actor) {

                            entity.getEvents().trigger("levelUp");
                        }
                    }
                );
                levelUpCost = new Label("-30\nWood", skin);
                upgradeCost = new Label("-30\nMetal", skin);
            }

            if (shop) {
                shopLabel.setPosition(400f, 150f);

                TextButton tb = new TextButton("X", skin);
                tb.addListener(new ChangeListener() {
                    @Override
                    public void changed(ChangeEvent changeEvent, Actor actor) {

                        entity.getEvents().trigger("exitShop");
                    }
                });
                tb.setPosition(475f, 150f);
                tb.setTransform(true);
                tb.setWidth(10f);
                tb.setHeight(20f);

                upgrade.setPosition(400f, 102f);
                levelUp.setPosition(400f, 60f);
                levelUp.getLabel().setFontScale(0.9f);
                levelUp.setTransform(true);
                levelUp.setScaleX(0.8f);
                levelUp.setScaleY(0.8f);

                upgrade.getLabel().setFontScale(0.9f);
                upgrade.setTransform(true);
                upgrade.setWidth(levelUp.getWidth() * 0.8f);
                upgrade.setScaleY(0.8f);

                levelUpCost.setFontScale(0.7f);
                levelUpCost.setPosition(355f, 53f);
                upgradeCost.setFontScale(0.7f);
                upgradeCost.setPosition(355f, 96f);

                shopLabel.setName("shopLabel");
                upgrade.setName("upgrade");
                levelUp.setName("levelUp");
                levelUpCost.setName("levelUpCost");
                upgradeCost.setName("upgradeCost");
                tb.setName("exit");

                stage.addActor(shopLabel);
                stage.addActor(upgrade);
                stage.addActor(levelUp);
                stage.addActor(levelUpCost);
                stage.addActor(upgradeCost);
                stage.addActor(tb);
            }

        } else {
            contextBoxItems.remove();
        }
    }

    public void remove() {
        contextBoxItems.remove();
    }
}
