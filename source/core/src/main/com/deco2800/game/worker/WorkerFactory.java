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
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;

/**
 * Factory to create a worker entity
 */
public class WorkerFactory {
    private static final WorkerConfig stats =
            FileLoader.readClass(WorkerConfig.class, "worker.json");

    /**
     * Create a worker entity.
     * @return worker
     */
    public static Entity createWorker() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForWorker();
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WorkerIdleTask(2f));
        Entity worker =
                new Entity()
                        .addComponent(new TextureRenderComponent("worker.png"))
                        // Sets worker sprite to worker.png
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WORKER))
                        // Adds worker hitbox
                        .addComponent(new WorkerActions())
                        // Controls player actions (e.g., movement, attack, etc.)
                        .addComponent(new WorkerInventoryComponent(stats.wood, stats.stone, stats.iron))
                        // Adds an inventory to the worker to manage wood, stone and iron
                        .addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE))
                        // Collects resources from resource node on collision (see TouchAttackComponent)
                        .addComponent(inputComponent)
                        .addComponent(aiComponent);
                        // Controls NPC actions (e.g., wandering, chasing, etc.)

        PhysicsUtils.setScaledCollider(worker, 0.6f, 0.3f);
        worker.getComponent(ColliderComponent.class).setDensity(1.5f);
        worker.getComponent(TextureRenderComponent.class).scaleEntity();
        return worker;
    }

    private WorkerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
