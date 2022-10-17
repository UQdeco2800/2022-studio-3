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
    private static final String HALF_ATTACKED = "50-attacked";
    private static final String FULL_HEALTH = "100-idle";
    private static final String FULL_ATTACKED = "100-attacked";
    private static final String COLLAPSE = "collapse";
    private static final String REBUILD = "reconstruction";

    private boolean isShip;
    private int healthCode;
    String currentDirection;

    @Override
    public void create() {
        super.create();

        isShip = false;
        healthCode = 100;

        animator = this.entity.getComponent(AnimationRenderComponent.class);
        D_health = this.entity.getComponent(CombatStatsComponent.class).getHealth();
        currentDirection = "";

        // Damage transition events
        entity.getEvents().addListener("damaged", this::damagedTransition);
        entity.getEvents().addListener("collapsing", this::collapseTransition);
        entity.getEvents().addListener("creation", this::creationTransition);
        // Under attack transition
        entity.getEvents().addListener("underAttackFull", this::underAttackFull);
        entity.getEvents().addListener("underAttackHalf", this::underAttackHalf);

//        animator.startAnimation("default");
        animator.startAnimation(REBUILD);
    }
    
    /**
     * This method plays an animation that transitions
     * the building to its damaged state.
     */
    private void damagedTransition() {
        if(animator.isFinished()) {
            animator.startAnimation(HALF_HEALTH_TRANSITION);
        }
    }

    /**
     * This method plays an animation that transitions
     * the building to its collapsed state.
     */
    private void collapseTransition() {
        if(animator.isFinished()) {
            animator.startAnimation(COLLAPSE);
        }
        ServiceLocator.getEntityService().unregister(this.getEntity());
        this.getEntity().getComponent(PhysicsComponent.class).getPhysics().addToDestroy(this.entity);
    }

    /**
     * This method plays an animation when building
     * is being created/constructed/reconstructed.
     */
    private void creationTransition() {
        if (animator.isFinished()) {
            animator.startAnimation(REBUILD);
        }
    }

    /**
     * This method plays an animation when building
     * health is greater than 50% and is being
     * attacked
     */
    private void underAttackFull() {
        if (animator.isFinished()) {
            animator.startAnimation(FULL_ATTACKED);
        }
    }

    /**
     * This method plays an animation when building
     * health is greater lesser than 50% (inclusive)
     * and is being attacked.
     */
    private void underAttackHalf() {
        if (animator.isFinished()) {
             animator.startAnimation(HALF_ATTACKED);
        }
    }
}
