package com.deco2800.game.worker;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.resources.ResourceConfig;
import com.deco2800.game.physics.components.HitboxComponent;

public class BaseFactory {
    private static final ResourceConfig stats = FileLoader.readClass(ResourceConfig.class, "configs/base.json");
    public static Entity createBase() {
        Entity newBase = new Entity()
            .addComponent(new TextureRenderComponent("images/base.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.iron));
        newBase.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        newBase.getComponent(TextureRenderComponent.class).scaleEntity();
        newBase.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(newBase, 0.5f, 0.2f);
        return newBase;
    }
}

