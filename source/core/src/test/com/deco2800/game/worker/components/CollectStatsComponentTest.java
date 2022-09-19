package com.deco2800.game.worker.components;

import com.deco2800.game.components.worker.CollectStatsComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CollectStatsComponentTest {

    @Test
    void shouldSetGetCollectionAmount() {
        CollectStatsComponent collectStatsComponent = new CollectStatsComponent(5);
        assertEquals(5, collectStatsComponent.getCollectionAmount());

//        Test if set will replace the collectionAmount
        collectStatsComponent.setCollectionAmount(10);
        collectStatsComponent.setCollectionAmount(20);
        assertEquals(20, collectStatsComponent.getCollectionAmount());

        collectStatsComponent.setCollectionAmount(-5);
        assertEquals(0, collectStatsComponent.getCollectionAmount());
    }

    @Test
    void shouldAddCollectionAmount() {
        CollectStatsComponent collectStatsComponent = new CollectStatsComponent(5);

//        Test if add will sum the prev and added value
        collectStatsComponent.addCollectionAmount(20);
        assertEquals(25, collectStatsComponent.getCollectionAmount());
    }
}