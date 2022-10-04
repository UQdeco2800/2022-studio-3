package com.deco2800.game.worker.resources;

import com.deco2800.game.components.Component;

public class TreeAnimationController extends Component {
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("treeIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("treeChopAnimate", this::animateChop);
    }

    void animateIdle() {
        entity.getEvents().trigger("treeIdle");
    }

    void animateChop() {
        entity.getEvents().trigger("treeChop");
    }
}
