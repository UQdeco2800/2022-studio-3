package com.deco2800.game.worker.type;

import com.deco2800.game.worker.WorkerFactory;
import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.EnemyDetectionComponent;
import com.deco2800.game.worker.components.MinerAnimationController;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MinerFactory {

    public static Entity createMiner() {

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/miner.atlas", TextureAtlas.class));

        animator.addAnimation("miner_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_forward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_forward_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_back_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_back_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_forward_left_action", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_forward_right_action", 0.1f, Animation.PlayMode.LOOP);

        Entity newMiner = WorkerFactory.createWorker()
            .addComponent(new MinerComponent())
            .addComponent(new CollectStatsComponent(2))
            .addComponent(animator)
            .addComponent(new MinerAnimationController())
            .addComponent(new EnemyDetectionComponent());
        newMiner.getComponent(AnimationRenderComponent.class).scaleEntity();
        newMiner.scaleHeight(2.0f);
        newMiner.scaleWidth(2.0f);
        return newMiner;
    }
    
}
