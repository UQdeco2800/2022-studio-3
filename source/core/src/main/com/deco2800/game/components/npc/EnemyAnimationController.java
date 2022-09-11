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

    public void animateWest(){ animator.startAnimation("move-west"); }
    public void animateEast(){ animator.startAnimation("move-east"); }
    public void animateNorth(){ animator.startAnimation("move-north"); }
    public void animateSouth(){ animator.startAnimation("move-south"); }

    public void defaultAnimation() {
        animator.startAnimation("default");
    }

    public void attackNorth() {
        animator.startAnimation("attack-north");
    }
    public void attackSouth() {
        animator.startAnimation("attack-south");
    }
    public void attackWest() {
        animator.startAnimation("attack-west");
    }
    public void attackEast() {
        animator.startAnimation("attack-east");
    }
}
