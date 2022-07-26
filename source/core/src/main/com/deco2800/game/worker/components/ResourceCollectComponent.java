package com.deco2800.game.worker.components;

import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.duration.DurationBarComponent;
import com.deco2800.game.worker.components.duration.DurationBarFactory;
import com.deco2800.game.worker.components.duration.DurationBarUiComponent;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.worker.components.type.StoneComponent;
import com.deco2800.game.worker.components.type.TreeComponent;
import com.deco2800.game.worker.resources.TreeFactory;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.services.GameTime;




import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




public class ResourceCollectComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ResourceCollectComponent.class);
    private short targetLayer;
    public static final long COLLECTION_TIME = 2500;
    private Fixture other;
    private Fixture me;
    private boolean colliding;
    private CollectStatsComponent collectStats;
    private HitboxComponent hitboxComponent;
    private long lastTimeMined;
    private GameTime gameTime;
    private long at_time;
    private static final long TIME_INTERVAL = 3000;
    private int initialWoodValue;
    private int initialMetalValue;
    private int initialStoneValue;

    /**
     * Create a component which collects resources from entity on collision.
     * @param targetLayer The physics layer of the target's collider.
     */
    public ResourceCollectComponent(short targetLayer) {
        this.targetLayer = targetLayer;
        this.gameTime = ServiceLocator.getTimeSource();
        this.lastTimeMined = 0;
        this.colliding = false;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        collectStats = entity.getComponent(CollectStatsComponent.class);
        hitboxComponent = entity.getComponent(HitboxComponent.class);
    }

    @Override
    public void update() {
        if (this.colliding) {
            this.onCollisionStart(this.me, this.other);
        }
    }

    /**
     * Called when the entity starts colliding with another entity.
     * Collects the resource if the target is a resource.
     * @param me The entity's fixture
     * @param other The collided entity's fixture
     */
    private void onCollisionStart(Fixture me, Fixture other) {
        if (this.lastTimeMined != 0 && this.gameTime.getTimeSince(this.lastTimeMined) < COLLECTION_TIME) {
            return;
        }
        if (hitboxComponent.getFixture() != me) {
            // Not triggered by hitbox, ignore
            return;
        }
        if (!PhysicsLayer.contains(targetLayer, other.getFilterData().categoryBits)) {
            // Doesn't match our target layer, ignore
            return;
        }
        if (me == null || (BodyUserData) other.getBody().getUserData() == null) {
            return;
        }
        // Try to collect resources from target.
        Entity target = ((BodyUserData) other.getBody().getUserData()).entity;
        ResourceStatsComponent targetStats = target.getComponent(ResourceStatsComponent.class);
        if (targetStats == null) {
            return;
        }
        BaseComponent isBase = target.getComponent(BaseComponent.class);
        TreeComponent isTree = target.getComponent(TreeComponent.class);
        StoneComponent isStone = target.getComponent(StoneComponent.class);
        if (isBase != null) {
            loadToBase(targetStats);
            return;
        }
        Entity collector = ((BodyUserData) me.getBody().getUserData()).entity;
        WorkerInventoryComponent collectorInventory = collector.getComponent(WorkerInventoryComponent.class);
        MinerComponent collectorIsMiner = collector.getComponent(MinerComponent.class);
        ForagerComponent collectorIsForager = collector.getComponent(ForagerComponent.class);

        // Create the duration bar animation
        // if(durationBar == null){
        //     durationBar = DurationBarFactory.createDurationBar();
        //     durationBar.addComponent(new DurationBarComponent());
        //     durationBar.setPosition(target.getPosition().x, target.getPosition().y + 1);
        //     ServiceLocator.getEntityService().register(durationBar);
        // }

        // Check the initial resource value
        // if(this.initialWoodValue == 0){
        //     this.initialWoodValue = targetStats.getWood();
        // }

        if (collectorIsMiner != null && isStone != null) {
            // If the worker type is Miner
            collectStone(targetStats);
            collectMetal(targetStats);
            if (target.getCenterPosition().x < collector.getCenterPosition().x) {
                collector.getEvents().trigger("workerForwardLeftActionAnimate");
            } else {
                collector.getEvents().trigger("workerForwardRightActionAnimate");
            }
        } else if (collectorIsForager != null && isTree != null) {
            // If the worker type is Forager
            collectWood(targetStats);
            if (target.getCenterPosition().x < collector.getCenterPosition().x) {
                collector.getEvents().trigger("workerForwardLeftActionAnimate");
            } else {
                collector.getEvents().trigger("workerForwardRightActionAnimate");
            }
            // this.triggerDurationBarAnimation(this.initialWoodValue, collectorInventory.getWood());
        } else {
            return;
        }

        startCollecting(me, other);
        if (targetStats.isDead()) {
            if(isTree!=null) {
                target.getComponent(AnimationRenderComponent.class).startAnimation("tree_damaged");}
            stopCollecting();
            collector.getEvents().trigger("workerIdleAnimate");
            ServiceLocator.getEntityService().unregister(target);
            if(isStone!=null) {
                target.dispose();}

            // durationBar.dispose();
            // ServiceLocator.getEntityService().unregister(durationBar);

            returnToBase(collector);
        }        
    }

    /**
     * Called when collector starts collecting resources.
     * @param me The entity's fixture
     * @param other The collided entity's fixture
     */
    public void startCollecting(Fixture me, Fixture other) {
        if (this.lastTimeMined == 0) {
            this.lastTimeMined = this.gameTime.getTime();
            this.colliding = true;
            this.other = other;
            this.me = me;   
        }
    }

    /**
     * Called when collector stops collecting resources.
     */
    public void stopCollecting() {
        this.colliding = false;
        this.me = null;
        this.other = null;
        this.lastTimeMined = 0;
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
     * Directs the worker to the base after resource collection and update the base resource stats
     */
    public void returnToBase(Entity collector) {
        WorkerInventoryComponent collectorInventory = collector.getComponent(WorkerInventoryComponent.class);
        Entity base = this.getBase();
        if (base != null) {
            entity.getEvents().trigger("workerWalk", this.getBase().getCenterPosition());
            BaseComponent baseComponent = base.getComponent(BaseComponent.class);
            baseComponent.updateBaseStats(collectorInventory.getWood(), collectorInventory.getMetal(), collectorInventory.getStone());
            baseComponent.updateDisplay();
        }   
    }
        

    /**
     * Adds the collected stone to the worker inventory.
     * @param targetStats The resource stats of the target.
     */
    public void collectStone(ResourceStatsComponent targetStats) {
        int numCollected = targetStats.collectStone(collectStats);
        // Add the number of collected resource to the worker inventory
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        inventory.addStone(numCollected);
        logger.info("[+] The worker has " + Integer.toString(inventory.getStone()) + " stones");
    }

    /**
     * Adds the collected metal to the worker inventory.
     * @param targetStats The resource stats of the target.
     */
    public void collectMetal(ResourceStatsComponent targetStats){
        int numCollected = targetStats.collectStone(collectStats);
        // Add the number of collected resource to the worker inventory
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        inventory.addMetal(numCollected);
        logger.info("[+] The worker has " + Integer.toString(inventory.getStone()) + " metals");
    }

    /**
     * Adds the collected wood to the worker inventory.
     * @param targetStats The resource stats of the target.
     */
    public void collectWood(ResourceStatsComponent targetStats){
        int numCollected = targetStats.collectWood(collectStats);
        // Add the number of collected resource to the worker inventory
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        inventory.addWood(numCollected);
        logger.info("[+] The worker has " + Integer.toString(inventory.getWood()) + " woods");
    }

    /**
     * Loads the collected resources to the base.
     * @param baseStats The resource stats of the base.
     */
    public void loadToBase(ResourceStatsComponent baseStats) {
        WorkerInventoryComponent inventory = entity.getComponent(WorkerInventoryComponent.class);
        baseStats.addMetal(inventory.unloadMetal());
        baseStats.addStone(inventory.unloadStone());
        baseStats.addWood(inventory.unloadWood());
        logger.info("[+] The worker now has " + Integer.toString(inventory.getWood()) + " wood and " + Integer.toString(inventory.getStone()) + " stone");
        logger.info("[+] The base now has " + Integer.toString(baseStats.getWood()) + " wood and " + Integer.toString(baseStats.getStone()) + " stone");
    }

    // private void triggerDurationBarAnimation(int initialValue, int currentValue){
    //     if(currentValue == 0){
    //         durationBar.getEvents().trigger("duration-bar-25");
    //     }else if(0 <= ((float) currentValue / initialValue) && ((float) currentValue / initialValue) <= 0.25f){
    //         durationBar.getEvents().trigger("duration-bar-25");
    //     }else if(0.25f <= ((float) currentValue / initialValue) && ((float) currentValue / initialValue) <= 0.5f){
    //         durationBar.getEvents().trigger("duration-bar-50");
    //     }else if(0.50f <= ((float) currentValue / initialValue) && ((float) currentValue / initialValue) <= 0.75f){
    //         durationBar.getEvents().trigger("duration-bar-75");
    //     }else{
    //         durationBar.getEvents().trigger("duration-bar-100");
    //     }
    // }
}
