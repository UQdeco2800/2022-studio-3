package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

public class BuildingFactory {
    // Default physical collider of buildings made through building factory
    private static final float COLLIDER_SCALE = 0.9f;
    public static Entity createWall() {
        Entity wall = new Entity()
            .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
            .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
            .addComponent(new TextureRenderComponent("images/wall.png")); // dummy image made by Sylvia
        wall.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        wall.getComponent(TextureRenderComponent.class).scaleEntity();
        wall.scaleWidth(1f);
        PhysicsUtils.setScaledCollider(wall, 1f, 1f);
        return wall;
    }

    public static Entity createTownHall() {
        Entity townHall = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/Base.png"));
        townHall.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        townHall.getComponent(TextureRenderComponent.class).scaleEntity();
        townHall.scaleWidth(4f);
        PhysicsUtils.setScaledCollider(townHall, 0.9f, 0.9f);
        return townHall;
    }

    public static Entity createBarracks() {
        Entity barracks = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/barracks atlantis.png"));
        barracks.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        barracks.getComponent(TextureRenderComponent.class).scaleEntity();
        barracks.scaleWidth(2f);
        PhysicsUtils.setScaledCollider(barracks, COLLIDER_SCALE, COLLIDER_SCALE);
        return barracks;
    }

    public static Entity createBarracksMedieval() {
        Entity barracks = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TextureRenderComponent("images/barracks medieval.png"));
        barracks.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        barracks.getComponent(TextureRenderComponent.class).scaleEntity();
        barracks.scaleWidth(2f);
        PhysicsUtils.setScaledCollider(barracks, COLLIDER_SCALE, COLLIDER_SCALE);
        return barracks;
    }


    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
