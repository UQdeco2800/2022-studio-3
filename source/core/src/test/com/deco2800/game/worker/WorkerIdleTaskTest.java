package com.deco2800.game.worker;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.movement.WorkerIdleTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
class WorkerIdleTaskTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.registerMapService(new MapService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);
    }

    @Test
    void shouldNotMoveOnStart() {
        Vector2 startPosition = new Vector2(10.0f, 10.0f);
        WorkerIdleTask task = new WorkerIdleTask();
        Entity entity = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        entity.addComponent(movementComponent);
        entity.create();
        entity.setPosition(startPosition.x, startPosition.y);

        task.create(() -> entity);
        task.start();
        task.update();
        // Check to make sure that entity hasn't moved
        assertEquals(Task.Status.FINISHED, task.getMovementTask().getStatus());
        assertEquals(entity.getPosition(), startPosition);
    }

    @Test
    void shouldBeIdle() {
        WorkerIdleTask task = new WorkerIdleTask();
        Entity entity = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        entity.addComponent(movementComponent);
        entity.create();

        task.create(() -> entity);
        task.start();
        task.update(); // Should have stopped
        task.update(); // Should have updated idling to true
        assertTrue(task.isIdling());
    }

    @Test
    void shouldNotBeIdle() {
        Vector2 startPosition = new Vector2(10.0f, 10.0f);
        WorkerIdleTask task = new WorkerIdleTask();
        Entity entity = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        entity.addComponent(movementComponent);
        entity.create();
        entity.setPosition(startPosition);

        task.create(() -> entity);
        task.start();
        task.update(); // Should have stopped
        task.update(); // Should have updated idling to true
        assertTrue(task.isIdling()); // Should be idle

        task.startMoving(new Vector2(15.0f, 15.0f));
        assertFalse(task.isIdling()); // Should be moving
        task.update();
        assertFalse(task.isIdling()); // Should still be moving
    }
}
