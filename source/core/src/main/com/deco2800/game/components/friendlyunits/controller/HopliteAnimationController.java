package com.deco2800.game.components.friendlyunits.controller;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class HopliteAnimationController extends Component{
    AnimationRenderComponent animator;

    @Override
    public void create(){
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("unitIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("unitHighlightedIdleAnimate", this::animateHighlightedIdle);
        entity.getEvents().addListener("unitLeftMoveAnimate", this::animateLeftMove);
        entity.getEvents().addListener("unitRightMoveAnimate", this::animateRightMove);
        entity.getEvents().addListener("unitHighlightedLeftMoveAnimate", this::animateHighlightedLeftMove);
        entity.getEvents().addListener("unitHighlightedRightMoveAnimate", this::animateHighlightedRightMove);
        entity.getEvents().addListener("unitLeftAttackAnimate", this::animateLeftAttack);
        entity.getEvents().addListener("unitRightAttackAnimate", this::animateRightAttack);
        entity.getEvents().addListener("unitHighlightedLeftAttackAnimate", this::animateHighlightedLeftAttack);
        entity.getEvents().addListener("unitHighlightedRightAttackAnimate", this::animateHighlightedRightAttack);
    }

    void animateIdle(){
        animator.startAnimation("hoplite_forward_left_idle");
    }

    void animateHighlightedIdle(){
        animator.startAnimation("hoplite_forward_left_idle_highlighted");
    }

    void animateLeftMove(){
        animator.startAnimation("hoplite_forward_left_move");
    }

    void animateRightMove(){
        animator.startAnimation("hoplite_forward_right_move");
    }

    void animateHighlightedLeftMove(){
        animator.startAnimation("hoplite_forward_left_move_highlighted");
    }

    void animateHighlightedRightMove(){
        animator.startAnimation("hoplite_forward_right_move_highlighted");
    }

    void animateLeftAttack(){
        animator.startAnimation("hoplite_forward_left_attack");
    }

    void animateRightAttack(){
        animator.startAnimation("hoplite_forward_right_attack");
    }

    void animateHighlightedLeftAttack(){
        animator.startAnimation("hoplite_forward_left_attack_highlighted");
    }

    void animateHighlightedRightAttack(){
        animator.startAnimation("hoplite_forward_right_attack_highlighted");
    }
}
