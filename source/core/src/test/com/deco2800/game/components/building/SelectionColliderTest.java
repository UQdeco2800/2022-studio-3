package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.BuildingFactory;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
public class SelectionColliderTest {
    //Define points for Library building's selection hitbox
    float[] selectionPoints = new float[] {
            78f, 355f,
            195f, 412f,
            367f, 411f,
            444f, 370f,
            431f, 175f,
            205f, 120f,
            111f, 160f,
            78f, 220f
    };

    @BeforeEach
    void beforeEach() {
        ServiceLocator.clear();
        //Store required texture
        Texture library = new Texture(Gdx.files.internal("images/library.png"));
        //Mock resource service to return desired textures
        ResourceService rs = mock(ResourceService.class);
        when(rs.getAsset(anyString(), eq(Texture.class))).thenReturn(library);
        //Register necessary services;
        ServiceLocator.registerResourceService(rs);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    /**
     * Returns the world point relative to a pixel point on the library's texture
     * @param point pixel point in image represented as a Vector2
     * @param entity entity with a TextureRenderComponent
     * @return Vector2 representation of a world point
     */
    private Vector2 pixelToWorldPoint(Vector2 point, Entity entity) {
        float[] points = new float[] {
                point.x, point.y
        };
        Texture libraryTexture = ServiceLocator.getResourceService().getAsset("a", Texture.class);
        PolygonRegion region = new PolygonRegion(new TextureRegion(libraryTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        Vector2 pointPosition = new Vector2(translatedCoords[0], translatedCoords[1])
                .scl(entity.getScale());

        return pointPosition;
    }

    /**
     * Creates a fixture for a given entity
     * @param entity entity that requires a fixture
     * @param points points reflecting the selection hitbox of the fixture
     * @return fixture
     */
    private Fixture makeFixture(Entity entity, float[] points) {
        FixtureDef fixtureDef = new FixtureDef();
        Body physBody = new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody).getBody();
        //Create Texture Region based off Library texture
        Texture baseTexture = ServiceLocator.getResourceService().getAsset("a", Texture.class);
        TextureRegion tr = new TextureRegion(baseTexture);

        PolygonRegion region = new PolygonRegion(tr, points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(entity.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();  // Collider shape
        boundingBox.set(vertices);
        fixtureDef.shape = boundingBox;

        return physBody.createFixture(fixtureDef);
    }

    /**
     * Tests if a pixel that is within the Library's selection hitbox will trigger a selection when clicked
     */
    @Test
    public void correctPixelTest() {
        Vector2 correctPixel =  new Vector2(262f, 267f);
        Entity library = BuildingFactory.createLibrary();
        //Convert this pixel to a world point relative to the library texture and scale
        Vector2 worldPoint = pixelToWorldPoint(correctPixel, library);
        //Create a fixture for the library as it is made in game
        Fixture libraryFixture = makeFixture(library, selectionPoints);
        //Test that the point is within the library's selection hitbox
        boolean outcome =  libraryFixture.testPoint(worldPoint);
        //Assert that the library is selected
        assertTrue(outcome);
    }

    /**
     * Tests if a pixel outside of the Library's selection hitbox will trigger a selection
     */
    @Test
    public void invalidPixelTest() {
        Vector2 incorrectPixel =  new Vector2(457f, 454f);
        Entity library = BuildingFactory.createLibrary();
        //Convert this pixel to a world point relative to the library texture and scale
        Vector2 worldPoint = pixelToWorldPoint(incorrectPixel, library);
        //Create a fixture for the library as it is made in game
        Fixture libraryFixture = makeFixture(library, selectionPoints);
        //Test that the point is within the library's selection hitbox
        boolean outcome = libraryFixture.testPoint(worldPoint);
        //Assert that the library  is not selected
        assertFalse("Library should not be selected here", outcome);
    }
}
