package com.deco2800.game.entities.factories;

import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

/**
 * Factory to create resource entities.
 *
 * <p>Each resource entity type should have a creation method that returns a corresponding entity.
 */
public class ResourceFactory {

    /**
     * Creates a tree resource.
     * @return entity
     */
    public static Entity createTree() {
        Entity tree =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/tree.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        tree.getComponent(TextureRenderComponent.class).scaleEntity();
        tree.scaleHeight(2.5f);
        PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
        return tree;
    }

    /**
     * Creates a rock resource entity
     * @param width Wall width in world units
     * @param height Wall height in world units
     * @return Wall entity of given width and height
     */
    public static Entity createRock(float width, float height) {
        Entity wall = new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        Entity rock =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/RockTemp.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        rock.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        rock.getComponent(TextureRenderComponent.class).scaleEntity();
        rock.scaleHeight(2.5f);
        //this allows just the base to be uncollideable
        //PhysicsUtils.setScaledCollider(rock, 0.5f, 0.2f);
        //this allows the image to be collideable anywhere
        rock.setScale(width, height);
        return rock;
    }

    private ResourceFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
