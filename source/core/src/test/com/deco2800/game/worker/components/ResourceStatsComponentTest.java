package com.deco2800.game.worker.components;

import com.deco2800.game.components.worker.CollectStatsComponent;
import com.deco2800.game.components.worker.ResourceStatsComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ResourceStatsComponentTest {

    @Test
    void isDead() {
        ResourceStatsComponent resourceStatsComponentDead = new ResourceStatsComponent(0,0,0);
        assertEquals(true, resourceStatsComponentDead.isDead());

        ResourceStatsComponent resourceStatsComponentAlive = new ResourceStatsComponent(10,10,10);
        assertEquals(false, resourceStatsComponentAlive.isDead());
    }

    @Test
    void collectStone() {
        CollectStatsComponent collectStatsComponent = new CollectStatsComponent(10);
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);

        assertEquals(10, collectStatsComponent.getCollectionAmount());
        assertEquals(10, resourceStatsComponent.getStone());

        resourceStatsComponent.collectStone(collectStatsComponent);
        assertEquals(0, resourceStatsComponent.collectStone(collectStatsComponent));
    }

    @Test
    void collectWood() {
        CollectStatsComponent collectStatsComponent = new CollectStatsComponent(10);
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);

        assertEquals(10, collectStatsComponent.getCollectionAmount());
        assertEquals(10, resourceStatsComponent.getWood());

        resourceStatsComponent.collectWood(collectStatsComponent);
        assertEquals(0, resourceStatsComponent.collectWood(collectStatsComponent));
    }

    @Test
    void collectMetal() {
        CollectStatsComponent collectStatsComponent = new CollectStatsComponent(10);
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);

        assertEquals(10, collectStatsComponent.getCollectionAmount());
        assertEquals(10, resourceStatsComponent.getMetal());

        resourceStatsComponent.collectMetal(collectStatsComponent);
        assertEquals(0, resourceStatsComponent.collectMetal(collectStatsComponent));
    }
}