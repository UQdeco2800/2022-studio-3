package com.deco2800.game.worker;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.movement.WorkerIdleTask;
import com.deco2800.game.worker.components.movement.WorkerInputComponent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
class WorkerInputComponentTest {
    private final static int SCREEN_X = 100;
    private final static int SCREEN_Y_ONE = 100;
    private final static int SCREEN_Y_TWO = 200;
    private final static Vector3 SCREEN_POSITION_ONE = new Vector3(SCREEN_X, SCREEN_Y_ONE, 0);
    private final static Vector3 SCREEN_POSITION_TWO = new Vector3(SCREEN_X, SCREEN_Y_TWO, 0);
    private final static int WORLD_X = 10;
    private final static int WORLD_Y_ONE = 10;
    private final static int WORLD_Y_TWO = 20;
    private final static Vector3 WORLD_POSITION_ONE = new Vector3(WORLD_X, WORLD_Y_ONE, 0);
    private final static Vector3 WORLD_POSITION_TWO = new Vector3(WORLD_X, WORLD_Y_TWO, 0);
    @Mock OrthographicCamera camera;
    @BeforeEach
    void beforeEach() {
        // Register relevant services
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerEntityService(new EntityService());
        // Register mock camera
        ServiceLocator.getEntityService().register(
                new Entity().addComponent(new CameraComponent(camera))
        );
    }

    @Test
    void shouldStartUnselected() {
        Entity entity = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
        WorkerInputComponent inputComponent = entity.getComponent(WorkerInputComponent.class);
        assertFalse(inputComponent.isSelected());
    }

    @Test
    void shouldSelectEntity() {
        Entity entity = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
        WorkerInputComponent inputComponent = entity.getComponent(WorkerInputComponent.class);
        // Set up mock for unproject() function
        when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
        // Trigger touch down event with left-click at screen position one
        inputComponent.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
        assertTrue(inputComponent.isSelected());
    }

    @Test
    void shouldUnselectEntity() {
        Entity entity = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
        WorkerInputComponent inputComponent = entity.getComponent(WorkerInputComponent.class);
        // Set up mock for unproject() function
        when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
        // Trigger touch down event with left-click at screen position one
        inputComponent.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
        assertTrue(inputComponent.isSelected());
        // Trigger key down event with ESC
        inputComponent.keyDown(Input.Keys.ESCAPE);
        assertFalse(inputComponent.isSelected());
    }

    // @Test
    // void shouldMoveSelectedEntity() {
    //     Entity entity = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
    //     WorkerInputComponent inputComponent = entity.getComponent(WorkerInputComponent.class);
    //     // Set up mock for unproject() function
    //     when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
    //     when(camera.unproject(SCREEN_POSITION_TWO)).thenReturn(WORLD_POSITION_TWO);
    //     // Trigger touch down event with left-click at screen position one
    //     inputComponent.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
    //     assertTrue(inputComponent.isSelected());
    //     createTimeSource();
    //     // Add worker idle task and update
    //     WorkerIdleTask workerIdleTask = addIdleTaskToEntity(entity);
    //     workerIdleTask.start();
    //     workerIdleTask.update();
    //     WorkerMovementTask movementTask = workerIdleTask.getMovementTask();
    //     assertFalse(movementTask.isMoving());
    //     // Trigger touch down event with right-click at screen position two
    //     inputComponent.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.RIGHT);
    //     assertTrue(movementTask.isMoving());
    // }

    // @Test
    // void shouldNotMoveUnselectedEntity() {
    //     Entity entity = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
    //     WorkerInputComponent inputComponent = entity.getComponent(WorkerInputComponent.class);
    //     // Set up mock for unproject() function
    //     when(camera.unproject(SCREEN_POSITION_TWO)).thenReturn(WORLD_POSITION_TWO);
    //     assertFalse(inputComponent.isSelected());
    //     createTimeSource();
    //     // Add worker idle task and update
    //     WorkerIdleTask workerIdleTask = addIdleTaskToEntity(entity);
    //     workerIdleTask.start();
    //     workerIdleTask.update();
    //     WorkerMovementTask movementTask = workerIdleTask.getMovementTask();
    //     assertFalse(movementTask.isMoving());
    //     // Trigger touch down event with right-click at screen position two
    //     inputComponent.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.RIGHT);
    //     assertFalse(movementTask.isMoving());
    // }

    @Test
    void shouldSelectMultipleEntities() {
        // Create two entities and add input components to both
        Entity entityOne = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
        WorkerInputComponent inputComponentOne = entityOne.getComponent(WorkerInputComponent.class);
        Entity entityTwo = createEntityAtPos(WORLD_X, WORLD_Y_TWO);
        WorkerInputComponent inputComponentTwo = entityOne.getComponent(WorkerInputComponent.class);
        // Set up mock for unproject() function
        when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
        when(camera.unproject(SCREEN_POSITION_TWO)).thenReturn(WORLD_POSITION_TWO);
        assertFalse(inputComponentOne.isSelected());
        assertFalse(inputComponentTwo.isSelected());
        // Trigger touch down event with left-click at screen position one
        inputComponentOne.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
        // Trigger touch down event with left-click at screen position two
        inputComponentTwo.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.LEFT);
        assertTrue(inputComponentOne.isSelected());
        assertTrue(inputComponentTwo.isSelected());
    }


    // @Test
    // void shouldMoveMultipleSelectedEntities() {
    //     // Create two entities and add input components to both
    //     Entity entityOne = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
    //     WorkerInputComponent inputComponentOne = entityOne.getComponent(WorkerInputComponent.class);
    //     Entity entityTwo = createEntityAtPos(WORLD_X, WORLD_Y_TWO);
    //     WorkerInputComponent inputComponentTwo = entityTwo.getComponent(WorkerInputComponent.class);
    //     // Set up mock for unproject() function
    //     when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
    //     when(camera.unproject(SCREEN_POSITION_TWO)).thenReturn(WORLD_POSITION_TWO);
    //     // Trigger touch down event with left-click at screen position one
    //     inputComponentOne.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
    //     // Trigger touch down event with left-click at screen position two
    //     inputComponentTwo.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.LEFT);
    //     assertTrue(inputComponentOne.isSelected());
    //     assertTrue(inputComponentTwo.isSelected());
    //     createTimeSource();
    //     // Add worker idle task to entityOne and update
    //     WorkerIdleTask workerIdleTaskOne = addIdleTaskToEntity(entityOne);
    //     workerIdleTaskOne.start();
    //     workerIdleTaskOne.update();
    //     WorkerMovementTask movementTaskOne = workerIdleTaskOne.getMovementTask();
    //     assertFalse(movementTaskOne.isMoving());
    //     // Add worker idle task to entityTwo and update
    //     WorkerIdleTask workerIdleTaskTwo = addIdleTaskToEntity(entityTwo);
    //     workerIdleTaskTwo.start();
    //     workerIdleTaskTwo.update();
    //     WorkerMovementTask movementTaskTwo = workerIdleTaskTwo.getMovementTask();
    //     assertFalse(movementTaskTwo.isMoving());
    //     // Trigger touch down event with right-click
    //     inputComponentOne.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.RIGHT);
    //     inputComponentTwo.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.RIGHT);
    //     assertTrue(movementTaskOne.isMoving());
    //     assertTrue(movementTaskTwo.isMoving());
    // }

    @Test
    void shouldDeselectAllEntities() {
        // Create two entities and add input components to both
        Entity entityOne = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
        WorkerInputComponent inputComponentOne = entityOne.getComponent(WorkerInputComponent.class);
        Entity entityTwo = createEntityAtPos(WORLD_X, WORLD_Y_TWO);
        WorkerInputComponent inputComponentTwo = entityTwo.getComponent(WorkerInputComponent.class);
        // Set up mock for unproject() function
        when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
        when(camera.unproject(SCREEN_POSITION_TWO)).thenReturn(WORLD_POSITION_TWO);
        // Trigger touch down event with left-click at screen position one
        inputComponentOne.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
        // Trigger touch down event with left-click at screen position two
        inputComponentTwo.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.LEFT);
        assertTrue(inputComponentOne.isSelected());
        assertTrue(inputComponentTwo.isSelected());
        // Trigger key down event with ESC
        inputComponentOne.keyDown(Input.Keys.ESCAPE);
        inputComponentTwo.keyDown(Input.Keys.ESCAPE);
        assertFalse(inputComponentOne.isSelected());
        assertFalse(inputComponentTwo.isSelected());
    }

    // @Test
    // void shouldMoveToSelectedLocation() {
    //     Entity entity = createEntityAtPos(WORLD_X, WORLD_Y_ONE);
    //     WorkerInputComponent inputComponent = entity.getComponent(WorkerInputComponent.class);
    //     // Set up mock for unproject() function
    //     when(camera.unproject(SCREEN_POSITION_ONE)).thenReturn(WORLD_POSITION_ONE);
    //     when(camera.unproject(SCREEN_POSITION_TWO)).thenReturn(WORLD_POSITION_TWO);
    //     // Trigger touch down event with left-click at screen position one
    //     inputComponent.touchDown(SCREEN_X, SCREEN_Y_ONE, 0, Input.Buttons.LEFT);
    //     assertTrue(inputComponent.isSelected());
    //     createTimeSource();
    //     // Add worker idle task and update
    //     WorkerIdleTask workerIdleTask = addIdleTaskToEntity(entity);
    //     workerIdleTask.start();
    //     workerIdleTask.update();
    //     WorkerMovementTask movementTask = workerIdleTask.getMovementTask();
    //     assertFalse(movementTask.isMoving());
    //     // Trigger touch down event with right-click at screen position two
    //     inputComponent.touchDown(SCREEN_X, SCREEN_Y_TWO, 0, Input.Buttons.RIGHT);
    //     assertTrue(movementTask.isMoving());
    //     workerIdleTask.update();
    //     // Find target of entity's bottom-right corner
    //     Vector2 entityDeltas = entity.getPosition().sub(entity.getCenterPosition());
    //     Vector2 centerTarget = new Vector2(WORLD_X, WORLD_Y_TWO).add(entityDeltas);
    //     entity.setPosition(centerTarget);
    //     workerIdleTask.update();
    //     assertFalse(movementTask.isMoving());
    //     assertEquals(Task.Status.FINISHED, movementTask.getStatus());
    // }

    public Entity createEntityAtPos(int x, int y) {
        InputComponent inputComponent =
                (WorkerInputComponent) ServiceLocator.getInputService().getInputFactory().createForWorker();
        Entity entity = new Entity().addComponent(inputComponent)
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent());
        entity.create();
        entity.setPosition(new Vector2(x, y));
        entity.setScale(50.0f, 50.0f);
        return entity;
    }

    public WorkerIdleTask addIdleTaskToEntity(Entity entity) {
        WorkerIdleTask workerIdleTask = new WorkerIdleTask();
        workerIdleTask.create(() -> entity);
        return workerIdleTask;
    }

    public void createTimeSource() {
        GameTime gameTime = mock(GameTime.class);
        when(gameTime.getTime()).thenReturn(0L);
        ServiceLocator.registerTimeSource(gameTime);
    }
}
