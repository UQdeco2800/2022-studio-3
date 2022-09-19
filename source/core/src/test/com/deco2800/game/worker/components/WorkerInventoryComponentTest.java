package com.deco2800.game.worker.components;

import com.deco2800.game.components.worker.WorkerInventoryComponent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WorkerInventoryComponentTest {

    @Test
    void shouldSetGetStone() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setStone(5);
        assertEquals(5, workerInventoryComponent.getStone());

    }

    @Test
    void shouldSetGetMetal() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setMetal(5);
        assertEquals(5, workerInventoryComponent.getMetal());

    }

    @Test
    void shouldSetGetWood() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setWood(5);
        assertEquals(5, workerInventoryComponent.getWood());

    }

    @Test
    void shouldHasStone() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setStone(5);
        assertEquals(true, workerInventoryComponent.hasStone(5));

        workerInventoryComponent.setStone(5);
        assertEquals(false, workerInventoryComponent.hasStone(10));


    }

    @Test
    void shouldHasMetal() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setMetal(5);
        assertEquals(5, workerInventoryComponent.getMetal());
        assertEquals(true, workerInventoryComponent.hasMetal(5));

        workerInventoryComponent.setMetal(5);
        assertEquals(false, workerInventoryComponent.hasMetal(10));

    }

    @Test
    void shouldHasWood() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setWood(5);
        assertEquals(true, workerInventoryComponent.hasWood(5));

        workerInventoryComponent.setStone(5);
        assertEquals(false, workerInventoryComponent.hasWood(10));

    }

    @Test
    void addStone() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setStone(5);
        assertEquals(5, workerInventoryComponent.getStone());

        workerInventoryComponent.addStone(5);
        assertEquals(10, workerInventoryComponent.getStone());
    }

    @Test
    void addMetal() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setMetal(5);
        assertEquals(5, workerInventoryComponent.getMetal());

        workerInventoryComponent.addMetal(5);
        assertEquals(10, workerInventoryComponent.getMetal());

    }

    @Test
    void addWood() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setWood(5);
        assertEquals(5, workerInventoryComponent.getWood());

        workerInventoryComponent.addWood(5);
        assertEquals(10, workerInventoryComponent.getWood());

    }

    @Test
    void unloadStone() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setStone(5);
        assertEquals(5, workerInventoryComponent.getStone());

        workerInventoryComponent.unloadStone();
        assertEquals(0, workerInventoryComponent.getStone());

    }

    @Test
    void unloadMetal() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setMetal(5);
        assertEquals(5, workerInventoryComponent.getMetal());

        workerInventoryComponent.unloadMetal();
        assertEquals(0, workerInventoryComponent.getMetal());

    }

    @Test
    void unloadWood() {
        WorkerInventoryComponent workerInventoryComponent = new WorkerInventoryComponent();

        workerInventoryComponent.setWood(5);
        assertEquals(5, workerInventoryComponent.getWood());

        workerInventoryComponent.unloadWood();
        assertEquals(0, workerInventoryComponent.getWood());

    }
}