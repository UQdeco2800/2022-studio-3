package com.deco2800.game.components.player;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class BoxBoyActions extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = true;

    private boolean selected = true;

    @Override
    public void create() {
        physicsComponent = entity.getComponent(PhysicsComponent.class);
    }

    private void select() {
        this.selected = true;
    }

    private void deSelect() {
        this.selected = false;
    }

    @Override
    public void update() {
        if (moving) {
            updateSpeed();
        }
    }

    private void updateSpeed() {
        Body body = physicsComponent.getBody();
        Vector2 velocity = body.getLinearVelocity();
        Vector2 desiredVelocity = walkDirection.cpy().scl(MAX_SPEED);
        // impulse = (desiredVel - currentVel) * mass
        Vector2 impulse = desiredVelocity.sub(velocity).scl(body.getMass());
        body.applyLinearImpulse(impulse, body.getWorldCenter(), true);
    }

    /**
     * Moves the player towards a given direction.
     *
     * @param direction direction to move in
     */
    void walk(Vector2 direction) {
        if (selected) {
            this.walkDirection = direction;
            moving = true;
        }
    }

    /**
     * Stops the player from walking.
     */
    void stopWalking() {
        if (selected) {
            this.walkDirection = Vector2.Zero.cpy();
            updateSpeed();
            moving = false;
        }
    }
}
