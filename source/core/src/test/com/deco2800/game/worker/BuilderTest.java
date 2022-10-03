package com.deco2800.game.worker;

import com.deco2800.game.worker.components.MinerAnimationController;
import com.deco2800.game.worker.components.BuildingFixComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.worker.components.type.BuilderComponent;
import com.deco2800.game.worker.resources.ResourceConfig;
import com.deco2800.game.worker.type.BuilderFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(GameExtension.class)
public class BuilderTest {

    @Test
    void builderComponentDetected(){
        Entity builder = BuilderFactory.createBuilder();
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
