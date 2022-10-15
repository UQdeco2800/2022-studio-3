package com.deco2800.game.components.building;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.resources.ResourceCountDisplay;
import com.deco2800.game.entities.Entity;
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
                    System.out.println("Added barracks event");
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
    }

    private void onUnitUpgrade() {
        System.out.println("Unit upgrade event");
        if (!spendResources(0, -10, 0)) {
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
            // if (this.isResourceBase(e)) {
            //     spendResources(e, 0, -10, 0);
            // }
        }
        removeButton();
    }

    public static void removeButton() {
        Stage s = ServiceLocator.getRenderService().getStage();
        Array<Actor> actors = s.getActors();
        List<Actor> toRemove = new ArrayList<>();
        for (Actor a : actors) {
            if (a.getName() != null && (a.getName().equals("upgrade") || a.getName().equals("shopLabel") ||
                a.getName().equals("levelUp"))) {
                    System.out.println(a.getName());
                    toRemove.add(a);
            }
        }
        toRemove.forEach(a -> a.remove());
    }

    private boolean spendResources(int wood, int metal, int stone) {
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (Entity e : entities) {
            if (this.isResourceBase(e)) {
                BaseComponent bc = e.getComponent(BaseComponent.class);
                if (bc.getWood() + wood > 0 && bc.getMetal() + metal > 0 && bc.getStone() + stone > 0) {
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
        return ba != null && ba.isWall(ba.getType()) && e.getComponent(CombatStatsComponent.class) != null;
    }

    private boolean isUpgradeableUnit(Entity e) {
        return e.getComponent(CombatStatsComponent.class) != null &&
                e.getComponent(FriendlyComponent.class) != null;
    }

    private boolean isResourceBase(Entity e) {
        BuildingActions ba = e.getComponent(BuildingActions.class);
        return ba != null && ba.getType() == Building.TOWNHALL && e.getComponent(BaseComponent.class) != null;
    }

    public void onWallUpgrade() {
        System.out.println("Wall upgrade event");
        if (!spendResources(0, -10, 0)) {
            System.out.println("Cannot afford wall upgrade event");
            return;
        }
        System.out.println("Can afford wall upgrade event");
        Array<Entity> entities = ServiceLocator.getEntityService().getEntities();
        for (Entity e : entities) {
            if (this.isUpgradeableWall(e)) {
                BuildingActions ba = e.getComponent(BuildingActions.class);
                ba.addLevel();
                System.out.println("Upgrade wall");
            }
            // if (this.isResourceBase(e)) {
            //     spendResources(e, 0, 0, -10);
            //     // BaseComponent bc = e.getComponent(BaseComponent.class);
            //     // bc.updateBaseStats(0, 0, -10);
            //     // bc.updateDisplay();
            // }
        }
        removeButton();
    }

    public void onUnitSpawn() {
        System.out.println("Unit spawn event");
        removeButton();
    }
}
