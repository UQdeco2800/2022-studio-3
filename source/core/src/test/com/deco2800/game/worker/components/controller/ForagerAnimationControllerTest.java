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
        animator = new AnimationRenderComponent(new TextureAtlas("images/forager_forward.atlas"));

        animator.addAnimation("forager_forward_idle", 0.1f);
        animator.addAnimation("forager_forward_move", 0.1f);
        animator.addAnimation("foragerActionLeft", 0.1f);
        animator.addAnimation("foragerActionRight", 0.1f);
        animator.addAnimation("foragerRight", 0.1f);
        animator.addAnimation("foragerLeft", 0.1f);

        forager.addComponent(animator);
    }

    @Test
    void foragerHasAnimatorComponent(){
        assertTrue(forager.getComponent(AnimationRenderComponent.class) == animator);
    }

    @Test
    void hasForagerForwardIdle(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_forward_idle"), true);
    }

    @Test
    void hasForagerForwardMove(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("forager_forward_move"), true);
    }

    @Test
    void hasForagerActionLeft(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("foragerActionLeft"), true);
    }

    @Test
    void hasForagerActionRight(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("foragerActionRight"), true);
    }

    @Test
    void hasForagerRight(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("foragerRight"), true);
    }
    
    @Test
    void hasForagerLeft(){
        assertEquals(forager.getComponent(AnimationRenderComponent.class).hasAnimation("foragerLeft"), true);
    }
}
