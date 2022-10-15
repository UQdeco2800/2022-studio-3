package com.deco2800.game.components.building;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

/**
 * Component to handle building animations, this component
 * simply listens to events and plays animation associated
 * with events called.
 */
public class BuildingAnimationController extends Component {
    AnimationRenderComponent animator;
    private int D_health;
    private boolean dispose = false;

    private static final String HALF_HEALTH = "50-idle";
    private static final String HALF_HEALTH_TRANSITION = "50";
    private static final String FULL_HEALTH = "100";
    private static final String FULL_ATTACKED = "attacked";
    private static final String COLLAPSE = "collapse";
    private static final String REBUILD = "reconstruction";

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        D_health = this.entity.getComponent(CombatStatsComponent.class).getHealth();
        entity.getEvents().addListener("HealthAnimation", this::updateAnimation);

        // Damage transition events
        entity.getEvents().addListener("damaged", this::damagedTransition);
        entity.getEvents().addListener("collapsing", this::collapseTransition);
        entity.getEvents().addListener("creation", this::creationTransition);
        // Under attack transition
        entity.getEvents().addListener("underAttackFull", this::underAttackFull);
        entity.getEvents().addListener("underAttackHalf", this::underAttackHalf);
    }

    void updateAnimation() {
//        // Updates the health value in HealthComponent
//        int health = this.entity.getComponent(CombatStatsComponent.class).getHealth();
//
//        if (health <= D_health && health >= 0.5*D_health) {
//            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.NORMAL);
//        } else if (health < 0.5*D_health && health > 0) {
//            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.HALF);
//        } else if (health <= 0 && !dispose) {
//            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.DEAD);
//            dispose = true;
//        } else if (dispose) {
//            this.entity.getComponent(PhysicsComponent.class).getPhysics().addToDestroy(this.entity);
//        }
//        // Applies the correct animation
//        if (this.enabled) {
//            animator.startAnimation(entity.getComponent(HealthAnimation.class).getAnimation());
//        }
    }

    /**
     * This method plays an animation that transitions
     * the building to its damaged state.
     */
    private void damagedTransition() {
        animator.startAnimation(HALF_HEALTH_TRANSITION);
        animator.startAnimation(HALF_HEALTH);
    }

    /**
     * This method plays an animation that transitions
     * the building to its collapsed state.
     */
    private void collapseTransition() {
        animator.startAnimation(COLLAPSE);
    }

    /**
     * This method plays an animation when building
     * is being created/constructed/reconstructed.
     */
    private void creationTransition() {
        animator.startAnimation(REBUILD);
        animator.startAnimation(FULL_HEALTH);
    }

    /**
     * This method plays an animation when building
     * health is greater than 50% and is being
     * attacked
     */
    private void underAttackFull() {
        animator.startAnimation(FULL_ATTACKED);
    }

    /**
     * This method plays an animation when building
     * health is greater lesser than 50% (inclusive)
     * and is being attacked.
     */
    private void underAttackHalf() {
        // animator.startAnimation(HALF_ATTACKED);
    }
}
