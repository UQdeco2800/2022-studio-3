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
        this.entity.getEvents().addListener("HalfDamaged", this::HalfDamaged);
        this.entity.getEvents().addListener("collapsed", this::collapse);
    }


    public void Damaged() {
        Animation.startAnimation("100-attacked");
    }
    public void HalfDamaged() {
        Animation.startAnimation("50-attacked");
    }
    public void collapse() {
        Animation.startAnimation("collapse");
    }
}
