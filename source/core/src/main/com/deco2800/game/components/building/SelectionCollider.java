package com.deco2800.game.components.building;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Used to set a precise selection hitbox on an Entity with a TextureRenderComponent
 */
public class SelectionCollider extends ColliderComponent {
    public SelectionCollider() {
        //Disable hitbox as a ColliderComponent
        super.setSensor(true);
    }

    public SelectionCollider setPoints(float[] points) {
        if (entity.getComponent(TextureRenderComponent.class) == null) {
            return null;
        }

        //Create Texture Region based off this class' TRC
        Texture baseTexture = entity.getComponent(TextureRenderComponent.class).getTextureOG();
        TextureRegion tr = new TextureRegion(baseTexture);

        PolygonRegion region = new PolygonRegion(tr, points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(entity.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();  // Collider shape
        boundingBox.set(vertices);

        super.setShape(boundingBox);
        return this;
    }
}
