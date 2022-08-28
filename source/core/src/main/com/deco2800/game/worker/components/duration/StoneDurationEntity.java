package com.deco2800.game.worker.components.duration;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.entities.Entity;

public class StoneDurationEntity {
    public static Entity createStoneDuration(){
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/duration_bar/duration_bar.atlas", TextureAtlas.class));

        animator.addAnimation("duration-bar-0", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("duration-bar-1", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("duration-bar-2", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("duration-bar-3", 0.2f, Animation.PlayMode.NORMAL);
        animator.addAnimation("duration-bar-4", 0.2f, Animation.PlayMode.NORMAL);

        Entity stone = new Entity()
            .addComponent(animator)
            .addComponent(new StoneAnimationController());
        
        return stone;
    }
}
