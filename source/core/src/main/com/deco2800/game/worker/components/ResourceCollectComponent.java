package com.deco2800.game.worker.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceCollectComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ResourceCollectComponent.class);
    private short targetLayer;
    private CollectStatsComponent collectStats;
    private HitboxComponent hitboxComponent;

    /**
     * Create a component which collects resources from entity on collision.
     * @param targetLayer The physics layer of the target's collider.
     */
    public ResourceCollectComponent(short targetLayer) {
        this.targetLayer = targetLayer;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        collectStats = entity.getComponent(CollectStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    private void onCollisionStart(Fixture me, Fixture other) {
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }

        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }

        // Try to collect resources from target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        ResourceStatsComponent targetStats = target.getComponent(ResourceStatsComponent.class);

        BaseComponent isBase = target.getComponent(BaseComponent.class);
        if (isBase != null) {
            loadToBase(targetStats);
        } else if (targetStats != null) {
            Entity collectorType = ((BodyUserData) me.getBody().getUserData()).entity;
            MinerComponent collectorIsMiner = collectorType.getComponent(MinerComponent.class);
            ForagerComponent collectorIsForager = collectorType.getComponent(ForagerComponent.class);
            if (collectorIsMiner != null){
                // If the worker type is Miner
                collectStone(targetStats);
            } else if(collectorIsForager != null){
                // If the worker type is Forager
                collectWood(targetStats);
            } else{
                targetStats.collect(collectStats);
            }
        }
    }

    private void collectStone(ResourceStatsComponent targetStats){
        int numCollected = targetStats.collectStone(collectStats);
        // Add the number of collected resource to the worker inventory
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        inventory.addStone(numCollected);
        logger.info("[+] The worker has " + Integer.toString(inventory.getStone()) + " stones");
    }

    private void collectWood(ResourceStatsComponent targetStats){
        int numCollected = targetStats.collectWood(collectStats);
        // Add the number of collected resource to the worker inventory
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        inventory.addWood(numCollected);
        logger.info("[+] The worker has " + Integer.toString(inventory.getWood()) + " woods");
    }

    private void loadToBase(ResourceStatsComponent baseStats) {
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        baseStats.addIron(inventory.unloadMetal());
        baseStats.addStone(inventory.unloadStone());
        baseStats.addWood(inventory.unloadWood());
        logger.info("[+] The worker has unloaded" + Integer.toString(baseStats.getStone()) + " stones");
        logger.info("[+] The worker has unloaded" + Integer.toString(baseStats.getIron()) + " metal");
        logger.info("[+] The worker has unloaded" + Integer.toString(baseStats.getWood()) + " wood");
        
    }
}
