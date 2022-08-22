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
     * @param height scale of the tree (default value is 2.5f)
     * @return entity
     */
    public static Entity createTree(float height) {
        Entity tree =
                new Entity()
                        .addComponent(new TextureRenderComponent("images/tree.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        tree.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        tree.getComponent(TextureRenderComponent.class).scaleEntity();
        tree.scaleHeight(height);
        PhysicsUtils.setScaledCollider(tree, 0.5f, 0.2f);
        return tree;
    }

    /**
     * Creates a rock resource entity
     * @param height scaled heigh of the rock entity
     * @return entity of a rock
     */
    public static Entity createRock(float height) {
        Entity rock =
                new Entity()
                        //this texture path is incorrect and requires a rock png
                        .addComponent(new TextureRenderComponent("images/mud.png"))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));

        rock.getComponent(PhysicsComponent.class).setBodyType(BodyType.StaticBody);
        rock.getComponent(TextureRenderComponent.class).scaleEntity();
        rock.scaleHeight(height);
        PhysicsUtils.setScaledCollider(rock, 0.5f, 2.5f);
        return rock;
    }

    private ResourceFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
