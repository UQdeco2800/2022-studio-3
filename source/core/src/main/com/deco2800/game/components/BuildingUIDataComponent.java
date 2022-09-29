package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.ArrayList;

public class BuildingUIDataComponent extends UIComponent {
    private CombatStatsComponent combatStats;
    private BuildingActions buildingInfo;
    private SelectableComponent selectableComponent;
    private boolean isSelected = false;
    private static Table infoTable;
    private static Image flagSprite;
    private static Image columnBaseSprite;
    private static Image columnConnectorSprite;
    private ArrayList<Label> buildingStats;
    private final String[] textures = {
            "images/context_flag_90x90.png",
            "images/greek_column_base_90x90.png",
            "images/greek_column_connector_90x90.png",
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

        Texture flagTexture = ServiceLocator.getResourceService()
                .getAsset("images/context_flag_90x90.png", Texture.class);
        Texture columnBaseTexture = ServiceLocator.getResourceService()
                .getAsset("images/greek_column_base_90x90.png", Texture.class);
        Texture columnConnectorTexture = ServiceLocator.getResourceService()
                .getAsset("images/greek_column_connector_90x90.png", Texture.class);

        infoTable = new Table();
        flagSprite = new Image(flagTexture);
        columnBaseSprite = new Image(columnBaseTexture);
        columnConnectorSprite = new Image(columnConnectorTexture);
        this.buildingStats = new ArrayList<>();
    }

    /**
     * Creates and returns a table containing the context information of the currently selected building
     *
     * @param screenWidth  - current width of screen
     * @param screenHeight - current height of screen
     * @return - table containing context information of currently selected building
     * <p>
     * Note: If the current screen size is below a certain size, a compact version of the context table is returned
     */
    private Table generateTable(float screenWidth, float screenHeight) {

        Table table = new Table();
        /* TODO: access selected building name */
        Label name = new Label("BUILD_NAME", skin);
        VerticalGroup stats = new VerticalGroup();
        Pixmap tableBackground;
        Image building = new Image(ServiceLocator.getResourceService()
                .getAsset(entity.getComponent(TextureRenderComponent.class).texturePath, Texture.class));

        for (Label stat : this.buildingStats) {

            stats.addActor(stat);
        }

        if (screenHeight <= 540f || screenWidth <= 960f) {

            tableBackground = new Pixmap(390, 180, Pixmap.Format.RGB565);

            table.setSize(390, 180);
            table.add(name).expand().colspan(2).center().row();
            table.add(building).expand().width(180).height(180).fill();
            table.add(stats).expand().center();
        } else {

            HorizontalGroup titleRow = new HorizontalGroup().fill();
            VerticalGroup columns = new VerticalGroup().fill();
            tableBackground = new Pixmap(480, 360, Pixmap.Format.RGB565);

            table.setSize(480, 360);
            titleRow.addActor(flagSprite);

            columns.addActor(columnConnectorSprite);
            columns.addActor(columnBaseSprite);

            /* TODO: add close button on far right */
            table.add(flagSprite).expand().left().top().width(90).height(90);
            table.add(name);
            table.add(columnBaseSprite).expand().right().top().row();
            table.add(building).expand().left().width(180).height(180);
            table.add(stats).expand().center();
            /* FIXME: column_base does not show */
            table.add(columns).expand().right().bottom();
        } /* screenHeight > 540 && screenWidth > 960 */

        tableBackground.setColor(25, 25, 25, 1);
        tableBackground.fill();
        table.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(tableBackground))));
        table.pack();
        table.setPosition(0f, 0f, Align.bottomLeft);
        return (table);
    }

    /**
     * Draw the renderable. Should be called only by the renderer, not manually.
     *
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {

        if (selectableComponent != null) {
            isSelected = selectableComponent.isSelected();
        }
        if (isSelected) {

            buildingStats.clear();
            buildingStats.add(new Label("Health: " + combatStats.getHealth(), skin));
            buildingStats.add(new Label("Level: " + buildingInfo.getLevel(), skin));

            infoTable.clear();
            infoTable = generateTable(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            stage.addActor(infoTable);
        } else {

            infoTable.remove();
        }
    }
}