package com.deco2800.game.soldiers.factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.BaseUnitConfig;
import com.deco2800.game.entities.configs.UnitConfigs;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.soldiers.animation.SwordsmanAnimationController;
import com.deco2800.game.soldiers.type.SwordsmanComponent;
import com.badlogic.gdx.graphics.g2d.Animation;

public class SwordsmanFactory {

    private static final BaseUnitConfig stats = FileLoader.readClass(UnitConfigs.class, "configs/units.json").swordsman;

    public static Entity createSwordsman() {
        AnimationRenderComponent animator =
                new AnimationRenderComponent(
                        ServiceLocator.getResourceService()
                                .getAsset("images/swordsman.atlas", TextureAtlas.class));

        animator.addAnimation("swordsman_forward_left_idle", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_left_idle_highlight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_left_move_highlight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_right_move_highlight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_left_attack", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_right_attack", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_left_attack_highlight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_forward_right_attack_highlight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_backward_left_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_backward_right_move", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_backward_left_move_highlight", 0.1f, Animation.PlayMode.LOOP);
        animator.addAnimation("swordsman_backward_right_move_highlight", 0.1f, Animation.PlayMode.LOOP);
        
        Entity swordsman = SoldierFactory.createSoldier();
        swordsman.addComponent(new SwordsmanComponent())
                .addComponent(animator)
                .addComponent(new CombatStatsComponent(stats.health, stats.baseAttack, stats.baseDefence))
                .addComponent(new SwordsmanAnimationController());

        swordsman.getComponent(AnimationRenderComponent.class).scaleEntity();
        swordsman.scaleHeight(2.0f);
        swordsman.scaleWidth(2.0f);
        return swordsman;
    }

    private SwordsmanFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
    
}


