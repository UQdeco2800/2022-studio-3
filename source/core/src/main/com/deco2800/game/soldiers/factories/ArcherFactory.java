package com.deco2800.game.soldiers.factories;


import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.EntityType;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.building.AttackListener;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendlyunits.AvatarIconComponent;
import com.deco2800.game.components.tasks.rangedAttackTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.animation.HopliteAnimationController;
import com.deco2800.game.soldiers.type.HopliteComponent;
import com.deco2800.game.worker.WorkerFactory;
import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.EnemyDetectionComponent;
import com.deco2800.game.worker.components.ForagerAnimationController;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.badlogic.gdx.graphics.g2d.Animation;

public class ArcherFactory {

    private static final BaseUnitConfig stats = FileLoader.readClass(UnitConfigs.class, "configs/units.json").archer;

    public static Entity createArcher(Entity target, GameArea gameArea) {
        Entity archer1 = new Entity()
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SOLDIER))
                .addComponent(new FriendlyComponent())
                .addComponent(new HealthBarComponent(EntityType.FRIENDLY));

        AITaskComponent aiComponent = new AITaskComponent()
                .addTask(new rangedAttackTask(target, 4, 10, 2000f));

        archer1.addComponent(new TextureRenderComponent("images/archerstatic.png"))
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.baseDefence))
                .addComponent(aiComponent)
                .addComponent(new AttackListener(target, gameArea));
        archer1.scaleHeight(0.8f);
        archer1.scaleWidth(0.5f);
        return archer1;
    }

    
    private ArcherFactory() {
        throw new IllegalStateException("Utility class");
    }
}
