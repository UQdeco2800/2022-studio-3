package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.utils.math.Vector2Utils;

/**
 * This class listens to events relevant to a ghost entity's state and plays the animation when one
 * of the events is triggered.
 */
public class GhostAnimationController extends Component {
  AnimationRenderComponent animator;

  @Override
  public void create() {
    super.create();
    animator = this.entity.getComponent(AnimationRenderComponent.class);
    entity.getEvents().addListener("move-west", this::animateWest);
    entity.getEvents().addListener("move-east", this::animateEast);
    entity.getEvents().addListener("move-south", this::animateSouth);
    entity.getEvents().addListener("idle", this::animateIdle);
  }

  void animateIdle() {
    animator.startAnimation("default");
  }
  void animateWest() {
    animator.startAnimation("move-west");
  }

  void animateEast() {
    animator.startAnimation("move-east");
  }
  void animateSouth() {
    animator.startAnimation("move-south");
  }
}
