package com.deco2800.game.worker.resources;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoneAnimation extends Component{
    private static final Logger logger = LoggerFactory.getLogger(StoneAnimation.class);
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("miningAnimate", this::animateMining);
    }

    void animateMining(){
        logger.info("[+] mining triggered");
        animator.startAnimation("duration_time_bar");
    }
}
