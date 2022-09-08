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
        entity.getEvents().addListener("workerWalkAnimate", this::animateMove);
        entity.getEvents().addListener("workerMiningAnimate", this::animateMining);
        entity.getEvents().addListener("workerWalkRightAnimate", this::animateRightMove);
        entity.getEvents().addListener("workerWalkLeftAnimate", this::animateLeftMove);
        entity.getEvents().addListener("workerMiningAnimateLeft", this::animateMiningLeft);
    }

    void animateIdle() {
        animator.startAnimation("miner_forward_idle");
    }

    void animateMove() {
        animator.startAnimation("miner_forward_move");
    }

    void animateMining(){
        animator.startAnimation("minerActionRight");
    }
    
    void animateMiningLeft() {
        animator.startAnimation("minerActionLeft");
    }

    void animateRightMove() {
        animator.startAnimation("minerRight");
    }

    void animateLeftMove() {
        animator.startAnimation("minerLeft");
    }
}


