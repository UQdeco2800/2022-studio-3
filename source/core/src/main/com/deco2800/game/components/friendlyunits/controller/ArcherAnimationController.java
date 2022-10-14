package com.deco2800.game.components.friendlyunits.controller;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class ArcherAnimationController extends Component{
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
        entity.getEvents().addListener("unitBackwardLeftMoveAnimate", this::animateBackwardLeftMove);
        entity.getEvents().addListener("unitBackwardRightMoveAnimate", this::animateBackwardRightMove);
        entity.getEvents().addListener("unitBackwardHighlightedLeftMoveAnimate", this::animateBackwardHighlightedLeftMove);
        entity.getEvents().addListener("unitBackwardHighlightedRightMoveAnimate", this::animateBackwardHighlightedRightMove);
    }

    void animateIdle(){
        animator.startAnimation("archer_forward_left_idle");
    }

    void animateHighlightedIdle(){
        animator.startAnimation("archer_forward_left_idle_highlight");
    }

    void animateLeftMove(){
        animator.startAnimation("archer_forward_left_move");
    }

    void animateRightMove(){
        animator.startAnimation("archer_forward_right_move");
    }

    void animateHighlightedLeftMove(){
        animator.startAnimation("archer_forward_left_move_highlight");
    }

    void animateHighlightedRightMove(){
        animator.startAnimation("archer_forward_right_move_highlight");
    }

    void animateLeftAttack(){
        animator.startAnimation("archer_forward_left_attack");
    }

    void animateRightAttack(){
        animator.startAnimation("archer_forward_right_attack");
    }

    void animateHighlightedLeftAttack(){
        animator.startAnimation("archer_forward_left_attack_highlight");
    }

    void animateHighlightedRightAttack(){
        animator.startAnimation("archer_forward_right_attack_highlight");
    }

    void animateBackwardLeftMove(){
        animator.startAnimation("archer_backward_left_move");
    }

    void animateBackwardRightMove(){
        animator.startAnimation("archer_backward_right_move");
    }

    void animateBackwardHighlightedLeftMove(){
        animator.startAnimation("archer_backward_left_move_highlighted");
    }

    void animateBackwardHighlightedRightMove(){
        animator.startAnimation("archer_backward_right_move_highlighted");
    }
}
