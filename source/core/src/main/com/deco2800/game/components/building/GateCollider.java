package com.deco2800.game.components.building;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.BodyUserData;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class GateCollider extends Component {
    /**
     * A list containing unique IDs of each allied unit in contact with the gate,
     * if it is empty, then the gate is closed
     */
    private List<Integer> alliedUnitsCollided = new ArrayList<>();
    public boolean gateOpen = false;

    /**
     * On create, listen for the events "collisionStart" and "collisionEnd" initiated by the
     * PhysicsContactListener, which will execute the functions "onCollisionStart" and "onCollisionEnd"
     */
    @Override
    public void create() {
        entity.getEvents().addListener("collisionStart", this::onCollisionStart);
        entity.getEvents().addListener("collisionEnd", this::onCollisionEnd);
    }

    /**
     * Handles the collision of two physics Fixtures: if an allied unit collides with the gate,
     * open it
     *
     * @param gate the fixture related to the ColliderComponent attached to the Gate entity
     * @param other the fixture related to the ColliderComponent of an entity that has come
     *              into contact with the Gate's fixture
     */
    public void onCollisionStart(Fixture gate, Fixture other) {
        //If gate fixture is null or other fixture is not attached to an entity, do nothing
        if (gate == null || other.getBody().getUserData() == null) {
            return;
        }
        //Store the other's entity
        Entity collidedEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        //Determine if the entity is an allied unit
        if (collidedEntity.getComponent(FriendlyComponent.class) != null) {
            //This unit is a friendly unit
            //Determine entity ID
            int entityId = collidedEntity.getComponent(FriendlyComponent.class).getId();
            if (!alliedUnitsCollided.contains(entityId)) {
                //If this entity has not already initiated a collider event, add it to allies in contact with gate
                alliedUnitsCollided.add(entityId);
                if (alliedUnitsCollided.size() == 1) {
                    //First unit in contact with gate - open it
                    Entity gateEntity = ((BodyUserData) gate.getBody().getUserData()).entity;
                    //Determine gate orientation and associated string to change the provided texture
                    Building gateType = gateEntity.getComponent(BuildingActions.class).getType();
                    String gateString = gateType == Building.GATE_EW ? "ew" : "ns";

                    ColliderComponent gateCollider = gateEntity.getComponent(ColliderComponent.class);
                    //Turn off collision for this collider
                    gateCollider.setSensor(true);
                    gateOpen = true;
                    //Play opening animation
                    AnimationRenderComponent gateARC = gateEntity.getComponent(AnimationRenderComponent.class);
                    gateARC.startAnimation("open_gate");
                    //Update image asset to open
                    ResourceService resourceService = ServiceLocator.getResourceService();
                    Texture openTexture = resourceService.getAsset("images/gate_" + gateString +"_open.png", Texture.class);
                    gateEntity.getComponent(TextureRenderComponent.class).setTexture(openTexture);
                }
            }

        }

    }

    /**
     * Handles the end of collision of two physics Fixtures: if an allied unit has left the gate,
     * reduce the number of allies in contact with the gate, if no allies are in contact, close it.
     *
     * @param gate the fixture related to the ColliderComponent attached to the Gate entity
     * @param other the fixture related to the ColliderComponent of an entity that has come
     *              into contact with the Gate's fixture
     */
    public void onCollisionEnd(Fixture gate, Fixture other) {
        //If gate fixture is null or other fixture is not attached to an entity, do nothing
        if (gate == null || other.getBody().getUserData() == null) {
            return;
        }
        //Store the other's entity
        Entity collidedEntity = ((BodyUserData) other.getBody().getUserData()).entity;

        //Determine if the entity is an allied unit
        if (collidedEntity.getComponent(FriendlyComponent.class) != null) {
            //This unit is a friendly unit
            //Determine entity ID
            int entityId = collidedEntity.getComponent(FriendlyComponent.class).getId();
            if (alliedUnitsCollided.contains(entityId)) {
                //This entity is no longer in contact with the gate, remove it from the list
                alliedUnitsCollided.remove((Integer) entityId);
                if (alliedUnitsCollided.size() == 0) {
                    //No friendly units colliding - close the gate
                    Entity gateEntity = ((BodyUserData) gate.getBody().getUserData()).entity;
                    ColliderComponent gateCollider = gateEntity.getComponent(ColliderComponent.class);
                    //Turn on collision for this collider
                    gateCollider.setSensor(false);
                    gateOpen = false;
                    //Play closing animation
                    AnimationRenderComponent gateARC = gateEntity.getComponent(AnimationRenderComponent.class);
                    gateARC.startAnimation("close_gate");
                    //Update asset image to closed - REMOVED due to issues with animation
                    //ResourceService resourceService = ServiceLocator.getResourceService();
                    //Texture closedTexture = resourceService.getAsset("images/gate_ns_closed.png", Texture.class);
                    //gateEntity.getComponent(TextureRenderComponent.class).setTexture(closedTexture);
                }
            }
        }

    }

    /**
     * Returns a copy of the collided allies list - used for unit testing
     * @return copy of allies collided list
     */
    public List<Integer> getCollidedIds() {
        return new ArrayList<>(alliedUnitsCollided);
    }

    /**
     * Returns the status of the gate
     * @return true if it is open, else false
     */
    public boolean isGateOpen() {
        return this.gateOpen;
    }
}

