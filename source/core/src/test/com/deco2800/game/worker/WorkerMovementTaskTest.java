package com.deco2800.game.worker;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.movement.WorkerMovementTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class WorkerMovementTaskTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerPhysicsService(new PhysicsService());
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    void shouldMoveOnStart() {
        Vector2 target = new Vector2(10f, 10f);
        WorkerMovementTask task = new WorkerMovementTask(target);
        Entity entity = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        entity.addComponent(movementComponent);
        entity.create();

        task.create(() -> entity);
        task.start();
        assertTrue(movementComponent.getMoving());
        assertEquals(target, movementComponent.getTarget());
        assertEquals(Task.Status.ACTIVE, task.getStatus());
    }

    @Test
    void shouldStopWhenClose() {
        WorkerMovementTask task = new WorkerMovementTask(new Vector2(10f, 10f));
        task.setStopDistance(1f); // Stopping distance for entity
        Entity entity = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        entity.addComponent(movementComponent);
        entity.setPosition(5f, 5f);
        entity.create();

        task.create(() -> entity);
        task.start();
        task.update();
        assertTrue(movementComponent.getMoving());
        assertEquals(Task.Status.ACTIVE, task.getStatus());

        entity.setPosition(10f, 9f);
        task.update();
        assertFalse(movementComponent.getMoving());
        assertEquals(Task.Status.FINISHED, task.getStatus());
    }

    @Test
    void shouldMoveToNewTarget() {
        Vector2 firstTarget = new Vector2(10f, 10f); // First entity target
        WorkerMovementTask task = new WorkerMovementTask(firstTarget);
        task.setStopDistance(1f); // Stopping distance for entity
        Entity entity = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        entity.addComponent(movementComponent);
        entity.setPosition(5f, 5f);
        entity.create();

        // Create WorkerMovementTask and assign entity
        task.create(() -> entity);
        task.start();
        task.update();
        assertTrue(movementComponent.getMoving());
        assertEquals(Task.Status.ACTIVE, task.getStatus());

        // Change target of entity
        Vector2 secondTarget = new Vector2(-10f, -10f);
        task.setTarget(secondTarget);

        // Move entity next to first target; should still be moving
        entity.setPosition(10f, 9f);
        task.update();
        assertTrue(movementComponent.getMoving());
        assertEquals(Task.Status.ACTIVE, task.getStatus());

        // Move entity next to second target; should stop moving
        entity.setPosition(-10f, -9f);
        task.update();
        assertFalse(movementComponent.getMoving());
        assertEquals(Task.Status.FINISHED, task.getStatus());
    }
}