package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.EntityDirectionComponent;
import com.deco2800.game.components.npc.EnemyAnimationController;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.TouchAttackComponent;
//import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.*;
import com.deco2800.game.entities.EnemyEntity;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.EnemyConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Factory to create non-playable character (NPC) entities with predefined components.
 *
 * <p>Each NPC entity type should have a creation method that returns a corresponding entity.
 * Predefined entity properties can be loaded from configs stored as json files which are defined in
 * "EnemyConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class EnemyFactory {
  private static final EnemyConfigs configs = FileLoader.readClass(EnemyConfigs.class, "configs/Enemies.json");
  private static final String MOVE_WEST = "move-west";
  private static final String MOVE_EAST = "move-east";
  private static final String MOVE_NORTH = "move-north";
  private static final String MOVE_SOUTH = "move-south";

  private static final String ATTACK_NORTH = "attack-north";
  private static final String ATTACK_SOUTH = "attack-south";
  private static final String ATTACK_EAST = "attack-east";
  private static final String ATTACK_WEST = "attack-west";

  /**
   * Creates a Blue Joker enemy entity
   *
   * @return Blue Joker enemy entity
   */
  public static Entity createBlueJoker(AtlantisTerrainFactory terrainFactory) {
    Entity blueJoker = createBaseEnemy(terrainFactory);
    BaseUnitConfig config = configs.blueJoker;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/blue_joker.atlas", TextureAtlas.class));

    animator.addAnimation(MOVE_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_WEST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("default", 1f, Animation.PlayMode.NORMAL);

    animator.addAnimation(ATTACK_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_WEST, 0.1f, Animation.PlayMode.LOOP);

    blueJoker
              .addComponent(new CombatStatsComponent(config.troops, config.health, config.baseAttack,
                                                     config.baseDefence, config.landSpeed, config.range))
              .addComponent(animator)
              .addComponent(new EnemyAnimationController());

    blueJoker .getComponent(AnimationRenderComponent.class).scaleEntity();

    return blueJoker;
  }

  /**
   * Creates a Snake entity
   *
   * @return Snake entity
   */
  public static Entity createSnake(AtlantisTerrainFactory terrainFactory) {
    Entity snake = createBaseEnemy(terrainFactory);
    BaseUnitConfig config = configs.snake;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/snake2.0.atlas", TextureAtlas.class));

    animator.addAnimation(MOVE_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_WEST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.NORMAL);

    animator.addAnimation(ATTACK_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_WEST, 0.1f, Animation.PlayMode.LOOP);
    snake
        .addComponent(new CombatStatsComponent(config.troops, config.health, config.baseAttack,
                                               config.baseDefence, config.landSpeed, config.range))
        .addComponent(animator)
        .addComponent(new EnemyAnimationController());

    snake.getComponent(AnimationRenderComponent.class).scaleEntity();

    return snake;
  }

  /**
   * Creates a wolf entity
   *
   * @return entity
   */
  public static Entity createWolf(AtlantisTerrainFactory terrainFactory) {
    Entity wolf = createBaseEnemy(terrainFactory);
    BaseUnitConfig config = configs.wolf;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(ServiceLocator.getResourceService().getAsset("images/newwolf.atlas", TextureAtlas.class));
    animator.addAnimation(MOVE_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_WEST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.NORMAL);

    animator.addAnimation(ATTACK_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_WEST, 0.1f, Animation.PlayMode.LOOP);
    wolf
            .addComponent(new CombatStatsComponent(config.troops, config.health, config.baseAttack,
                                                   config.baseDefence, config.landSpeed, config.range))
            .addComponent(animator)
            .addComponent(new EnemyAnimationController());

    wolf.getComponent(AnimationRenderComponent.class).scaleEntity();

    return wolf;
  }

  /**
   * Creates a titan entity
   *
   * @return entity
   */
  public static Entity createTitan(AtlantisTerrainFactory terrainFactory) {
    Entity titan = createBaseEnemy(terrainFactory);
    BaseUnitConfig config = configs.titan;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/titan.atlas", TextureAtlas.class));
    animator.addAnimation(MOVE_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_WEST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(MOVE_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("default", 0.1f, Animation.PlayMode.NORMAL);

    animator.addAnimation(ATTACK_NORTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_SOUTH, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_EAST, 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation(ATTACK_WEST, 0.1f, Animation.PlayMode.LOOP);
    titan
            .addComponent(new CombatStatsComponent(config.troops, config.health, config.baseAttack,
                                                   config.baseDefence, config.landSpeed, config.range))
            .addComponent(animator)
            .addComponent(new EnemyAnimationController());

    titan.getComponent(AnimationRenderComponent.class).scaleEntity();

    return titan;
  }


  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseEnemy(AtlantisTerrainFactory terrainFactory) {

    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new EnemyMovement(terrainFactory));
    Entity npc =
        new EnemyEntity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent)
            .addComponent(new EntityDirectionComponent());

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    System.out.println(npc.getClass());
    return npc;
  }

  private EnemyFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}

