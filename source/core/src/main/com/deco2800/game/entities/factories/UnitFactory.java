package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.TouchAttackComponent;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.components.friendly.TroopContainerComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.UnitType;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.events.listeners.EventListener0;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.PhysicsUtils;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.HitboxComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;

import javax.naming.OperationNotSupportedException;
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
                        .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        PhysicsUtils.setRadiusCollider(unit, stats.example.movementRadius);
        return unit;
    }

    private static Entity createRangedTroop(UnitType type) {
        //TODO: implement ranged units
        return new Entity();
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
            AnimationRenderComponent animator =
                    new AnimationRenderComponent(resources.getAsset(atlasPath,
                            TextureAtlas.class));
            for (String direction: animation_directions) {
                for (String action: animation_actions) {
                    String animationName = unitTypeStrings.get(type) + "_" +
                            direction + "_" + action;
                    if (action.equals("attack")) {
                        // attacks don't have enough frames for this
                        animator.addAnimation(animationName, 0.5f, loop);
                    } else {
                        animator.addAnimation(animationName, framePeriod, loop);
                    }
                }
            }

            if (type == UnitType.ARCHER) {
                troop = createRangedTroop(UnitType.ARCHER);
            } else {
                troop = createMeleeTroop(type);
            }
            troop.addComponent(new PhysicsComponent())
                    .addComponent(new ColliderComponent())
                    .addComponent(new HitboxComponent().setLayer(PhysicsLayer.ALL))
                    .addComponent(animator);
            PhysicsUtils.setScaledCollider(troop, 0.2f, 1f);
            troop.getComponent(ColliderComponent.class).setDensity(1f);
            troop.setScale(2f,2f); // make size more manageable
            troops.add(troop);
        }

        // add animation events to unit
        for (String direction: animation_directions) {
            for (String action: animation_actions) {
                String animationName = unitTypeStrings.get(type) + "_" +
                        direction + "_" + action;

                // register animation on relevant troop trigger
                unit.getEvents().addListener(direction + "_" + action,
                        () -> troops.forEach( troop -> troop.getComponent(
                                 AnimationRenderComponent.class)
                                .startAnimation(animationName)));
            }
        }

        // TODO: add control to unit entity
        unit.addComponent(new TroopContainerComponent(troops,
                          unitStats.movementRadius))
                .addComponent(new PhysicsComponent())
                .addComponent(new ColliderComponent())
                .addComponent(new HitboxComponent().setLayer(PhysicsLayer.PLAYER));
        PhysicsUtils.setRadiusCollider(unit, unitStats.movementRadius);
        // start us in an idle state
        unit.getEvents().trigger("forward_right_idle");
        //Add FriendlyComponent, to indicate that this Entity should be allowed out of the City
        unit.addComponent(new FriendlyComponent());
        return unit;
    }

    private UnitFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
