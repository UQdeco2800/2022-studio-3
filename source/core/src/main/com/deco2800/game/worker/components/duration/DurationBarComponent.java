package com.deco2800.game.worker.components.duration;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DurationBarComponent extends Component{

    private static final Logger logger = LoggerFactory.getLogger(DurationBarComponent.class);
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("durationBarIdleAnimate", this::animateIdleDurationBar);
        entity.getEvents().addListener("durationBarAnimate", this::animateDurationBar);
    }

    void animateIdleDurationBar(){
        animator.startAnimation("durationBarIdle");
    }

    void animateDurationBar() {
        animator.startAnimation("durationBar");
    }

}
