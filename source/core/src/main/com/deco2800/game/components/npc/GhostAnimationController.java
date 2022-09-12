package com.deco2800.game.components.npc;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

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
    entity.getEvents().addListener("enemy-movement", this::animateWest);
    entity.getEvents().addListener("moveEast", this::animateEast);
  }

  void animateWest() {
    animator.startAnimation("move-west");
  }

  void animateEast() {
    animator.startAnimation("move-east");
  }
}
