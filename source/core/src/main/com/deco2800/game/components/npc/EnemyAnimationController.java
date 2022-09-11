package com.deco2800.game.components.npc;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.RandomPointGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.tasks.EnemyMovement;
import com.deco2800.game.rendering.AnimationRenderComponent;

/**
 * This class listens to events relevant to enemy entity's state and plays the animation when one
 * of the events is triggered.
 */
public class EnemyAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("goWest", this::animateWest);
        entity.getEvents().addListener("goEast", this::animateEast);
        entity.getEvents().addListener("goNorth", this::animateNorth);
        entity.getEvents().addListener("goSouth", this::animateSouth);

        entity.getEvents().addListener("default", this::defaultAnimation);

        entity.getEvents().addListener("attackNorth", this::attackNorth);
        entity.getEvents().addListener("attackSouth", this::attackSouth);
        entity.getEvents().addListener("attackWest", this::attackWest);
        entity.getEvents().addListener("attackEast", this::attackEast);
    }

    /**
     * Play animation when enemy moves west
     */
    public void animateWest(){ animator.startAnimation("move-west"); }

    /**
     * Play animation when enemy moves east
     */
    public void animateEast(){ animator.startAnimation("move-east"); }

    /**
     * Play animation when enemy moves north
     */
    public void animateNorth(){ animator.startAnimation("move-north"); }

    /**
     * Play animation when enemy moves south
     */
    public void animateSouth(){ animator.startAnimation("move-south"); }

    /**
     * Display default state enemy sprite
     */
    public void defaultAnimation() {
        animator.stopAnimation();
    }

    /**
     * Play animation when enemy attacks in north direction
     */
    public void attackNorth() {
        animator.startAnimation("attack-north");
    }

    /**
     * Play animation when enemy attacks in south direction
     */
    public void attackSouth() {
        animator.startAnimation("attack-south");
    }

    /**
     * Play animation when enemy attacks in west direction
     */
    public void attackWest() {
        animator.startAnimation("attack-west");
    }

    /**
     * Play animation when enemy attacks in east direction
     */
    public void attackEast() {
        animator.startAnimation("attack-east");
    }
}
