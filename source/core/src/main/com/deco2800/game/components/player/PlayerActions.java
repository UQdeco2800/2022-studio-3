package com.deco2800.game.components.player;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.physics.components.PhysicsComponent;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {
    private static final Vector2 MAX_SPEED = new Vector2(3f, 3f); // Metres per second

    private PhysicsComponent physicsComponent;
    private SelectableComponent selectableComponent;
    private Vector2 walkDirection = Vector2.Zero.cpy();
    private boolean moving = false;


    @Override
    public void create() {
        selectableComponent = entity.getComponent(SelectableComponent.class);
        physicsComponent = entity.getComponent(PhysicsComponent.class);

        this.getEntity().getEvents().addListener("walk", this::walk);
        this.getEntity().getEvents().addListener("walStop", this::stopWalking);
    }


    @Override
    public void update() {
        if (moving && this.selectableComponent.isSelected()) {
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
        if (this.selectableComponent.isSelected()) {
            this.walkDirection = direction;
            moving = true;
        }
    }

    /**
     * Stops the player from walking.
     */
    void stopWalking() {
        if (this.selectableComponent.isSelected()) {
            this.walkDirection = Vector2.Zero.cpy();
            updateSpeed();
            moving = false;
        }
    }
}
