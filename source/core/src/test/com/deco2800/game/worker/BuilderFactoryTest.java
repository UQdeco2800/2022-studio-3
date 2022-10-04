package com.deco2800.game.worker;

import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.BuilderComponent;
import com.deco2800.game.worker.type.BuilderFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
public class BuilderFactoryTest {

    private static Entity builder;

    @Test
    static void builderComponentDetected(){
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
