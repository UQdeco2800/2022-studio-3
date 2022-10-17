package com.deco2800.game.worker;

import com.badlogic.gdx.Gdx;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.BuilderComponent;
import com.deco2800.game.worker.type.BuilderFactory;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class BuilderFactoryTest {

    private static Entity builder;

    @BeforeEach
    void BeforeEach() {
        ServiceLocator.clear();
        TextureAtlas builderAtlas = new TextureAtlas(Gdx.files.internal("images/builder.atlas"));
        ResourceService rs = mock(ResourceService.class);
        when(rs.getAsset(anyString(), eq(TextureAtlas.class))).thenReturn(builderAtlas);
        ServiceLocator.registerResourceService(rs);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    @Test
    public void builderComponentDetected(){
        builder = BuilderFactory.createBuilder();
        BuilderComponent builder_type = builder.getComponent(BuilderComponent.class);
        ForagerComponent forager_type = builder.getComponent(ForagerComponent.class);
        if(builder_type.getIsBuilder() == 1){
            assertTrue(builder_type.getIsBuilder() == 1);
        }
        if(forager_type == null){
            assertTrue(builder_type.getIsBuilder() == 1);
        }
    }
}
