package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.DebugRenderer;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@ExtendWith(GameExtension.class)
class rangedAttackTest {
    @BeforeEach
    void beforeEach() {
        // Mock rendering, physics, game time
        RenderService renderService = new RenderService();
        renderService.setDebug(mock(DebugRenderer.class));
        ServiceLocator.registerRenderService(renderService);
        GameTime gameTime = new GameTime();
        ServiceLocator.registerTimeSource(gameTime);
        ServiceLocator.registerPhysicsService(new PhysicsService());
    }


    /**
     * Testing whether the bullet is fired only once.
     */
    @Test
    void testShootBulletOnce() {
        ArrayList<Integer> bulletamount = new ArrayList<>();
        Entity target = new Entity();
        target.setPosition(0f, 0f);
        AITaskComponent ai = new AITaskComponent().addTask(new rangedAttackTask(target, 1f, 8, 8));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(2f, 2f);
        entity.getEvents().addListener("attack", () ->  bulletamount.add(1));
        long time = ServiceLocator.getTimeSource().getTime();
        while (ServiceLocator.getTimeSource().getTimeSince(time) < 1100) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        assertEquals(1, bulletamount.size());
    }

    /**
     * Testing whether the bullet is fired twice.
     */
    @Test
    void testShootBulletTwice() {
        ArrayList<Integer> bulletamount = new ArrayList<>();
        Entity target = new Entity();
        target.setPosition(0f, 0f);
        AITaskComponent ai = new AITaskComponent().addTask(new rangedAttackTask(target, 1f, 8, 8));
        Entity entity = makePhysicsEntity().addComponent(ai);
        entity.create();
        entity.setPosition(2f, 2f);
        entity.getEvents().addListener("attack", () ->  bulletamount.add(1));
        long time = ServiceLocator.getTimeSource().getTime();
        while (ServiceLocator.getTimeSource().getTimeSince(time) < 2200) {
            entity.earlyUpdate();
            entity.update();
            ServiceLocator.getPhysicsService().getPhysics().update();
        }
        assertEquals(2, bulletamount.size());
    }

    /**
     * Testing whether the bullet wil fire when the target is far away from the enemy.
     */
    @Test
    void testOnlyShootInDistance() {
        Entity target = new Entity();
        target.setPosition(0f, 100f);

        Entity entity = makePhysicsEntity();
        entity.create();
        entity.setPosition(0f, 0f);
        rangedAttackTask attackTask = new rangedAttackTask(target, 1f, 8, 8);
        attackTask.create(() -> entity);

        // Not currently active, target is too far, should have negative priority
        assertTrue(attackTask.getPriority() < 0);
    }

    private Entity makePhysicsEntity() {
        return new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
    }
}
