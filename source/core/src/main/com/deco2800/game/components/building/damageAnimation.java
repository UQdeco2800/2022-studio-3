package com.deco2800.game.components.building;

import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class damageAnimation extends Component {
    AnimationRenderComponent Animation;

    @Override
    public void create() {
        super.create();
        Animation = this.entity.getComponent(AnimationRenderComponent.class);
        addEvents();

    }

    public void addEvents() {
        this.entity.getEvents().addListener("Damaged", this::Damaged);
    }

    public void Damaged() {
        Animation.startAnimation("Damaged");
    }
}
