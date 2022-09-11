package com.deco2800.game.worker.components.duration;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class DurationBarFactory {
    public static Entity createDurationBar(){
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/duration_bar/duration-bar.atlas", TextureAtlas.class));
        
        animator.addAnimation("durationBarIdle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("durationBar", 0.1f, Animation.PlayMode.LOOP);
        Entity newDurationBar = new Entity()
            .addComponent(animator)
            .addComponent(new DurationBarComponent());
        return newDurationBar;
    }
}
