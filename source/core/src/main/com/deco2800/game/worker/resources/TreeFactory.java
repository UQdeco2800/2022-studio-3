package com.deco2800.game.worker.resources;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.physics.components.HitboxComponent;

public class TreeFactory {
    private static final ResourceConfig stats = FileLoader.readClass(ResourceConfig.class, "configs/tree.json");

    public static Entity createTree(){
        
        Entity tree = new Entity()
            .addComponent(new TextureRenderComponent("images/tree.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RESOURCE_NODE))
            .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal));
        tree.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        tree.getComponent(TextureRenderComponent.class).scaleEntity();
        return tree;
    }
}
