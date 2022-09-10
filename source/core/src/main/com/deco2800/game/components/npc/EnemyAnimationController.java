package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to enemy entity's state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("goLeft", this::animateLeft);
        entity.getEvents().addListener("goRight", this::animateRight);
        entity.getEvents().addListener("goUp", this::animateUp);
        entity.getEvents().addListener("goDown", this::animateDown);
        entity.getEvents().addListener("stopUp", this::stopUp);
        entity.getEvents().addListener("stopDown", this::stopDown);
        entity.getEvents().addListener("stopLeft", this::stopLeft);
        entity.getEvents().addListener("stopRight", this::stopRight);
    }

    public void animateLeft() {
        animator.startAnimation("move-west");
    }

    public void animateRight() {
        animator.startAnimation("move-east");
    }

    public void animateUp() {
        animator.startAnimation("move-north");
    }

    public void animateDown() {
        animator.startAnimation("move-south");
    }

    public void stopDown() {
        animator.startAnimation("default");
    }
    public void stopLeft() {
        animator.startAnimation("default");
    }
    public void stopRight() {
        animator.startAnimation("default");
    }
    public void stopUp() {
        animator.startAnimation("default");
    }
}
