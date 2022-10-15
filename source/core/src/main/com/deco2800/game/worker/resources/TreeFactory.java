package com.deco2800.game.worker.resources;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.type.TreeComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.services.ServiceLocator;



public class TreeFactory {
    private static final ResourceConfig stats = FileLoader.readClass(ResourceConfig.class, "configs/tree.json");

    public static Entity createTree(){

        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/tree_.atlas", TextureAtlas.class));
        animator.addAnimation("tree_damaged", 0.5f, Animation.PlayMode.NORMAL);
        animator.addAnimation("tree_idle", 1f, Animation.PlayMode.LOOP);
        animator.addAnimation("tree_destroyed", 1f, Animation.PlayMode.LOOP);


        MapComponent mc = new MapComponent();
        mc.display();
        mc.setDisplayColour(Color.FOREST);
        Entity tree = new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RESOURCE_NODE))
            .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal))
            .addComponent(new TreeComponent())
            // .addComponent(new TextureScaler(new Vector2(0f, 128f), new Vector2(0f, 0f), new Vector2(128f, 0f)))
            .addComponent(mc)
            .addComponent(animator);
        tree.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        tree.getComponent(AnimationRenderComponent.class).startAnimation("tree_idle");

        return tree;
    }


}
