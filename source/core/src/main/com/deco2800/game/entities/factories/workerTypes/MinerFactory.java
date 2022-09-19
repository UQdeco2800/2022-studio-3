package com.deco2800.game.entities.factories.workerTypes;

import com.deco2800.game.entities.configs.WorkerConfig;
import com.deco2800.game.entities.factories.WorkerFactory;
import com.deco2800.game.components.worker.CollectStatsComponent;
import com.deco2800.game.components.worker.EnemyDetectionComponent;
import com.deco2800.game.components.worker.MinerAnimationController;
import com.deco2800.game.components.worker.type.MinerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

public class MinerFactory {
    
    private static final WorkerConfig stats = FileLoader.readClass(WorkerConfig.class, "configs/worker.json");

    public static Entity createMiner() {

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/miner_forward.atlas", TextureAtlas.class));

        animator.addAnimation("miner_forward_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("miner_forward_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("minerActionLeft", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("minerActionRight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("minerRight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("minerLeft", 0.1f, Animation.PlayMode.LOOP);

        Entity newMiner = WorkerFactory.createWorker()
            .addComponent(new MinerComponent())
            .addComponent(new CollectStatsComponent(2))
            .addComponent(animator)
            .addComponent(new MinerAnimationController())
            .addComponent(new EnemyDetectionComponent());
        newMiner.getComponent(AnimationRenderComponent.class).scaleEntity();
        newMiner.scaleHeight(1.5f);
        newMiner.scaleWidth(1.5f);
        return newMiner;
    }
    
}
