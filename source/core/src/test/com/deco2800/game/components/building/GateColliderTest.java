package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.type.ForagerFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mock;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class GateColliderTest {

    @BeforeAll
    static void beforeEach() {
        ServiceLocator.clear();
        //Store required textures and texture atlases
        TextureAtlas gateAtlas = new TextureAtlas(Gdx.files.internal("images/ew_gate.atlas"));
        TextureAtlas foragerAtlas = new TextureAtlas(Gdx.files.internal("images/forager_forward.atlas"));
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
    }

    //Helper method to create a mocked fixture for an entity
    private Fixture mockFixture(Entity entity) {
        //Mock fixture
        Fixture fixture = mock(Fixture.class);
        //Mock body
        Body body = mock(Body.class);
        //Set BodyUserData to return correct entity
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

    /*
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

     */

}
