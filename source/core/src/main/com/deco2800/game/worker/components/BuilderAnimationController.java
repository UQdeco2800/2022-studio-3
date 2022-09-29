package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class BuilderAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("workerIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("workerForwardLeftMove", this::animateForwardLeftMove);
        entity.getEvents().addListener("workerForwardRightMove", this::animateForwardRightMove);
        entity.getEvents().addListener("workerBackLeftMove", this::animateBackLeftMove);
        entity.getEvents().addListener("workerBackRightMove", this::animateBackRightMove);
    }

    void animateIdle() {
        animator.startAnimation("builder_idle");
    }

    void animateForwardLeftMove() {
        animator.startAnimation("builder_forward_left_move");
    }

    void animateForwardRightMove() {
        animator.startAnimation("builder_forward_right_move");
    }

    void animateBackLeftMove() {
        animator.startAnimation("builder_back_left_move");
    }

    void animateBackRightMove() {
        animator.startAnimation("builder_back_right_move");
    }
}
