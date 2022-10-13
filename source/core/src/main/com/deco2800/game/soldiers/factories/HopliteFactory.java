package com.deco2800.game.soldiers.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.type.HopliteComponent;
import com.badlogic.gdx.graphics.g2d.Animation;

public class HopliteFactory {

    private static final BaseUnitConfig stats = FileLoader.readClass(UnitConfigs.class, "configs/units.json").hoplite;

    public static Entity createHoplite() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/hoplite.atlas", TextureAtlas.class));

        animator.addAnimation("hoplite_forward_left_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("hoplite_backward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("hoplite_backward_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("hoplite_forward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("hoplite_forward_right_move", 0.1f, Animation.PlayMode.LOOP);
        
        Entity hoplite = SoldierFactory.createSoldier();
        hoplite.addComponent(new HopliteComponent())
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.baseDefence));
        return hoplite;
    }
    
    private HopliteFactory() {
        throw new IllegalStateException("Utility class");
    }
}
