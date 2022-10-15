package com.deco2800.game.soldiers.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.animation.SpearmanAnimationController;
import com.deco2800.game.soldiers.type.HopliteComponent;
import com.deco2800.game.soldiers.type.SpearmanComponent;
import com.badlogic.gdx.graphics.g2d.Animation;

public class SpearmanFactory {

    private static final BaseUnitConfig stats = FileLoader.readClass(UnitConfigs.class, "configs/units.json").spearmen;

    public static Entity createSpearman() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/spearman.atlas", TextureAtlas.class));

        animator.addAnimation("spearman_forward_left_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("spearman_backward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("spearman_backward_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("spearman_forward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("spearman_forward_right_move", 0.1f, Animation.PlayMode.LOOP);
        
        Entity spearman = SoldierFactory.createSoldier();
        spearman.addComponent(new SpearmanComponent())
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.baseDefence))
                .addComponent(new SpearmanAnimationController());

        spearman.getComponent(AnimationRenderComponent.class).scaleEntity();
        spearman.scaleHeight(2.0f);
        spearman.scaleWidth(2.0f);
        return spearman;
    }
    
    private SpearmanFactory() {
        throw new IllegalStateException("Utility class");
    }
}