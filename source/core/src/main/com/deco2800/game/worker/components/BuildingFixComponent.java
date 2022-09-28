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
    public static final long FIX_TIME = 2500;
    private HitboxComponent hitboxComponent;
    public static final int FIX_AMOUNT = 10;

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
     * Called when the entity starts colliding with another entity.
     * Collects the resource if the target is a resource.
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
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        BuildingActions buildingActions = target.getComponent(BuildingActions.class);
        CombatStatsComponent combatStatsComponent = target.getComponent(CombatStatsComponent.class);
        BaseComponent baseComponent = target.getComponent(BaseComponent.class);
        // Does not fix the worker base
        if (baseComponent != null || buildingActions == null || combatStatsComponent == null) {
            return;
        }
        Entity collector = ((BodyUserData) me.getBody().getUserData()).entity;
        BuilderComponent builderComponent = collector.getComponent(BuilderComponent.class);
        WorkerInventoryComponent inventoryComponent = collector.getComponent(WorkerInventoryComponent.class);
        // fix the building if the worker is a builder and the target is a building
        if (builderComponent != null && buildingActions != null) {
            // fix the building if the builder has the required resources
            if (!combatStatsComponent.isMaxHealth() && (inventoryComponent.getStone() >= 5 && inventoryComponent.getWood() >= 5 && inventoryComponent.getMetal() >= 5)) {
                inventoryComponent.addWood(-5);
                inventoryComponent.addStone(-5);
                inventoryComponent.addMetal(-5);
                combatStatsComponent.addHealth(FIX_AMOUNT);
                if (combatStatsComponent.getMaxHealth() < combatStatsComponent.getHealth()) {
                    combatStatsComponent.setHealth(combatStatsComponent.getMaxHealth());
                }
                target.getEvents().trigger("levelUp");
                logger.info("Building fixed");
            }  
            returnToBase();       
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
        baseStats.addMetal(inventory.unloadMetal());
        baseStats.addStone(inventory.unloadStone());
        baseStats.addWood(inventory.unloadWood());
        logger.info("[+] The worker now has " + Integer.toString(inventory.getWood()) + " wood and " + Integer.toString(inventory.getStone()) + " stone");
        logger.info("[+] The base now has " + Integer.toString(baseStats.getWood()) + " wood and " + Integer.toString(baseStats.getStone()) + " stone");
    }
}
