package com.deco2800.game.components.tasks;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.physics.components.PhysicsComponent;

/**
 * A class that makes enemies move horizontally in a constant speed.
 */
public class left_rightTask extends DefaultTask implements PriorityTask {
    private final int priority;
    private final float x;
    private float p_x = 0;
    private boolean isMove = true;

    /**
     * @param priority Task priority when chasing (0 when not chasing).
     */
    public left_rightTask(float x, int priority) {
        this.x = x;
        this.priority = priority;
    }

    @Override
    public void start() {
        super.start();
        p_x = this.owner.getEntity().getPosition().x;
    }

    /**
     * When the enemy reaches the boundary of the moving track, then moves in the opposite direction.
     */
    @Override
    public void update() {
        float position_x = this.owner.getEntity().getPosition().x;
        if (isMove) {
            if(position_x <= p_x + x/2) {
                this.owner.getEntity().getComponent(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(1,0));
            } else {
                isMove = false;
            }
        } else {
            if(position_x >= p_x - x/2) {
                this.owner.getEntity().getComponent(PhysicsComponent.class).getBody().setLinearVelocity(new Vector2(-1,0));
            } else {
                isMove = true;
            }
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        return priority;
    }
}
