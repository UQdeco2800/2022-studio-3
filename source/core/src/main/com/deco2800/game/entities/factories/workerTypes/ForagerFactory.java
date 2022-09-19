package com.deco2800.game.entities.factories.workerTypes;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;

import com.deco2800.game.components.worker.CollectStatsComponent;
import com.deco2800.game.components.worker.type.ForagerComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.entities.configs.WorkerConfig;
import com.deco2800.game.entities.factories.WorkerFactory;
import com.deco2800.game.components.worker.ForagerAnimationController;

public class ForagerFactory {
    private static final WorkerConfig stats = FileLoader.readClass(WorkerConfig.class, "configs/worker.json");

    public static Entity createForager() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/forager_forward.atlas", TextureAtlas.class));

        animator.addAnimation("forager_forward_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("forager_forward_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("foragerActionRight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("foragerActionLeft", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("foragerRight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("foragerLeft", 0.1f, Animation.PlayMode.LOOP);

        Entity forager = WorkerFactory.createWorker()
                .addComponent(new ForagerComponent())
                .addComponent(new CollectStatsComponent(2))
                .addComponent(animator)
                .addComponent(new ForagerAnimationController());
        forager.getComponent(AnimationRenderComponent.class).scaleEntity();
        forager.scaleHeight(1.5f);
        forager.scaleWidth(1.5f);
        return forager;
    }
}
