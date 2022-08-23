package com.deco2800.game.worker.type;

import com.deco2800.game.worker.WorkerConfig;
import com.deco2800.game.worker.WorkerFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.type.MinerComponent;

public class MinerFactory {
    
    private static final WorkerConfig stats = FileLoader.readClass(WorkerConfig.class, "configs/worker.json");

    public static Entity createMiner() {
        return WorkerFactory.createWorker()
        .addComponent(new MinerComponent())
        .addComponent(new CollectStatsComponent(2));
    }
    
}
