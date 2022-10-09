package com.deco2800.game.components.friendlyunits.controller;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class SwordsmanAnimationController extends Component{
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
        animator.startAnimation("swordsman_forward_left_idle");
    }

    void animateHighlightedIdle(){
        animator.startAnimation("swordsman_forward_left_idle_highlight");
    }

    void animateLeftMove(){
        animator.startAnimation("swordsman_forward_left_move");
    }

    void animateRightMove(){
        animator.startAnimation("swordsman_forward_right_move");
    }

    void animateHighlightedLeftMove(){
        animator.startAnimation("swordsman_forward_left_move_highlight");
    }

    void animateHighlightedRightMove(){
        animator.startAnimation("swordsman_forward_right_move_highlight");
    }

    void animateLeftAttack(){
        animator.startAnimation("swordsman_forward_left_attack");
    }

    void animateRightAttack(){
        animator.startAnimation("swordsman_forward_right_attack");
    }

    void animateHighlightedLeftAttack(){
        animator.startAnimation("swordsman_forward_left_attack_highlight");
    }

    void animateHighlightedRightAttack(){
        animator.startAnimation("swordsman_forward_right_attack_highlight");
    }
}
