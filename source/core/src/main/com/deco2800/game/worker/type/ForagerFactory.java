package com.deco2800.game.worker.type;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.EnemyDetectionComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.WorkerFactory;
import com.deco2800.game.worker.components.ForagerAnimationController;
import com.deco2800.game.worker.components.ResourceCollectComponent;
 
public class ForagerFactory {

    public static Entity createForager() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/forager.atlas", TextureAtlas.class));

        animator.addAnimation("forager_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_forward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_forward_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_back_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_back_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_forward_left_action", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_forward_right_action", 0.1f, Animation.PlayMode.LOOP);

        Entity forager = WorkerFactory.createWorker()
                .addComponent(new ForagerComponent())
                .addComponent(new CollectStatsComponent(2))
                .addComponent(new ResourceCollectComponent(PhysicsLayer.RESOURCE_NODE))
                .addComponent(animator)
                .addComponent(new ForagerAnimationController())
                .addComponent(new EnemyDetectionComponent());
        forager.getComponent(AnimationRenderComponent.class).scaleEntity();
        forager.scaleHeight(2.0f);
        forager.scaleWidth(2.0f);
        return forager;
    }
}
