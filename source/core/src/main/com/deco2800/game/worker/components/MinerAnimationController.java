package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class MinerAnimationController extends Component {
    
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("workerIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("workerForwardLeftMoveAnimate", this::animateForwardLeftMove);
        entity.getEvents().addListener("workerForwardRightMoveAnimate", this::animateForwardRightMove);
        entity.getEvents().addListener("workerBackwardLeftMoveAnimate", this::animateBackLeftMove);
        entity.getEvents().addListener("workerBackwardRightMoveAnimate", this::animateBackRightMove);
        entity.getEvents().addListener("workerForwardLeftActionAnimate", this::animateForwardLeftAction);
        entity.getEvents().addListener("workerForwardRightActionAnimate", this::animateForwardRightAction);
        // Highlight
        entity.getEvents().addListener("workerHighlightedIdleAnimate", this::animateHighlightedIdle);
        entity.getEvents().addListener("workerHighlightedForwardLeftMoveAnimateAnimate", this::animateHighlightedForwardLeftMove);
        entity.getEvents().addListener("workerHighlightedForwardRightMoveAnimate", this::animateHighlightedForwardRightMove);
        entity.getEvents().addListener("workerHighlightedBackwardLeftMoveAnimate", this::animateHighlightedBackLeftMove);
        entity.getEvents().addListener("workerHighlightedBackwardRightMoveAnimate", this::animateHighlightedBackRightMove);
        entity.getEvents().addListener("workerHighlightedForwardLeftActionAnimate", this::animateHighlightedForwardLeftAction);
        entity.getEvents().addListener("workerHighlightedForwardRightActionAnimate", this::animateHighlightedForwardRightAction);
    }

    void animateIdle() {
        animator.startAnimation("miner_idle");
    }

    void animateHighlightedIdle() {
        animator.startAnimation("miner_idle_highlighted");
    }

    void animateForwardLeftMove() {
        animator.startAnimation("miner_forward_left_move");
    }

    void animateHighlightedForwardLeftMove() {
        animator.startAnimation("miner_forward_left_move_highlighted");
    }

    void animateForwardRightMove() {
        animator.startAnimation("miner_forward_right_move");
    }

    void animateHighlightedForwardRightMove() {
        animator.startAnimation("miner_forward_right_move_highlighted");
    }

    void animateBackLeftMove() {
        animator.startAnimation("miner_back_left_move");
    }

    void animateHighlightedBackLeftMove() {
        animator.startAnimation("miner_back_left_move_highlighted");
    }

    void animateBackRightMove() {
        animator.startAnimation("miner_back_right_move");
    }

    void animateHighlightedBackRightMove() {
        animator.startAnimation("miner_back_right_move_highlighted");
    }

    void animateForwardLeftAction() {
        animator.startAnimation("miner_forward_left_action");
    }

    void animateHighlightedForwardLeftAction() {
        animator.startAnimation("miner_forward_left_action_highlighted");
    }

    void animateForwardRightAction() {
        animator.startAnimation("miner_forward_right_action");
    }

    void animateHighlightedForwardRightAction() {
        animator.startAnimation("miner_forward_right_action_highlighted");
    }
}


