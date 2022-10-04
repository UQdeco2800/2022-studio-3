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
public class BuilderAnimationControllerTest {

    static Entity builder;
    static AnimationRenderComponent animator;

    @BeforeAll
    static void BeforeAll(){
        builder = new Entity();
        animator = new AnimationRenderComponent(new TextureAtlas("images/builder.atlas"));

        animator.addAnimation("builder_idle", 0.1f);
        animator.addAnimation("builder_forward_left_move", 0.1f);
        animator.addAnimation("builder_forward_right_move", 0.1f);
        animator.addAnimation("builder_back_left_move", 0.1f);
        animator.addAnimation("builder_back_right_move", 0.1f);
        builder.addComponent(animator);
    }

    @Test
    void builderHasAnimatorComponent(){
        assertTrue(builder.getComponent(AnimationRenderComponent.class) == animator);
    }

    @Test
    void hasBuilderForwardIdle(){
        assertEquals(builder.getComponent(AnimationRenderComponent.class).hasAnimation("builder_idle"), true);
    }

    @Test
    void hasBuilderForwardLeftMove(){
        assertEquals(builder.getComponent(AnimationRenderComponent.class).hasAnimation("builder_forward_left_move"), true);
    }

    @Test
    void hasBuilderForwardRightMove(){
        assertEquals(builder.getComponent(AnimationRenderComponent.class).hasAnimation("builder_forward_right_move"), true);
    }

    @Test
    void hasBuilderBackLeftMove(){
        assertEquals(builder.getComponent(AnimationRenderComponent.class).hasAnimation("builder_back_left_move"), true);
    }

    @Test
    void hasBuilderBackRightMove(){
        assertEquals(builder.getComponent(AnimationRenderComponent.class).hasAnimation("builder_back_right_move"), true);
    }

}
