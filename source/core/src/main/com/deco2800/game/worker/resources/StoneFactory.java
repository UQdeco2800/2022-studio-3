package com.deco2800.game.worker.resources;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.physics.components.HitboxComponent;

public class StoneFactory {
    private static final ResourceConfig stats = FileLoader.readClass(ResourceConfig.class, "configs/stone.json");

    public static Entity createStone() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/duration_bar.atlas", TextureAtlas.class));

        animator.addAnimation("duration_time_bar", 0.2f, Animation.PlayMode.LOOP);

        Entity stone = new Entity()
            .addComponent(new TextureRenderComponent("images/mud.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(animator)
            .addComponent(new StoneAnimation())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RESOURCE_NODE))
            .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal));
        
        return stone;
    } 
}