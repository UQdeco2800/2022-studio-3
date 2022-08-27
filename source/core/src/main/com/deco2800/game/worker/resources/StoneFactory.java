package com.deco2800.game.worker.resources;

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
        return new Entity()
            .addComponent(new TextureRenderComponent("images/mud.png"))
            .addComponent(new PhysicsComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RESOURCE_NODE))
            .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.iron));
    } 
}