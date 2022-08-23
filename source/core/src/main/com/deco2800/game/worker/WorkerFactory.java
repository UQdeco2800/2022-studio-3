package com.deco2800.game.worker;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.ForagerComponent;
import com.deco2800.game.worker.components.MinerComponent;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;
import com.deco2800.game.worker.components.movement.WorkerIdleTask;

/**
 * Factory to create a worker entity
 */
public class WorkerFactory {
    private static final WorkerConfig stats =
            FileLoader.readClass(WorkerConfig.class, "configs/worker.json");

    /**
     * Create a worker entity.
     * @return worker
     */
    public static Entity createWorker() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForWorker();
        AITaskComponent aiComponent = new AITaskComponent().addTask(new WorkerIdleTask());
        Entity worker =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/worker.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WORKER))
                        .addComponent(new WorkerInventoryComponent(stats.wood, stats.stone, stats.iron))
                        .addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE))
                        .addComponent(aiComponent)
                        .addComponent(inputComponent);

        PhysicsUtils.setScaledCollider(worker, 0.6f, 0.3f);
        worker.getComponent(ColliderComponent.class).setDensity(1.5f);
        worker.getComponent(TextureRenderComponent.class).scaleEntity();
        return worker;
    }

    /**
     * Create a Miner entity
     * @return miner
     */
    public static Entity createMiner(){
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForWorker();
        AITaskComponent aiComponent = new AITaskComponent().addTask(new WorkerIdleTask());
        Entity worker =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/worker.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new MinerComponent())
                        .addComponent(new CollectStatsComponent(2))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WORKER))
                        .addComponent(new WorkerInventoryComponent(stats.wood, stats.stone, stats.iron))
                        .addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE))
                        .addComponent(aiComponent)
                        .addComponent(inputComponent);

        PhysicsUtils.setScaledCollider(worker, 0.6f, 0.3f);
        worker.getComponent(ColliderComponent.class).setDensity(1.5f);
        worker.getComponent(TextureRenderComponent.class).scaleEntity();
        return worker;
    }

    /**
     * Create a Forager entity
     * @return forager
     */
    public static Entity createForager(){
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForWorker();
        AITaskComponent aiComponent = new AITaskComponent().addTask(new WorkerIdleTask());
        Entity worker =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/worker.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new ForagerComponent())
                        .addComponent(new CollectStatsComponent(4))
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WORKER))
                        .addComponent(new WorkerInventoryComponent(stats.wood, stats.stone, stats.iron))
                        .addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE))
                        .addComponent(aiComponent)
                        .addComponent(inputComponent);

        PhysicsUtils.setScaledCollider(worker, 0.6f, 0.3f);
        worker.getComponent(ColliderComponent.class).setDensity(1.5f);
        worker.getComponent(TextureRenderComponent.class).scaleEntity();
        return worker;
    }

    private WorkerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
