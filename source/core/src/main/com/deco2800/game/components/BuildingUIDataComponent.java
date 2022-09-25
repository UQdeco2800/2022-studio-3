package com.deco2800.game.components;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.files.UserSettings;
import com.deco2800.game.ui.UIComponent;

public class BuildingUIDataComponent extends UIComponent {
    private CombatStatsComponent combatStats;
    private BuildingActions buildingInfo;
    private SelectableComponent selectableComponent;
    private boolean isSelected = false;

    private Table infoTable;
    UserSettings.Settings settings = UserSettings.get();

    @Override
    public void create() {
        super.create();

        combatStats = this.entity.getComponent(CombatStatsComponent.class);
        buildingInfo = this.entity.getComponent(BuildingActions.class);
        selectableComponent = this.entity.getComponent(SelectableComponent.class);

        isSelected = selectableComponent.isSelected();

        this.infoTable = new Table();
        infoTable.setWidth(305);
        infoTable.setHeight(135);

        if (settings.fullscreen) {
            infoTable.setPosition(200, Gdx.graphics.getHeight()-1020);
        } else {
            infoTable.setPosition(200, Gdx.graphics.getHeight()-745);
        }

    }

    /**
     * Draw the renderable. Should be called only by the renderer, not manually.
     *
     * @param batch Batch to render to.
     */
    @Override
    protected void draw(SpriteBatch batch) {
        // drawing the UI elements
        if (selectableComponent != null) {
            isSelected = selectableComponent.isSelected();
        }
        // clear the UI, to prevent it from printing across the height of the screen.
        infoTable.clear();
        if(isSelected) {
            // draw UI
            stage.addActor(infoTable);
            Label attackText = new Label("Attack:"+combatStats.getBaseAttack(), skin, "large");
            Label defenceText = new Label("Defence:"+combatStats.getBaseDefence(), skin, "large");

            infoTable.add(attackText);
            infoTable.row();
            infoTable.add(defenceText);
            infoTable.row();

//            infoTable.add(defenceText);
//            infoTable.row();
        } else {
            // get rid of UI
            infoTable.remove();
        }
    }
}