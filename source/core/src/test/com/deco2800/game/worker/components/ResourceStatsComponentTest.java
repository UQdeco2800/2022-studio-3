package com.deco2800.game.worker.components;

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
    void shouldSetGetWood() {
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);
        assertEquals(10, resourceStatsComponent.getWood());

        resourceStatsComponent.setWood(20);
        assertEquals(20, resourceStatsComponent.getWood());

        resourceStatsComponent.setWood(-5);
        assertEquals(0, resourceStatsComponent.getWood());

    }

    @Test
    void shouldSetGetStone() {
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);
        assertEquals(10, resourceStatsComponent.getStone());

        resourceStatsComponent.setStone(20);
        assertEquals(20, resourceStatsComponent.getStone());

        resourceStatsComponent.setStone(-5);
        assertEquals(0, resourceStatsComponent.getStone());

    }

    @Test
    void shouldSetGetMetal() {
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);
        assertEquals(10, resourceStatsComponent.getMetal());

        resourceStatsComponent.setMetal(20);
        assertEquals(20, resourceStatsComponent.getMetal());

        resourceStatsComponent.setMetal(-5);
        assertEquals(0, resourceStatsComponent.getMetal());

    }

    @Test
    void addWood() {
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);

        resourceStatsComponent.addWood(5);
        assertEquals(15, resourceStatsComponent.getWood());
    }

    @Test
    void addStone() {
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);

        resourceStatsComponent.addStone(5);
        assertEquals(15, resourceStatsComponent.getStone());
    }

    @Test
    void addMetal() {
        ResourceStatsComponent resourceStatsComponent = new ResourceStatsComponent(10,10,10);

        resourceStatsComponent.addMetal(5);
        assertEquals(15, resourceStatsComponent.getMetal());
    }

    @Test
    void collect() {
    }

    @Test
    void collectStone() {
    }

    @Test
    void collectWood() {
    }
}