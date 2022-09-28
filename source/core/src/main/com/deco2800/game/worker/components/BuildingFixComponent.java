package com.deco2800.game.worker.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.components.type.BuilderComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingFixComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(BuildingFixComponent.class);
    private short targetLayer;
    private HitboxComponent hitboxComponent;
    public static final int FIX_AMOUNT = 10;
    public static final int WOOD_REQUIRED = 5;
    public static final int STONE_REQUIRED = 5;
    public static final int METAL_REQUIRED = 5;

    /**
     * Create a component which collects resources from entity on collision.
     * @param targetLayer The physics layer of the target's collider.
     */
    public BuildingFixComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }


    /**
     * Fix building when the entity starts colliding with another entity.
     * Fixes building if the target is a building.
     * @param me The entity's fixture
     * @param other The collided entity's fixture
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }
      
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        // target components
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        BuildingActions buildingActions = target.getComponent(BuildingActions.class);
        CombatStatsComponent combatStatsComponent = target.getComponent(CombatStatsComponent.class);
        ResourceStatsComponent resourceStatsComponent = target.getComponent(ResourceStatsComponent.class);
        BaseComponent baseComponent = target.getComponent(BaseComponent.class);

        // worker components
        Entity collector = ((BodyUserData) me.getBody().getUserData()).entity;
        BuilderComponent builderComponent = collector.getComponent(BuilderComponent.class);
        WorkerInventoryComponent inventoryComponent = collector.getComponent(WorkerInventoryComponent.class);

        // fix the building if the worker is a builder and the target is a building
        if (buildingActions == null || combatStatsComponent == null || inventoryComponent == null || builderComponent == null) {
            return;
        }

        if (baseComponent == null) {
            // fix the building if the builder has the required resources
            if (!combatStatsComponent.isMaxHealth() && (inventoryComponent.getStone() >= STONE_REQUIRED 
            && inventoryComponent.getWood() >= WOOD_REQUIRED && inventoryComponent.getMetal() >= METAL_REQUIRED)) {
                inventoryComponent.addWood(-WOOD_REQUIRED);
                inventoryComponent.addStone(-STONE_REQUIRED);
                inventoryComponent.addMetal(-METAL_REQUIRED);
                combatStatsComponent.addHealth(FIX_AMOUNT);
                if (combatStatsComponent.getMaxHealth() < combatStatsComponent.getHealth()) {
                    combatStatsComponent.setHealth(combatStatsComponent.getMaxHealth());
                }
                target.getEvents().trigger("levelUp");
                logger.info("Building fixed");
            }
            // does not have the required resources to fix another building so return to base
            returnToBase();       
        } else {
            // collided with a base and should load resource for next fix
            getResourcesFromBase(resourceStatsComponent);    
        }
    }
        
    /**
     * Gets the base entity.
     * @return The base entity.
     */
    public Entity getBase() {
        Entity base = null;
        for (Entity entity : ServiceLocator.getEntityService().getEntities()) {
            if (entity.getComponent(BaseComponent.class) != null) {
                base = entity;
            }
        }
        return base;
    }

    /**
     * Directs the worker to the base after resource collection
     */
    public void returnToBase() {
        Entity base = this.getBase();
        if (base != null) {
            entity.getEvents().trigger("workerWalk", this.getBase().getCenterPosition());
        }   
    }
        
    /**
     * Loads the collected resources to the base.
     * @param baseStats The resource stats of the base.
     */
    public void getResourcesFromBase(ResourceStatsComponent baseStats) {
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        if (baseStats.hasWood(WOOD_REQUIRED) && baseStats.hasStone(STONE_REQUIRED) && baseStats.hasMetal(METAL_REQUIRED)) {
            baseStats.addWood(-WOOD_REQUIRED);
            baseStats.addStone(-STONE_REQUIRED);
            baseStats.addMetal(-METAL_REQUIRED);
            inventory.addWood(WOOD_REQUIRED);
            inventory.addStone(STONE_REQUIRED);
            inventory.addMetal(METAL_REQUIRED);
        }
        logger.info("[+] The builder now has " + Integer.toString(inventory.getWood()) + " wood and " + Integer.toString(inventory.getStone()) + " stone");
        logger.info("[+] The base now has " + Integer.toString(baseStats.getWood()) + " wood and " + Integer.toString(baseStats.getStone()) + " stone");
    }
}
