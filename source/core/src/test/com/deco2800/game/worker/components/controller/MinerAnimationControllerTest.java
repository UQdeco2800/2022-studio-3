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
        animator = new AnimationRenderComponent(new TextureAtlas("images/miner.atlas"));

        animator.addAnimation("miner_idle", 0.1f);
        animator.addAnimation("miner_forward_left_move", 0.1f);
        animator.addAnimation("miner_forward_right_move", 0.1f);
        animator.addAnimation("miner_back_left_move", 0.1f);
        animator.addAnimation("miner_back_right_move", 0.1f);
        animator.addAnimation("miner_forward_left_action", 0.1f);
        animator.addAnimation("miner_forward_right_action", 0.1f);

        miner.addComponent(animator);
    }

    @Test
    void minerHasAnimatorComponent(){
        assertTrue(miner.getComponent(AnimationRenderComponent.class) == animator);
    }

    @Test
    void hasMinerForwardIdle(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_idle"), true);
    }

    @Test
    void hasMinerForwardLeftMove(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_forward_left_move"), true);
    }

    @Test
    void hasMinerForwardRightMove(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_forward_right_move"), true);
    }

    @Test
    void hasMinerBackLeftMove(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_back_left_move"), true);
    }

    @Test
    void hasMinerBackRightMove(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_back_right_move"), true);
    }

    @Test
    void hasMinerActionLeft(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_forward_left_action"), true);
    }

    @Test
    void hasMinerActionRight(){
        assertEquals(miner.getComponent(AnimationRenderComponent.class).hasAnimation("miner_forward_right_action"), true);
    }
}
