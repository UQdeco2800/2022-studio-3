package com.deco2800.game.worker.components;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceCollectComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ResourceCollectComponent.class);
    private short targetLayer;
    public static final long COLLECTION_TIME = 2500;
    private CollectStatsComponent collectStats;
    private HitboxComponent hitboxComponent;
    private final GameTime gameTime;
    private long lastTimeMined;

    /**
     * Create a component which collects resources from entity on collision.
     * @param targetLayer The physics layer of the target's collider.
     */
    public ResourceCollectComponent(short targetLayer) {
        this.targetLayer = targetLayer;
        this.gameTime = ServiceLocator.getTimeSource();
        this.lastTimeMined = 0;
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

        logger.info("Collided " + Long.toString(this.gameTime.getTime()));
        // Try to collect resources from target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        ResourceStatsComponent targetStats = target.getComponent(ResourceStatsComponent.class);
        if (targetStats == null) {
            logger.info("Resource Stats not found");
            return;
        }
        BaseComponent isBase = target.getComponent(BaseComponent.class);
        if (isBase != null) {
            loadToBase(targetStats);
            logger.info("Loading to Base");
            return;
        }

        Entity collector = ((BodyUserData) me.getBody().getUserData()).entity;
        MinerComponent collectorIsMiner = collector.getComponent(MinerComponent.class);
        ForagerComponent collectorIsForager = collector.getComponent(ForagerComponent.class);

        if (this.lastTimeMined == 0 || this.gameTime.getTimeSince(this.lastTimeMined) >= COLLECTION_TIME) {
            // not enough time has elapsed for collector to collect resource
            if (collectorIsMiner != null) {
                // If the worker type is Miner
                collectStone(targetStats);
            } else if (collectorIsForager != null){
                // If the worker type is Forager
                collectWood(targetStats);
            }
            this.lastTimeMined = this.gameTime.getTime();                    
        }
    }

    private void collectStone(ResourceStatsComponent targetStats) {
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
        logger.info("[+] The worker now has " + Integer.toString(inventory.getWood()) + " wood and " + Integer.toString(inventory.getStone()) + " stone");
        logger.info("[+] The base now has " + Integer.toString(baseStats.getWood()) + " wood and " + Integer.toString(baseStats.getStone()) + " stone");
    }
}
