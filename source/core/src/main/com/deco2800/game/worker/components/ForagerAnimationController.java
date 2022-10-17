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
        entity.getEvents().addListener("workerForwardLeftMove", this::animateForwardLeftMove);
        entity.getEvents().addListener("workerForwardRightMove", this::animateForwardRightMove);
        entity.getEvents().addListener("workerBackLeftMove", this::animateBackLeftMove);
        entity.getEvents().addListener("workerBackRightMove", this::animateBackRightMove);
        entity.getEvents().addListener("workerForwardLeftAction", this::animateForwardLeftAction);
        entity.getEvents().addListener("workerForwardRightAction", this::animateForwardRightAction);
        // Highlight
        entity.getEvents().addListener("workerHighlightedIdleAnimate", this::animateHighlightedIdle);
        entity.getEvents().addListener("workerHighlightedForwardLeftMove", this::animateHighlightedForwardLeftMove);
        entity.getEvents().addListener("workerHighlightedForwardRightMove", this::animateHighlightedForwardRightMove);
        entity.getEvents().addListener("workerHighlightedBackLeftMove", this::animateHighlightedBackLeftMove);
        entity.getEvents().addListener("workerHighlightedBackRightMove", this::animateHighlightedBackRightMove);
        entity.getEvents().addListener("workerHighlightedForwardLeftAction", this::animateHighlightedForwardLeftAction);
        entity.getEvents().addListener("workerHighlightedForwardRightAction", this::animateHighlightedForwardRightAction);
    }

    void animateIdle() {
        animator.startAnimation("forager_idle");
    }

    void animateHighlightedIdle() {
        animator.startAnimation("forager_idle_highlighted");
    }

    void animateForwardLeftMove() {
        animator.startAnimation("forager_forward_left_move");
    }

    void animateHighlightedForwardLeftMove() {
        animator.startAnimation("forager_forward_left_move_highlighted");
    }

    void animateForwardRightMove() {
        animator.startAnimation("forager_forward_right_move");
    }

    void animateHighlightedForwardRightMove() {
        animator.startAnimation("forager_forward_right_move_highlighted");
    }

    void animateBackLeftMove() {
        animator.startAnimation("forager_back_left_move");
    }

    void animateHighlightedBackLeftMove() {
        animator.startAnimation("forager_back_left_move_highlighted");
    }

    void animateBackRightMove() {
        animator.startAnimation("forager_back_right_move");
    }

    void animateHighlightedBackRightMove() {
        animator.startAnimation("forager_back_right_move_highlighted");
    }

    void animateForwardLeftAction() {
        animator.startAnimation("forager_forward_left_action");
    }

    void animateHighlightedForwardLeftAction() {
        animator.startAnimation("forager_forward_left_action_highlighted");
    }

    void animateForwardRightAction() {
        animator.startAnimation("forager_forward_right_action");
    }

    void animateHighlightedForwardRightAction() {
        animator.startAnimation("forager_forward_right_action_highlighted");
    }
}
