package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.WorkerFactory;
import com.deco2800.game.worker.type.ForagerFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT) //One test case doesn't use one function in one mock - so this is set to prevent an exception
public class GateColliderTest {
    @BeforeEach
    void beforeEach() {
        ServiceLocator.clear();
        //Store required textures and texture atlases
        TextureAtlas gateAtlas = new TextureAtlas(Gdx.files.internal("images/ew_gate.atlas"));
        TextureAtlas foragerAtlas = new TextureAtlas(Gdx.files.internal("images/forager.atlas"));
        Texture gateClosed = new Texture(Gdx.files.internal("images/gate_ew_closed.png"));
        Texture gateOpen = new Texture(Gdx.files.internal("images/gate_ew_open.png"));
        //Mock resource service to return desired textures
        ResourceService rs = mock(ResourceService.class);
        when(rs.getAsset(anyString(), eq(TextureAtlas.class))).thenReturn(gateAtlas).thenReturn(foragerAtlas);
        when(rs.getAsset(anyString(), eq(Texture.class))).thenReturn(gateOpen);
        //Register necessary services;
        ServiceLocator.registerResourceService(rs);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    //Helper method to create a mocked fixture for an entity
    private Fixture mockFixture(Entity entity) {
        //Mock fixture
        Fixture fixture = mock(Fixture.class);
        //Mock body
        Body body = mock(Body.class);
        //Make BodyUserData which returns correct entity
        BodyUserData fixtureData = new BodyUserData();
        fixtureData.entity = entity;
        body.setUserData(fixtureData);
        //Set body mock to return the user data
        when(body.getUserData()).thenReturn(fixtureData);
        //Set fixture mock to return body mock
        when(fixture.getBody()).thenReturn(body);
        return fixture;
    }

    /**
     * Tests the gate's status when a friendly unit comes into contact, then leaves
     */
    @Test
    public void friendlyUnitEnterLeave() {
        Entity gate = BuildingFactory.createEWGate();
        Entity friendly = ForagerFactory.createForager();

        //Mock fixtures for each entity and corresponding bodies
        Fixture friendlyFixture = mockFixture(friendly);
        Fixture gateFixture = mockFixture(gate);
        ColliderComponent gateCollider = gate.getComponent(ColliderComponent.class);

        GateCollider gc = gate.getComponent(GateCollider.class);

        //Simulate ally collision
        gc.onCollisionStart(gateFixture, friendlyFixture);

        //Assert it has stored 1 collided object
        assertEquals(1, gc.getCollidedIds().size());
        assertTrue("Gate must be open upon friendly contact", gc.isGateOpen());

        //Simulate ally leaving
        gc.onCollisionEnd(gateFixture, friendlyFixture);

        //Assert it has stored 0 objects
        assertEquals(0, gc.getCollidedIds().size());
        assertFalse(gc.isGateOpen());
    }


    /**
     * Tests an enemy entity coming into contact with and leaving the gate - it should remain closed
     */
    @Test
    public void enemyUnitEnterLeave() {
        Entity gate = BuildingFactory.createEWGate();
        Entity enemy = new Entity();    //Any entity without a FriendlyComponent is an enemy of the gate

        //Mock fixtures for each
        Fixture gateFixture = mockFixture(gate);
        Fixture enemyFixture = mockFixture(enemy);

        GateCollider gc = gate.getComponent(GateCollider.class);

        //Simulate enemy collision
        gc.onCollisionStart(gateFixture, enemyFixture);

        //Assert gate is closed
        assertEquals(0, gc.getCollidedIds().size());
        assertFalse(gc.isGateOpen());

        //Simulate enemy leaving
        gc.onCollisionEnd(gateFixture, enemyFixture);

        //Assert gate is still closed
        assertEquals(0, gc.getCollidedIds().size());
        assertFalse(gc.isGateOpen());
    }

    /**
     * Tests gate behaviour when multiple allied units come into contact with the gate
     * and then leave. If at least one unit is in contact with the gate, it should still be open.
     */
    @Test
    public void multipleUnitsInContact() {
        Entity gate = BuildingFactory.createEWGate();
        Entity firstFriendly = WorkerFactory.createWorker();
        Entity secondFriendly = WorkerFactory.createWorker();

        //Mock fixtures
        Fixture gateFixture = mockFixture(gate);
        Fixture ffFixture = mockFixture(firstFriendly);
        Fixture sfFixture = mockFixture(secondFriendly);

        GateCollider gc = gate.getComponent(GateCollider.class);

        //Simulate 2 different allies colliding
        gc.onCollisionStart(gateFixture, ffFixture);
        gc.onCollisionStart(gateFixture, sfFixture);

        //Assert gate is open and registers 2 collisions
        assertEquals(2, gc.getCollidedIds().size());
        assertTrue("Gate must be open upon friendly contact", gc.isGateOpen());

        //Simulate 1 ally leaving
        gc.onCollisionEnd(gateFixture, ffFixture);

        //Assert gate is still open and registers 1 collision
        assertEquals(1, gc.getCollidedIds().size());
        assertTrue("Gate must be open upon friendly contact", gc.isGateOpen());
    }

    /**
     * Tests to ensure when the same allied unit registers a collision continually, it is
     * still only recognised as one unique current collision with the gate
     */
    @Test
    public void sameUnitMultipleContact() {
        Entity gate = BuildingFactory.createEWGate();
        Entity friendly = ForagerFactory.createForager();

        //Mock fixtures for each entity and corresponding bodies
        Fixture friendlyFixture = mockFixture(friendly);
        Fixture gateFixture = mockFixture(gate);
        ColliderComponent gateCollider = gate.getComponent(ColliderComponent.class);

        GateCollider gc = gate.getComponent(GateCollider.class);

        //Simulate 2 ally collisions of the same entity
        gc.onCollisionStart(gateFixture, friendlyFixture);
        gc.onCollisionStart(gateFixture, friendlyFixture);

        //Assert it has stored only 1 collided object
        assertEquals(1, gc.getCollidedIds().size());
        assertTrue("Gate must be open upon friendly contact", gc.isGateOpen());
    }
}
