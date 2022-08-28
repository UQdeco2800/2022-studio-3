package com.deco2800.game.worker;

import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.worker.resources.ResourceConfig;
import com.deco2800.game.worker.type.MinerFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class Miner {

    @Test
    void minerComponentDetected(){
        Entity miner = new Entity();
        miner.addComponent(new MinerComponent());
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
    void minerCollectStatsDetected(){
        Entity miner = new Entity();
        miner.addComponent(new MinerComponent());
        miner.addComponent(new CollectStatsComponent(2));
        CollectStatsComponent miner_collect_stats = miner.getComponent(CollectStatsComponent.class);
        assertEquals(miner_collect_stats.getCollectionAmount(), 2);
    }

    @Test
    void minerResourceStatsDetected(){
        ResourceConfig stats = FileLoader.readClass(ResourceConfig.class, "configs/stone.json");
        Entity stone_entity = new Entity().addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal));

        Entity miner = new Entity();
        miner.addComponent(new CollectStatsComponent(2));
        miner.addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE));

        CollectStatsComponent miner_collector = miner.getComponent(CollectStatsComponent.class);
        ResourceStatsComponent stone_resource = stone_entity.getComponent(ResourceStatsComponent.class);
        stone_resource.collectStone(miner_collector);
    }
}
