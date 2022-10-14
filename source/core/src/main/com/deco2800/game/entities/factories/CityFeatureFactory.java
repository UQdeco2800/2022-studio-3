package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class CityFeatureFactory {
    /**
     * Creates a base feature entity, with components necessary for colliders
     * @return base feature entity
     */
    private static Entity createBaseFeature() {
        Entity feature = new Entity();
        feature.addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
        return feature;
    }

    /**
     * Creates a lamp entity
     * @return lamp entity
     */
    public static Entity createLamp() {
        Entity lamp = createBaseFeature();
        final float LAMP_SCALE = 1f;
        //Set TextureScalerPoints
        Vector2 leftPoint = new Vector2(96f, 553f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(161f, 576f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(136f, 531f);

        //Create animation component
        TextureAtlas lampAnimationAtlas = ServiceLocator.getResourceService().getAsset("images/lamp.atlas", TextureAtlas.class);
        AnimationRenderComponent lampARC = new AnimationRenderComponent(lampAnimationAtlas);
        lampARC.addAnimation("default", 0.5f, Animation.PlayMode.LOOP);
        lampARC.startAnimation("default");

        //Create map component
        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.OLIVE);

        //Set base texture for TextureScaler
        Texture baseTexture = ServiceLocator.getResourceService()
                .getAsset("images/lampDefault.png", Texture.class);

        //Add components
        lamp.addComponent(lampARC)
                .addComponent(new TextureScaler(leftPoint, maxX, maxY, baseTexture))
                .addComponent(mp);

        //Precisely scale width to one tile long
        lamp.getComponent(TextureScaler.class).setPreciseScale(LAMP_SCALE, true);

        //Set collider
        float[] points = new float[] {      // Four vertices
                96f, 553f,       // Vertex 0       3--2
                161f, 576f,     // Vertex 1      /  /
                199f, 544f,     // Vertex 2     /  /
                136f, 531f       // Vertex 3    0--1
        };
        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(baseTexture), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(lamp.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();  // Collider shape
        boundingBox.set(vertices);
        lamp.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return lamp;
    }

    /**
     * Creates a water feature entity
     * @return water feature entity
     */
    public static Entity createWaterFeature() {
        Entity wf = createBaseFeature();
        final float FEATURE_SCALE = 1f;
        //Set TextureScalerPoints
        Vector2 leftPoint = new Vector2(9f, 104f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(67f, 125f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(69f, 65f);

        //Create animation component
        TextureAtlas wfAnimationAtlas = ServiceLocator.getResourceService().getAsset("images/waterfeature.atlas", TextureAtlas.class);
        AnimationRenderComponent wfARC = new AnimationRenderComponent(wfAnimationAtlas);
        wfARC.addAnimation("default", 0.15f, Animation.PlayMode.LOOP);
        wfARC.startAnimation("default");

        //Create map component
        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.BLUE);

        Texture baseTexture = ServiceLocator.getResourceService()
                .getAsset("images/waterFeatureDefault.png", Texture.class);

        wf.addComponent(wfARC)
                .addComponent(new TextureScaler(leftPoint, maxX, maxY, baseTexture))
                .addComponent(mp);

        //Precisely scale width to one tile long
        wf.getComponent(TextureScaler.class).setPreciseScale(FEATURE_SCALE, true);

        //Set collider
        float[] points = new float[] {      // Four vertices
                1f, 72f,       // Vertex 0       3--2
                67f, 112f,     // Vertex 1      /  /
                133f, 82f,     // Vertex 2     /  /
                72f, 33f       // Vertex 3    0--1
        };
        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(baseTexture), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(wf.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();  // Collider shape
        boundingBox.set(vertices);
        wf.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return wf;
    }

    public static Entity createCityBush() {
        return null;
    }
}
