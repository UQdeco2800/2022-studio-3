package com.deco2800.game.soldiers.movement;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.Task;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.movement.FightingMovementTask;
import com.deco2800.game.worker.components.movement.WorkerMovementTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class FightingMovementTaskTest {
    @BeforeEach
    void beforeEach() {
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerMapService(new MapService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
    }

    @Test
    void shouldMoveOnStart() {
        Vector2 target = new Vector2(10f, 10f);
        Entity owner = new Entity().addComponent(new PhysicsComponent());
        PhysicsMovementComponent movementComponent = new PhysicsMovementComponent();
        owner.addComponent(movementComponent);
        FightingMovementTask task = new FightingMovementTask(target, owner);

        Entity entity = new Entity().addComponent(new PhysicsComponent());
        entity.addComponent(movementComponent);
        entity.create();

        task.create(() -> entity);
        task.start();
        assertTrue(movementComponent.getMoving());
        assertEquals(target, movementComponent.getTarget());
        assertEquals(Task.Status.ACTIVE, task.getStatus());
    }
}
