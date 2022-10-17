package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.ai.tasks.AITaskComponent;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.EntityType;
import com.deco2800.game.components.HealthBarComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendly.TroopContainerComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.components.friendlyunits.controller.ArcherAnimationController;
import com.deco2800.game.components.friendlyunits.controller.HopliteAnimationController;
import com.deco2800.game.components.friendlyunits.controller.SpearmanAnimationController;
import com.deco2800.game.components.friendlyunits.controller.SwordsmanAnimationController;
import com.deco2800.game.components.friendlyunits.task.UnitIdleTask;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UnitType;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.physics.components.PhysicsMovementComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UnitFactory {
    private static final UnitConfigs stats =
            FileLoader.readClass(UnitConfigs.class, "configs/units.json");


    public static Entity createExampleUnit() {
        ArrayList<Entity> troops = new ArrayList<>();
        for (int i = 0; i < stats.example.troops; i++) {
            //TODO: add animations to this construction
            Entity troop = new Entity().addComponent(new TextureRenderComponent(
                            "images/simpleman.png"))
                    .addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ALL))
                    .addComponent(new CombatStatsComponent(stats.example.health,
                            stats.example.baseAttack,
                            stats.example.baseDefence))
                    .addComponent(new TouchAttackComponent(PhysicsLayer.NPC));
            PhysicsUtils.setScaledCollider(troop, 0.2f, 1f);
            troop.getComponent(ColliderComponent.class).setDensity(1f);
            troop.getComponent(TextureRenderComponent.class).scaleEntity();
            troop.setScale(0.5f, 0.5f);
            troops.add(troop);
        }
        // TODO: add control to unit entity
        Entity unit =
                new Entity().addComponent(new TroopContainerComponent(troops,
                        stats.example.movementRadius))
                        .addComponent(new PhysicsComponent())
                        .addComponent(new ColliderComponent())
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER))
                        .addComponent(new FriendlyComponent());
        PhysicsUtils.setRadiusCollider(unit, stats.example.movementRadius);
        return unit;
    }

    private static Entity createRangedTroop(UnitType type) {
        BaseUnitConfig stats;

        switch (type) {
            case HOPLITE -> {
                stats = UnitFactory.stats.archer;
            }
            default -> {
                stats = new BaseUnitConfig();
            }
        }
        return new Entity().addComponent(
                        new CombatStatsComponent(stats.health,
                                stats.baseAttack,
                                stats.baseDefence))
                        .addComponent(new HealthBarComponent(EntityType.FRIENDLY))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.NPC));
    }

    private static Entity createMeleeTroop(UnitType type) {
        BaseUnitConfig stats;

        // first find our unit stats
        switch (type) {
            case HOPLITE -> {
                stats = UnitFactory.stats.hoplite;
            }
            case SPEARMAN -> {
                stats = UnitFactory.stats.spearmen;
            }
            case SWORDSMAN -> {
                stats = UnitFactory.stats.swordsman;
            }
            default -> {
                stats = new BaseUnitConfig();
            }
        }

        return new Entity().addComponent(
                        new CombatStatsComponent(stats.health,
                                stats.baseAttack,
                                stats.baseDefence))
                        .addComponent(new HealthBarComponent(EntityType.FRIENDLY))
                        .addComponent(new TouchAttackComponent(PhysicsLayer.NPC));
    }

    public static Entity createUnit(UnitType type) {

        ResourceService resources = ServiceLocator.getResourceService();

        // parameters for building animation strings
        Map<UnitType, String> unitTypeStrings = Map.ofEntries(
                Map.entry(UnitType.HOPLITE, "hoplite"),
                Map.entry(UnitType.ARCHER, "archer"),
                Map.entry(UnitType.SPEARMAN, "spearman"),
                Map.entry(UnitType.SWORDSMAN, "swordsman")
        );

        List<String> animation_actions = List.of(
                        "move",
                        "idle",
                        "attack");

        List<String> animation_directions = List.of(
                "forward_left",
                "forward_right",
                "backward_right",
                "backward_left");

        String atlasPath = "images/" + unitTypeStrings.get(type) + ".atlas";
        Animation.PlayMode loop = Animation.PlayMode.LOOP; // for shorthand
        float framePeriod = 0.2f; // TODO: Make constant somewhere

        Entity unit = new Entity();
        ArrayList<Entity> troops = new ArrayList<>();

        // how many troops are we making?
        BaseUnitConfig unitStats;
        switch (type) {
            case HOPLITE -> {
                unitStats = stats.hoplite;
            }
            case SPEARMAN -> {
                unitStats = stats.spearmen;
            }
            case SWORDSMAN -> {
                unitStats = stats.swordsman;
            }
            case ARCHER -> {
                unitStats = stats.archer;
            }
            default -> unitStats = stats.example;
        }

        for (int i = 0; i < unitStats.troops; i++) {
            Entity troop;
            // AnimationRenderComponent animator =
            //         new AnimationRenderComponent(resources.getAsset(atlasPath,
            //                 TextureAtlas.class));
            // for (String direction: animation_directions) {
            //     for (String action: animation_actions) {
            //         String animationName = unitTypeStrings.get(type) + "_" +
            //                 direction + "_" + action;
            //         if (action.equals("attack")) {
            //             // attacks don't have enough frames for this
            //             animator.addAnimation(animationName, 0.5f, loop);
            //         } else {
            //             animator.addAnimation(animationName, framePeriod, loop);
            //         }
            //     }
            // }
            AnimationRenderComponent animator = new AnimationRenderComponent(
                resources.getAsset(atlasPath, TextureAtlas.class)
            );

            if (type == UnitType.ARCHER) {
                troop = createRangedTroop(UnitType.ARCHER);

                animator.addAnimation("archer_forward_left_idle", framePeriod, loop);
                animator.addAnimation("archer_forward_left_idle_highlight", framePeriod, loop);
                animator.addAnimation("archer_forward_left_move", framePeriod, loop);
                animator.addAnimation("archer_forward_right_move", framePeriod, loop);
                animator.addAnimation("archer_forward_left_move_highlight", framePeriod, loop);
                animator.addAnimation("archer_forward_right_move_highlight", framePeriod, loop);
                animator.addAnimation("archer_forward_left_attack", framePeriod, loop);
                animator.addAnimation("archer_forward_right_attack", framePeriod, loop);
                animator.addAnimation("archer_forward_left_attack_highlight", framePeriod, loop);
                animator.addAnimation("archer_forward_right_attack_highlight", framePeriod, loop);
                animator.addAnimation("archer_backward_left_move", framePeriod, loop);
                animator.addAnimation("archer_backward_right_move", framePeriod, loop);
                animator.addAnimation("archer_backward_left_move_highlight", framePeriod, loop);
                animator.addAnimation("archer_backward_right_move_highlight", framePeriod, loop);

                troop.addComponent(animator);
                troop.addComponent(new ArcherAnimationController());
            } else {
                troop = createMeleeTroop(type);

                // Add animation controller based on the unit type
                if(type == UnitType.SPEARMAN){
                    animator.addAnimation("spearman_forward_left_idle", framePeriod, loop);
                    animator.addAnimation("spearman_forward_left_idle_highlighted", framePeriod, loop);
                    animator.addAnimation("spearman_forward_left_move", framePeriod, loop);
                    animator.addAnimation("spearman_forward_right_move", framePeriod, loop);
                    animator.addAnimation("spearman_forward_left_move_highlighted", framePeriod, loop);
                    animator.addAnimation("spearman_forward_right_move_highlighted", framePeriod, loop);
                    animator.addAnimation("spearman_forward_left_attack", framePeriod, loop);
                    animator.addAnimation("spearman_forward_right_attack", framePeriod, loop);
                    animator.addAnimation("spearman_forward_left_attack_highlighted", framePeriod, loop);
                    animator.addAnimation("spearman_forward_right_attack_highlighted", framePeriod, loop);
                    animator.addAnimation("spearman_backward_left_move", framePeriod, loop);
                    animator.addAnimation("spearman_backward_right_move", framePeriod, loop);
                    animator.addAnimation("spearman_backward_left_move_highlighted", framePeriod, loop);
                    animator.addAnimation("spearman_backward_right_move_highlighted", framePeriod, loop);

                    troop.addComponent(animator);
                    troop.addComponent(new SpearmanAnimationController());
                    
                }else if(type == UnitType.SWORDSMAN){
                    animator.addAnimation("swordsman_forward_left_idle", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_left_idle_highlight", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_left_move", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_right_move", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_left_move_highlight", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_right_move_highlight", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_left_attack", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_right_attack", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_left_attack_highlight", framePeriod, loop);
                    animator.addAnimation("swordsman_forward_right_attack_highlight", framePeriod, loop);
                    animator.addAnimation("swordsman_backward_left_move", framePeriod, loop);
                    animator.addAnimation("swordsman_backward_right_move", framePeriod, loop);
                    animator.addAnimation("swordsman_backward_left_move_highlight", framePeriod, loop);
                    animator.addAnimation("swordsman_backward_right_move_highlight", framePeriod, loop);

                    troop.addComponent(animator);
                    troop.addComponent(new SwordsmanAnimationController());
                }else if(type == UnitType.HOPLITE){
                    animator.addAnimation("hoplite_forward_left_idle", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_left_idle_highlighted", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_left_move", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_right_move", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_left_move_highlighted", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_right_move_highlighted", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_left_attack", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_right_attack", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_left_attack_highlighted", framePeriod, loop);
                    animator.addAnimation("hoplite_forward_right_attack_highlighted", framePeriod, loop);
                    animator.addAnimation("hoplite_backward_left_move", framePeriod, loop);
                    animator.addAnimation("hoplite_backward_right_move", framePeriod, loop);
                    animator.addAnimation("hoplite_backward_left_move_highlighted", framePeriod, loop);
                    animator.addAnimation("hoplite_backward_right_move_highlighted", framePeriod, loop);

                    troop.addComponent(animator);
                    troop.addComponent(new HopliteAnimationController());
                }
            }

            InputComponent inputComponent = ServiceLocator.getInputService().getInputFactory().createForFriendlyUnit();

            // Add AI Task to enable all the animations
            AITaskComponent aiComponent = new AITaskComponent().addTask(new UnitIdleTask());
            troop.addComponent(aiComponent);

            troop.addComponent(new PhysicsComponent())
                    .addComponent(new PhysicsMovementComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ALL))
                    .addComponent(new FriendlyComponent())
                    .addComponent(inputComponent)
                    .addComponent(new SelectableComponent());
            PhysicsUtils.setScaledCollider(troop, 0.2f, 1f);
            troop.getComponent(ColliderComponent.class).setDensity(1f);
            troop.setScale(2f,2f); // make size more manageable
            troops.add(troop);
        }

        // add animation events to unit
        // for (String direction: animation_directions) {
        //     for (String action: animation_actions) {
        //         String animationName = unitTypeStrings.get(type) + "_" +
        //                 direction + "_" + action;

        //         // register animation on relevant troop trigger
        //         unit.getEvents().addListener(direction + "_" + action,
        //                 () -> troops.forEach( troop -> troop.getComponent(
        //                          AnimationRenderComponent.class)
        //                         .startAnimation(animationName)));
        //     }
        // }

        // TODO: add control to unit entity
        unit.addComponent(new TroopContainerComponent(troops,
                          unitStats.movementRadius))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        PhysicsUtils.setRadiusCollider(unit, unitStats.movementRadius);
        // start us in an idle state
        // unit.getEvents().trigger("forward_right_idle");
        //Add FriendlyComponent, to indicate that this Entity should be allowed out of the City
        unit.addComponent(new FriendlyComponent());
        return unit;
    }

    private UnitFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
