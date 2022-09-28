package com.deco2800.game.worker.type;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.worker.components.BuilderAnimationController;
import com.deco2800.game.worker.components.type.BuilderComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.WorkerConfig;
import com.deco2800.game.worker.WorkerFactory;

public class BuilderFactory {
    private static final WorkerConfig stats = FileLoader.readClass(WorkerConfig.class, "configs/worker.json");

    public static Entity createBuilder() {
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

        Entity builder = WorkerFactory.createWorker()
                .addComponent(new BuilderComponent())
                .addComponent(animator)
                .addComponent(new BuilderAnimationController());
        builder.getComponent(AnimationRenderComponent.class).scaleEntity();
        builder.scaleHeight(1.5f);
        builder.scaleWidth(1.5f);
        return builder;
    }
}
