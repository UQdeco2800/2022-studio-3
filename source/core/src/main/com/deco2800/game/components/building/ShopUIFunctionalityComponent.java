package com.deco2800.game.components.building;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.components.BuildingUIDataComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.components.resources.ResourceCountDisplay;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.type.BaseComponent;

/**
 * Adds shop spending and trading functionality.
 */
public class ShopUIFunctionalityComponent extends Component {
    
    @Override
    public void create() {
        BuildingActions ba = entity.getComponent(BuildingActions.class);
        if (ba != null) {
            switch (ba.getType()) {
                case BARRACKS:
                    entity.getEvents().addListener("spawn unit", this::onUnitSpawn);
                    break;
                case LIBRARY:
                    entity.getEvents().addListener("unit upgrade", this::onUnitUpgrade);
                    break;
                case BLACKSMITH:
                    entity.getEvents().addListener("wall upgrade", this::onWallUpgrade);
                    break;
                default:
            }
        }
        entity.getEvents().addListener("exitShop", this::localRemove);
    }

    /**
     * Upgrades soldier units.
     */
    public void onUnitUpgrade() {
        if (!spendResources(0, -30, 0)) {
            return;
        }
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (Entity e : entities) {
            if (this.isUpgradeableUnit(e)) {
                CombatStatsComponent csc = e.getComponent(CombatStatsComponent.class);
                csc.setMaxHealth(csc.getMaxHealth() + 20);
                csc.setHealth(csc.getMaxHealth());
                csc.setBaseDefence(csc.getBaseDefence() + 20);
                csc.setBaseAttack(csc.getBaseAttack() + 20);
            }
        }
    }

    public void localRemove() {
        removeButton();
        SelectableComponent e = entity.getComponent(SelectableComponent.class);
        e.unselect();
    }

    /**
     * Removes the shop UI
     */
    public static void removeButton() {
        Stage s = ServiceLocator.getRenderService().getStage();
        Array<Actor> actors = s.getActors();
        List<Actor> toRemove = new ArrayList<>();
        for (Actor a : actors) {
            if (a.getName() != null && (a.getName().equals("upgrade") || a.getName().equals("shopLabel") ||
                a.getName().equals("levelUp") || a.getName().equals("exit") || a.getName().equals("upgradeCost") ||
                a.getName().equals("levelUpCost"))) {
                    toRemove.add(a);
            }
        }
        toRemove.forEach(a -> a.remove());
    }

    /**
     * Spends resources through the shop UI
     * @param wood amount of wood item costs
     * @param metal amount of metal item costs
     * @param stone amount of stone item costs
     * @return true if player has enough to buy item, else false
     */
    public static boolean spendResources(int wood, int metal, int stone) {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (Entity e : entities) {
            if (ShopUIFunctionalityComponent.isResourceBase(e)) {
                BaseComponent bc = e.getComponent(BaseComponent.class);
                if (bc.getWood() + wood >= 0 && bc.getMetal() + metal >= 0 && bc.getStone() + stone >= 0) {
                    bc.updateBaseStats(wood, metal, stone);
                    bc.updateDisplay();
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean isUpgradeableWall(Entity e) {
        BuildingActions ba = e.getComponent(BuildingActions.class);
        return ba != null && BuildingActions.isWall(ba.getType()) && e.getComponent(CombatStatsComponent.class) != null;
    }

    private boolean isUpgradeableUnit(Entity e) {
        return e.getComponent(CombatStatsComponent.class) != null &&
                e.getComponent(FriendlyComponent.class) != null;
    }

    private static boolean isResourceBase(Entity e) {
        BuildingActions ba = e.getComponent(BuildingActions.class);
        return ba != null && ba.getType() == Building.TOWNHALL && e.getComponent(BaseComponent.class) != null;
    }

    /**
     * Upgrades all game walls.
     */
    public void onWallUpgrade() {
        if (!spendResources(0, 0, -30)) {
            return;
        }
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (int i = 0; i < entities.size; i++) {
            Entity e = entities.get(i);
            if (this.isUpgradeableWall(e)) {
                BuildingActions ba = e.getComponent(BuildingActions.class);
                ba.incrementLevel();
                CombatStatsComponent csc = e.getComponent(CombatStatsComponent.class);
                csc.setMaxHealth(csc.getMaxHealth() + 100);
                csc.setHealth(csc.getMaxHealth());
                csc.setBaseDefence(csc.getBaseDefence() + 20);
            }
        }
    }

    /**
     * Spawns units in the city centre.
     */
    public void onUnitSpawn() {
        System.out.println("Unit spawn event");
        if (!spendResources(0, -30, 0)) {
            return;
        }
        AtlantisGameArea area = ServiceLocator.getGameArea();
        EventHandler handler = area.getGameAreaEventHandler();
        handler.trigger("spawnArcher");
        handler.trigger("spawnHoplite");
        handler.trigger("spawnSpearmint");
    }
}
