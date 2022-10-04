package com.deco2800.game.worker;

import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.worker.type.ForagerFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
public class ForagerFactoryTest {

    private static Entity forager1;

    @Test
    static void shouldDetectForagerComponents() {
        forager1 = ForagerFactory.createForager();
        ForagerComponent foragerComponent = forager1.getComponent(ForagerComponent.class);
        MinerComponent minerComponent = forager1.getComponent(MinerComponent.class);
        assertNotNull(foragerComponent);
        assertNull(minerComponent);
        assertEquals(1, foragerComponent.getIsForager());
    }

    @Test
    static void shouldDetectCollectStatsComponent(){
        forager1 = ForagerFactory.createForager();
        CollectStatsComponent forager_collect_stats = forager1.getComponent(CollectStatsComponent.class);
        assertEquals(2, forager_collect_stats.getCollectionAmount());
    }

    @Test
    static void shouldCollectAndUnloadResources(){
        forager1 = ForagerFactory.createForager();
        ResourceCollectComponent resourceCollectComponent = forager1.getComponent(ResourceCollectComponent.class);
        resourceCollectComponent.create();
        Entity tree = new Entity();
        ResourceStatsComponent treeStats = new ResourceStatsComponent(4, 0, 0);
        tree.addComponent(treeStats);
        WorkerInventoryComponent inventory = forager1.getComponent(WorkerInventoryComponent.class);

        assertEquals(0, inventory.getWood());
        resourceCollectComponent.collectWood(treeStats);
        assertEquals(2, inventory.getWood());
        resourceCollectComponent.collectWood(treeStats);
        assertEquals(4, inventory.getWood());

        Entity base = new Entity();
        base.addComponent(new ResourceStatsComponent(0, 0, 0));
        resourceCollectComponent.loadToBase(base.getComponent(ResourceStatsComponent.class));
        assertEquals(0, inventory.getWood());
    }
    
}
