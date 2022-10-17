package com.deco2800.game.components.building;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.EntityDirection;
import com.deco2800.game.components.EntityDirectionComponent;
import com.deco2800.game.entities.Entity;

/**
 * There are stationary buildings (trebuchet)
 * in the game that change their sprite depending
 * on what direction their target is in, this
 * component implements similar direction detection
 * as PhysicsMovementComponent, but for stationary
 * entities.
 */
public class UpdateBuildingDirection extends Component {
    Entity target;
    EntityDirectionComponent entityDirection;


    public UpdateBuildingDirection(Entity target) {
        this.target = target;
    }

    @Override
    public void create() {
        entityDirection = this.getEntity().getComponent(EntityDirectionComponent.class);
    }

    /**
     * Checks the direction building is pointing in relation to
     * assigned target. It is assumed that any building that used this
     * component has a target.
     */
    @Override
    public void update() {
        Vector2 direction = target.getCenterPosition().cpy().sub(this.entity.getCenterPosition()).nor();
        if (Math.abs(direction.x) > Math.abs(direction.y)) {
            if (direction.x < 0) {
                west();
            } else if (direction.x > 0) {
                east();
            }
        } else {
            if (direction.y > 0) {
                north();
            } else if (direction.y < 0) {
                south();
            }
        }
    }

    /**
     * Changes the direction to face west and trigger that respective animation
     * on the condition that the previous recorded direction is not west.
     */
    private void west() {
        entityDirection.setDirectionWest();
    }

    /**
     * Changes the direction to face east and trigger that respective animation
     * on the condition that the previous recorded direction is not east.
     */
    private void east() {
        entityDirection.setDirectionEast();
    }

    /**
     * Changes the direction to face north and trigger that respective animation
     * on the condition that the previous recorded direction is not north.
     */
    private void north() {
        entityDirection.setDirectionNorth();
    }

    /**
     * Changes the direction to face south and trigger that respective animation
     * on the condition that the previous recorded direction is not south.
     */
    private void south() {
        entityDirection.setDirectionSouth();
    }
}
