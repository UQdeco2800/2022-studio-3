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
        entity.getEvents().addListener("workerForwardLeftMove", this::animateForwardLeftMove);
        entity.getEvents().addListener("workerForwardRightMove", this::animateForwardRightMove);
        entity.getEvents().addListener("workerBackLeftMove", this::animateBackLeftMove);
        entity.getEvents().addListener("workerBackRightMove", this::animateBackRightMove);
        entity.getEvents().addListener("workerForwardLeftAction", this::animateForwardLeftAction);
        entity.getEvents().addListener("workerForwardRightAction", this::animateForwardRightAction);
    }

    void animateIdle() {
        animator.startAnimation("miner_idle");
    }

    void animateForwardLeftMove() {
        animator.startAnimation("miner_forward_left_move");
    }

    void animateForwardRightMove() {
        animator.startAnimation("miner_forward_right_move");
    }

    void animateBackLeftMove() {
        animator.startAnimation("miner_back_left_move");
    }

    void animateBackRightMove() {
        animator.startAnimation("miner_back_right_move");
    }

    void animateForwardLeftAction() {
        animator.startAnimation("miner_forward_left_action");
    }

    void animateForwardRightAction() {
        animator.startAnimation("miner_forward_right_action");
    }
}


