package com.deco2800.game.components.friendly;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.friendly.SoldierUnit;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.SoldierConfig;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

public class SoldierFactory {
    private static final SoldierConfig config =
            FileLoader.readClass(SoldierConfig.class, "configs/soldier.json");

    public static Entity createArcher(GameArea gameArea) {
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f));

        Entity archer = new Entity()
                .addComponent(new TextureRenderComponent("images/archer_sprite_sheet.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(archer, 1f, 1f);
        archer.scaleHeight(2f);
        return archer;
    }

    public static Entity createHoplite(GameArea gameArea) {
        //AlienMonsterConfig config = configs.alienMonster;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f));
        //.addTask(new AttackTask(target, 2, 10, 10f));

        Entity hoplite = new Entity()
                .addComponent(new TextureRenderComponent("images/Hoplite.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SOLDIER))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(hoplite, 1f, 1f);
        hoplite.scaleHeight(2f);
        return hoplite;
    }

    public static Entity createSpearman(GameArea gameArea) {
        //AlienMonsterConfig config = configs.alienMonster;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f));
        //.addTask(new AttackTask(target, 2, 10, 10f));

        Entity spearman = new Entity()
                .addComponent(new TextureRenderComponent("images/spearman.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SOLDIER))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(spearman, 1f, 1f);
        spearman.scaleHeight(2f);
        return spearman;
    }

    public static Entity createSwordsman(GameArea gameArea) {
        //AlienMonsterConfig config = configs.alienMonster;
        AITaskComponent aiComponent =
                new AITaskComponent()
                        .addTask(new WanderTask(new Vector2(3f, 2f), 0f));
        //.addTask(new AttackTask(target, 2, 10, 10f));

        Entity swordsman = new Entity()
                .addComponent(new TextureRenderComponent("images/swordsman_sprite_sheet.png"))
                .addComponent(new PhysicsComponent())
                .addComponent(new PhysicsMovementComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.SOLDIER))
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE))
                .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 0f))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(aiComponent);

        PhysicsUtils.setScaledCollider(swordsman, 1f, 1f);
        swordsman.scaleHeight(2f);
        return swordsman;
    }


}
