package com.deco2800.game.worker.components.duration;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoneAnimationController extends Component{
    private static final Logger logger = LoggerFactory.getLogger(StoneAnimationController.class);
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("miningAnimate", this::animateMining);
    }

    void animateMining(){
        logger.info("[+] mining triggered");
        animator.startAnimation("duration-bar-0");
        animator.startAnimation("duration-bar-1");
        animator.startAnimation("duration-bar-2");
        animator.startAnimation("duration-bar-3");
        animator.startAnimation("duration-bar-4");
        logger.info("[+] mining trigger finished");
    }
}
