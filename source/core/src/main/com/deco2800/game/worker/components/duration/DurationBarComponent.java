package com.deco2800.game.worker.components.duration;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class DurationBarComponent extends Component{
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("duration-bar-25", this::durationBar25);
        entity.getEvents().addListener("duration-bar-50", this::durationBar50);
        entity.getEvents().addListener("duration-bar-75", this::durationBar75);
        entity.getEvents().addListener("duration-bar-100", this::durationBar100);
    }

    void durationBar25(){
        animator.startAnimation("duration-bar-25");
    }

    void durationBar50(){
        animator.startAnimation("duration-bar-50");
    }

    void durationBar75(){
        animator.startAnimation("duration-bar-75");
    }

    void durationBar100(){
        animator.startAnimation("duration-bar-100");
    }

}
