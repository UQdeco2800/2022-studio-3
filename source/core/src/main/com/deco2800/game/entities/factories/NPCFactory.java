package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.npc.GhostAnimationController;
import com.deco2800.game.components.TouchAttackComponent;
//import com.deco2800.game.components.tasks.ChaseTask;
import com.deco2800.game.components.tasks.WanderTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseEntityConfig;
import com.deco2800.game.entities.configs.NPCConfigs;
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
 * "NPCConfigs".
 *
 * <p>If needed, this factory can be separated into more specific factories for entities with
 * similar characteristics.
 */
public class NPCFactory {
  private static final NPCConfigs configs = FileLoader.readClass(NPCConfigs.class, "configs/NPCs.json");


  /**
   * Creates a ghoul entity
   *
   * @return entity
   */
  public static Entity createGhoul() {
    Entity ghoul = createBaseNPC();
    BaseEntityConfig config = configs.ghoul;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);


    ghoul
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack,
            config.baseDefence))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    ghoul.getComponent(AnimationRenderComponent.class).scaleEntity();

    return ghoul;
  }

  /**
   * Creates a demon entity
   *
   * @return entity
   */
  public static Entity createDemon() {
    Entity demon = createBaseNPC();
    BaseEntityConfig config = configs.demon;

    AnimationRenderComponent animator =
        new AnimationRenderComponent(
            ServiceLocator.getResourceService().getAsset("images/ghost.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    demon
        .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
        .addComponent(animator)
        .addComponent(new GhostAnimationController());

    demon.getComponent(AnimationRenderComponent.class).scaleEntity();

    return demon;
  }

  /**
   * Creates a wolf entity
   *
   * @return entity
   */
  public static Entity createWolf() {
    Entity wolf = createBaseNPC();
    BaseEntityConfig config = configs.wolf;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/wolf.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    demon
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
            .addComponent(animator)
            .addComponent(new GhostAnimationController());

    demon.getComponent(AnimationRenderComponent.class).scaleEntity();

    return wolf;
  }

  /**
   * Creates a titan entity
   *
   * @return entity
   */
  public static Entity createTitan() {
    Entity wolf = createBaseNPC();
    BaseEntityConfig config = configs.titan;

    AnimationRenderComponent animator =
            new AnimationRenderComponent(
                    ServiceLocator.getResourceService().getAsset("images/wolf.atlas", TextureAtlas.class));
    animator.addAnimation("angry_float", 0.1f, Animation.PlayMode.LOOP);
    animator.addAnimation("float", 0.1f, Animation.PlayMode.LOOP);
    demon
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
            .addComponent(animator)
            .addComponent(new GhostAnimationController());

    demon.getComponent(AnimationRenderComponent.class).scaleEntity();

    return titan;
  }


  /**
   * Creates a generic NPC to be used as a base entity by more specific NPC creation methods.
   *
   * @return entity
   */
  private static Entity createBaseNPC() {

    AITaskComponent aiComponent =
        new AITaskComponent()
            .addTask(new WanderTask(new Vector2(2f, 2f), 2f));
//            .addTask(new ChaseTask(target, 10, 3f, 4f)); //<- don't know if this is relevant now
    Entity npc =
        new Entity()
            .addComponent(new PhysicsComponent())
            .addComponent(new PhysicsMovementComponent())
            .addComponent(new ColliderComponent())
            .addComponent(new HitboxComponent().setLayer(PhysicsLayer.NPC))
            .addComponent(new TouchAttackComponent(PhysicsLayer.PLAYER, 1.5f))
            .addComponent(aiComponent);

    PhysicsUtils.setScaledCollider(npc, 0.9f, 0.4f);
    return npc;
  }

  private NPCFactory() {
    throw new IllegalStateException("Instantiating static util class");
  }
}


