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
public class MinerAnimationControllerTest {

    static Entity miner;
    static AnimationRenderComponent animator;

    @BeforeAll
    static void BeforeAll(){
        miner = new Entity();
        animator = new AnimationRenderComponent(new TextureAtlas("images/miner_forward.atlas"));

        animator.addAnimation("miner_forward_idle", 0.1f);
        animator.addAnimation("miner_forward_move", 0.1f);
        animator.addAnimation("minerActionLeft", 0.1f);
        animator.addAnimation("minerActionRight", 0.1f);
        animator.addAnimation("minerRight", 0.1f);
        animator.addAnimation("minerLeft", 0.1f);

        miner.addComponent(animator);
    }

    @Test
    void minerHasAnimatorComponent(){
        assertTrue(miner.getComponent(AnimationRenderComponent.class) == animator);
    }

    @Test
    void hasMinerForwardIdle(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_forward_idle"), true);
    }

    @Test
    void hasMinerForwardMove(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_forward_move"), true);
    }

    @Test
    void hasMinerActionLeft(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("minerActionLeft"), true);
    }

    @Test
    void hasMinerActionRight(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("minerActionRight"), true);
    }

    @Test
    void hasMinerRight(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("minerRight"), true);
    }
    
    @Test
    void hasMinerLeft(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("minerLeft"), true);
    }
}
