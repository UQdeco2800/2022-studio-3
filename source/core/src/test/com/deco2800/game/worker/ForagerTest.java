package com.deco2800.game.worker;

import com.deco2800.game.components.worker.CollectStatsComponent;
import com.deco2800.game.components.worker.ResourceCollectComponent;
import com.deco2800.game.components.worker.ResourceStatsComponent;
import com.deco2800.game.components.worker.WorkerInventoryComponent;
import com.deco2800.game.components.worker.type.ForagerComponent;
import com.deco2800.game.components.worker.type.MinerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.components.HitboxComponent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
public class ForagerTest {

    @Test
    void shouldDetectForagerComponents() {
        Entity forager1 = new Entity();
        forager1.addComponent(new ForagerComponent());
        ForagerComponent foragerComponent = forager1.getComponent(ForagerComponent.class);
        MinerComponent minerComponent = forager1.getComponent(MinerComponent.class);
        assertNotNull(foragerComponent);
        assertNull(minerComponent);
        assertEquals(1, foragerComponent.getIsForager());
    }

    @Test
    void shouldDetectCollectStatsComponent(){
        Entity forager1 = new Entity();
        forager1.addComponent(new ForagerComponent());
        forager1.addComponent(new CollectStatsComponent(2));
        CollectStatsComponent forager_collect_stats = forager1.getComponent(CollectStatsComponent.class);
        assertEquals(2, forager_collect_stats.getCollectionAmount());
    }

    @Test
    void shouldCollectAndUnloadResources(){

        Entity forager1 = new Entity();
        forager1.addComponent(new ForagerComponent());
        forager1.addComponent(new CollectStatsComponent(2));
        forager1.addComponent(new ResourceCollectComponent((short) (1 << 2)));
        forager1.addComponent(new WorkerInventoryComponent());
        HitboxComponent hitbox = new HitboxComponent();
        forager1.addComponent(hitbox);
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
