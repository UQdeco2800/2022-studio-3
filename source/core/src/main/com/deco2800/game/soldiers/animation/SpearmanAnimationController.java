package com.deco2800.game.soldiers.animation;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class SpearmanAnimationController extends Component{
    AnimationRenderComponent animator;

    @Override
    public void create(){
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("soldierIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("soldierHighlightedIdleAnimate", this::animateHighlightedIdle);
        entity.getEvents().addListener("soldierLeftMoveAnimate", this::animateLeftMove);
        entity.getEvents().addListener("soldierRightMoveAnimate", this::animateRightMove);
        entity.getEvents().addListener("soldierHighlightedLeftMoveAnimate", this::animateHighlightedLeftMove);
        entity.getEvents().addListener("soldierHighlightedRightMoveAnimate", this::animateHighlightedRightMove);
        entity.getEvents().addListener("soldierLeftAttackAnimate", this::animateLeftAttack);
        entity.getEvents().addListener("soldierRightAttackAnimate", this::animateRightAttack);
        entity.getEvents().addListener("soldierHighlightedLeftAttackAnimate", this::animateHighlightedLeftAttack);
        entity.getEvents().addListener("soldierHighlightedRightAttackAnimate", this::animateHighlightedRightAttack);
        entity.getEvents().addListener("soldierBackwardLeftMoveAnimate", this::animateBackwardLeftMove);
        entity.getEvents().addListener("soldierBackwardRightMoveAnimate", this::animateBackwardRightMove);
        entity.getEvents().addListener("soldierBackwardHighlightedLeftMoveAnimate", this::animateBackwardHighlightedLeftMove);
        entity.getEvents().addListener("soldierBackwardHighlightedRightMoveAnimate", this::animateBackwardHighlightedRightMove);
    }

    void animateIdle(){
        animator.startAnimation("spearman_forward_left_idle");
    }

    void animateHighlightedIdle(){
        animator.startAnimation("spearman_forward_left_idle_highlighted");
    }

    void animateLeftMove(){
        animator.startAnimation("spearman_forward_left_move");
    }

    void animateRightMove(){
        animator.startAnimation("spearman_forward_right_move");
    }

    void animateHighlightedLeftMove(){
        animator.startAnimation("spearman_forward_left_move_highlighted");
    }

    void animateHighlightedRightMove(){
        animator.startAnimation("spearman_forward_right_move_highlighted");
    }

    void animateLeftAttack(){
        animator.startAnimation("spearman_forward_left_attack");
    }

    void animateRightAttack(){
        animator.startAnimation("spearman_forward_right_attack");
    }

    void animateHighlightedLeftAttack(){
        animator.startAnimation("spearman_forward_left_attack_highlighted");
    }

    void animateHighlightedRightAttack(){
        animator.startAnimation("spearman_forward_right_attack_highlighted");
    }

    void animateBackwardLeftMove(){
        animator.startAnimation("spearman_backward_left_move");
    }

    void animateBackwardRightMove(){
        animator.startAnimation("spearman_backward_right_move");
    }

    void animateBackwardHighlightedLeftMove(){
        animator.startAnimation("spearman_backward_left_move_highlighted");
    }

    void animateBackwardHighlightedRightMove(){
        animator.startAnimation("spearman_backward_right_move_highlighted");
    }
}
