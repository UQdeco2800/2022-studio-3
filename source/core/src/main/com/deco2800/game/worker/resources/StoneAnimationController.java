package com.deco2800.game.worker.resources;

import com.deco2800.game.components.Component;

public class StoneAnimationController extends Component {
    @Override
    public void create() {
        super.create();
        entity.getEvents().addListener("stoneIdleAnimate", this::animateIdle);
        entity.getEvents().addListener("stoneMineAnimate", this::animateMine);
    }

    void animateIdle() {
        entity.getEvents().trigger("stoneIdle");
    }

    void animateMine() {
        entity.getEvents().trigger("stoneMine");
    }
}
