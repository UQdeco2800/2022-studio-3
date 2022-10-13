package com.deco2800.game.soldiers.animation;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class HopliteAnimationController extends Component {
    private AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("soldierIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("soldierForwardLeftMove", this::animateForwardLeftMove);
        entity.getEvents().addListener("soldierForwardRightMove", this::animateForwardRightMove);
        entity.getEvents().addListener("soldierBackLeftMove", this::animateBackLeftMove);
        entity.getEvents().addListener("soldierBackRightMove", this::animateBackRightMove);
    }

    @Override
    public void update() {
        super.update();
        animator.startAnimation("hoplite_forward_left_idle");
    }

    public void animateIdle() {
        animator.startAnimation("hoplite_forward_left_idle");
    }

    public void animateForwardLeftMove() {
        animator.startAnimation("hoplite_forward_left_move");
    }

    public void animateForwardRightMove() {
        animator.startAnimation("hoplite_forward_right_move");
    }

    public void animateBackLeftMove() {
        animator.startAnimation("hoplite_backward_left_move");
    }

    public void animateBackRightMove() {
        animator.startAnimation("hoplite_backward_right_move");
    }
    
}
