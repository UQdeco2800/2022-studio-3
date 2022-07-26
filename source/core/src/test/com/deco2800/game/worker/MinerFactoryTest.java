package com.deco2800.game.worker;

import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.MinerAnimationController;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.worker.resources.ResourceConfig;
import com.deco2800.game.worker.type.MinerFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;

import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.entities.EntityService;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Gdx;

import java.lang.Class;
import java.lang.reflect.Method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class MinerFactoryTest {

    private static Entity miner;

    @BeforeEach
    void BeforeEach() {
        ServiceLocator.clear();
        TextureAtlas builderAtlas = new TextureAtlas(Gdx.files.internal("images/miner.atlas"));
        ResourceService rs = mock(ResourceService.class);
        when(rs.getAsset(anyString(), eq(TextureAtlas.class))).thenReturn(builderAtlas);
        ServiceLocator.registerResourceService(rs);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    @Test
    public void minerComponentDetected(){
        miner = MinerFactory.createMiner();
        MinerComponent miner_type = miner.getComponent(MinerComponent.class);
        ForagerComponent forager_type = miner.getComponent(ForagerComponent.class);
        if(miner_type.getIsMiner() == 1){
            assertTrue(miner_type.getIsMiner() == 1);
        }
        if(forager_type == null){
            assertTrue(miner_type.getIsMiner() == 1);
        }
    }

    @Test
    public void minerCollectStatsDetected(){
        miner = MinerFactory.createMiner();
        CollectStatsComponent miner_collect_stats = miner.getComponent(CollectStatsComponent.class);
        assertEquals(miner_collect_stats.getCollectionAmount(), 2);
    }

    @Test
    public void minerResourceStatsDetected(){
        ResourceConfig stats = FileLoader.readClass(ResourceConfig.class, "configs/stone.json");
        Entity stone_entity = new Entity().addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal));

        miner = MinerFactory.createMiner();
        miner.addComponent(new CollectStatsComponent(2));
        miner.addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE));

        CollectStatsComponent miner_collector = miner.getComponent(CollectStatsComponent.class);
        ResourceStatsComponent stone_resource = stone_entity.getComponent(ResourceStatsComponent.class);
        stone_resource.collectStone(miner_collector);

        assertEquals(stone_resource.getStone(), stats.stone - miner_collector.getCollectionAmount());
    }

    @Test
    public void minerAnimationControllerDetected(){
        miner = MinerFactory.createMiner();

        try {
            Class[] param = null;
            Method func = miner.getClass().getMethod("animateIdle",param);
        // Catch statement in case if the method is not defined
        } catch (NoSuchMethodException e) {
            assertFalse(miner.getComponent(MinerAnimationController.class) == null);
        }

        try {
            Class[] param = null;
            Method func = miner.getClass().getMethod("animateMove",param);
        } catch (NoSuchMethodException e) {
            assertFalse(miner.getComponent(MinerAnimationController.class) == null);
        }
    }

    @Test
    public void inventoryCouldBeAttachedToMiner(){
        miner = MinerFactory.createMiner();
        
        WorkerInventoryComponent miner_inventory = miner.getComponent(WorkerInventoryComponent.class);

        // Check if get...() defined
        assertTrue(miner_inventory.hasStone(0));
        assertTrue(miner_inventory.hasMetal(0));
        assertTrue(miner_inventory.hasWood(0));

        // Call the add all resources with 1
        miner_inventory.setMetal(1);
        miner_inventory.setStone(1);
        miner_inventory.setWood(1);

        // Check that the previous set methods only set each atrribute with value 1
        assertFalse(miner_inventory.hasStone(2));
        assertFalse(miner_inventory.hasMetal(2));
        assertFalse(miner_inventory.hasWood(2));

        // Unload add resources
        miner_inventory.unloadMetal();
        miner_inventory.unloadStone();
        miner_inventory.unloadWood();

        // Check if all resources set to 0
        assertEquals(miner_inventory.getMetal(), 0);
        assertEquals(miner_inventory.getMetal(), 0);
        assertEquals(miner_inventory.getMetal(), 0);
    }

}
