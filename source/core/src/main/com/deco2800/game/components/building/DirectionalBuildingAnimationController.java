package com.deco2800.game.components.building;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * Some buildings face different direction, as such they will
 * have animation in different directions, this component
 * accounts for these cases and handles animations and states
 * just for these buildings.
 */
public class DirectionalBuildingAnimationController extends Component {
    AnimationRenderComponent animator;

    // Building States
    private static final String FULL_HEALTH = "100-idle-";
    private static final String FULL_HEALTH_ATTACKED = "100-attacked-";

    private static final String HALF_HEALTH = "50-idle-";
    private static final String HALF_HEALTH_TRANSITION = "50-";
    private static final String HALF_HEALTH_ATTACKED = "50-attacked-";

    private static final String COLLAPSE = "collapse-";
    private static final String REBUILD = "reconstuction-";

    @Override
    public void create() {
        super.create();

        animator = this.entity.getComponent(AnimationRenderComponent.class);

        // Damage transition events
        this.entity.getEvents().addListener("damaged'", this::damageTransition);
        this.entity.getEvents().addListener("collapsing'", this::collapseTransition);
        this.entity.getEvents().addListener("creation'", this::creationTransition);

        // Under attack transition
        this.entity.getEvents().addListener("underAttackFull'", this::underAttackFull);
        this.entity.getEvents().addListener("underAttackHalf'", this::underAttackHalf);

        // Idle states
        this.entity.getEvents().addListener("fullHealth", this::fullHealthIdle);
        this.entity.getEvents().addListener("halfHealth", this::halfHealthIdle);

//        this.animateInDirection(FULL_HEALTH, "west");
    }

    /**
     * Generic function that plays an `animation` in a specified
     * `direction`
     *
     * @param animation animation to be played
     * @param direction direction variant of the animation
     *                  {north, south, east west}.
     */
    private void animateInDirection(String animation, String direction) {
        animator.startAnimation(animation + direction);
    }

    /**
     * Plays the damage animation in a specified direction
     * when building health is lesser than 50% (inclusive)
     * @param direction direction building is facing in
     */
    private void damageTransition(String direction) {
        animateInDirection(HALF_HEALTH_TRANSITION, direction);
        animateInDirection(HALF_HEALTH, direction);
    }

    /**
     * Plays the collapse animation in a specified direction
     * when building is being destroyed.
     * @param direction direction building is facing in
     */
    private void collapseTransition(String direction) {
        animateInDirection(COLLAPSE, direction);
    }

    /**
     * Plays the reconstruction animation in a specified
     * direction when building is being created/constructed/
     * reconstructed.
     * @param direction direction building is facing in
     */
    private void creationTransition(String direction) {
//        animateInDirection(REBUILD, direction);
        animateInDirection(FULL_HEALTH, direction);
    }

    /**
     * Plays the attacked animation in a specified
     * direction when building is being attacked and
     * building health is greater than 50% (exclusive)
     * @param direction direction building is facing in
     */
    private void underAttackFull(String direction) {
        animateInDirection(FULL_HEALTH_ATTACKED, direction);
    }

    /**
     * Plays the attacked animation in a specified
     * direction when building is being attacked and
     * building health is lesser than 50% (inclusive)
     * @param direction direction building is facing in
     */
    private void underAttackHalf(String direction) {
        animateInDirection(HALF_HEALTH_ATTACKED, direction);
    }

    /**
     * Changes to full health idle state in desired direction
     * @param direction direction building is facing in
     */
    private void fullHealthIdle(String direction) {
        animateInDirection(FULL_HEALTH, direction);
    }

    /**
     * Changes to half health idle state in desired direction
     * @param direction direction building is facing in
     */
    private void halfHealthIdle(String direction) {
        animateInDirection(HALF_HEALTH, direction);
    }


}
