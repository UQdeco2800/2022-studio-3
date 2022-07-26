package com.deco2800.game.worker;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.EntityType;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;
import com.deco2800.game.worker.components.movement.WorkerIdleTask;
import com.deco2800.game.services.ServiceLocator;

/** Factory to create a worker entity */
public class WorkerFactory {
    private static final WorkerConfig stats =
            FileLoader.readClass(WorkerConfig.class, "configs/worker.json");

    /**
     * Create a worker entity.
     * @return worker
     */
    public static Entity createWorker() {
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForFriendlyUnit();
        AITaskComponent aiComponent = new AITaskComponent().addTask(new WorkerIdleTask());
        Entity worker =
                new Entity()
                        .addComponent(new PhysicsComponent())
                        .addComponent(new PhysicsMovementComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.WORKER))
                        .addComponent(new WorkerInventoryComponent(stats.wood, stats.stone, stats.metal))
                        .addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE))
                        .addComponent(new SelectableComponent())
                        .addComponent(new CombatStatsComponent(stats.health, 0, 0))
                        .addComponent(new HealthBarComponent(EntityType.FRIENDLY))

                        .addComponent(aiComponent)
                        .addComponent(inputComponent)
                        .addComponent(new FriendlyComponent());

        PhysicsUtils.setScaledCollider(worker, 0.6f, 0.3f);
        worker.getComponent(ColliderComponent.class).setDensity(1.5f);
        return worker;
    }

    private WorkerFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
