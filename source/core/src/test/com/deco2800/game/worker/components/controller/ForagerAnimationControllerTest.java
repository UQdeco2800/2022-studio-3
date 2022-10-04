package com.deco2800.game.worker.components.controller;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.AnimationRenderComponent;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(GameExtension.class)
public class ForagerAnimationControllerTest {
    static Entity forager;
    static AnimationRenderComponent animator;

    @BeforeAll
    static void BeforeAll(){
        forager = new Entity();
        animator = new AnimationRenderComponent(new TextureAtlas("images/forager.atlas"));

        animator.addAnimation("forager_idle", 0.1f);
        animator.addAnimation("forager_forward_left_move", 0.1f);
        animator.addAnimation("forager_forward_right_move", 0.1f);
        animator.addAnimation("forager_back_left_move", 0.1f);
        animator.addAnimation("forager_back_right_move", 0.1f);
        animator.addAnimation("forager_forward_left_action", 0.1f);
        animator.addAnimation("forager_forward_right_action", 0.1f);

        forager.addComponent(animator);
    }

    @Test
    void foragerHasAnimatorComponent(){
        assertTrue(forager.getComponent(AnimationRenderComponent.class) == animator);
    }

    @Test
    void hasForagerForwardIdle(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_idle"), true);
    }

    @Test
    void hasForagerForwardLeftMove(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_forward_left_move"), true);
    }

    @Test
    void hasForagerForwardRightMove(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_forward_right_move"), true);
    }

    @Test
    void hasForagerBackLeftMove(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_back_left_move"), true);
    }

    @Test
    void hasForagerBackRightMove(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_back_right_move"), true);
    }

    @Test
    void hasForagerActionLeft(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_forward_left_action"), true);
    }

    @Test
    void hasForagerActionRight(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_forward_right_action"), true);
    }

}
