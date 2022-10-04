package com.deco2800.game.worker.resources;

import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.worker.components.type.StoneComponent;
import com.badlogic.gdx.physics.box2d.BodyDef;

public class MiningCampFactory {
    public static Entity createMiningCamp(){
        AITaskComponent aiComponent = new AITaskComponent().addTask(new CampSpawnStones());
        Entity miningCamp = new Entity()
        .addComponent(new TextureRenderComponent("images/mining_camp_level_one.png"))
        .addComponent(new PhysicsComponent())
        .addComponent(new ColliderComponent())
        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.RESOURCE_NODE))
        .addComponent(new StoneComponent())
        .addComponent(aiComponent);

        miningCamp.getComponent(PhysicsComponent.class).setBodyType(BodyDef.BodyType.StaticBody);
        miningCamp.getComponent(TextureRenderComponent.class).scaleEntity();

        return miningCamp;
    }
}
