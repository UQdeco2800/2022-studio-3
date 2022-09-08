package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class ForagerAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("workerIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("workerWalkAnimate", this::animateMove);
        entity.getEvents().addListener("workerForagingAnimateRight", this::animateForagingRight);
        entity.getEvents().addListener("workerForagingAnimateLeft", this::animateForagingLeft);
        entity.getEvents().addListener("workerWalkRightAnimate", this::animateRightMove);
        entity.getEvents().addListener("workerWalkLeftAnimate", this::animateLeftMove);
    }

    void animateIdle() {
        animator.startAnimation("forager_forward_idle");
    }

    void animateMove() {
        animator.startAnimation("forager_forward_move");
    }

    void animateForagingRight(){
        animator.startAnimation("foragerActionRight");
    }

    void animateForagingLeft() {
        animator.startAnimation("foragerActionLeft");
    }

    void animateRightMove() {
        animator.startAnimation("foragerRight");
    }

    void animateLeftMove() {
        animator.startAnimation("foragerLeft");
    }
}
