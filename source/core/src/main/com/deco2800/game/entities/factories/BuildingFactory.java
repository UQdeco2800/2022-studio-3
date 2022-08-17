package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

public class BuildingFactory {
    public static Entity createWall() {
        Entity wall = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new TextureRenderComponent("images/wall.png"));
        wall.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        wall.getComponent(TextureRenderComponent.class).scaleEntity();
        wall.scaleWidth(1f);
        PhysicsUtils.setScaledCollider(wall, 1f, 1f);
        return wall;
    }

    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
