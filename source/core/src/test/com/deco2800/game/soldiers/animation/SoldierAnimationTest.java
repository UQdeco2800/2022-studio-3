package com.deco2800.game.soldiers.animation;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.soldiers.factories.SpearmanFactory;

@ExtendWith(GameExtension.class)
public class SoldierAnimationTest {
    private static Entity spearman;

    @BeforeAll
    void BeforeAll(){
        spearman = SpearmanFactory.createSpearman();
    }

    @Test
    static void checkAllAvailableSoldierAnimation(){
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierIdleAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierHighlightedIdleAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierLeftMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierRightMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierHighlightedLeftMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierHighlightedRightMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierLeftAttackAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierRightAttackAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierHighlightedLeftAttackAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierHighlightedRightAttackAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierBackwardLeftMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierBackwardRightMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierBackwardHighlightedLeftMoveAnimate"));
        assertTrue(spearman.getComponent(AnimationRenderComponent.class).hasAnimation("soldierBackwardHighlightedRightMoveAnimate"));
    }
}
