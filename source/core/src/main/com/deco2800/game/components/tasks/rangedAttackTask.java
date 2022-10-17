package com.deco2800.game.components.tasks;

import com.deco2800.game.ai.tasks.DefaultTask;
import com.deco2800.game.ai.tasks.PriorityTask;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/** Attacks a target entity when it gets close to the enemy. */
public class rangedAttackTask extends DefaultTask implements PriorityTask {
    private final GameTime timeSource;
    private final Entity target;
    private final float waitTime;
    private final int priority;
    private final float distance;
    private long endtime;

    /**
     * @param target The entity to attack.
     * @param waitTime The waiting time between each attack.
     * @param priority Task priority when chasing (0 when not chasing).
     * @param distance The attack distance.
     *
     * Construct the AttackTask.
     */
    public rangedAttackTask(Entity target, float waitTime, int priority, float distance) {
        this.target = target;
        this.waitTime = waitTime;
        this.priority = priority;
        this.distance = distance;
        this.timeSource = ServiceLocator.getTimeSource();
    }

    @Override
    public void start() {
        super.start();
        endtime = timeSource.getTime() + (int)(waitTime * 1000);
    }

    @Override
    public void update() {
        if (timeSource.getTime() >= endtime) {
            if (this.owner.getEntity().getComponent(FriendlyComponent.class) != null) {
                this.owner.getEntity().getEvents().trigger("shoot");
            } else {
                this.owner.getEntity().getEvents().trigger("attack");
            }   
            endtime = timeSource.getTime() + (int)(waitTime * 1000);
        }
    }

    @Override
    public void stop() {
        super.stop();
    }

    @Override
    public int getPriority() {
        if (status == Status.ACTIVE) {
            return Active();
        }
        return inActive();
    }

    private float DistanceToTarget() {
        return owner.getEntity().getPosition().dst(target.getPosition());
    }

    private int Active() {
        float dst = DistanceToTarget();
        if (dst > distance) {
            return -1;
        }
        return priority;
    }

    private int inActive() {
        float dst = DistanceToTarget();
        if (dst <= distance) {
            return priority;
        }
        return -1;
    }
}
