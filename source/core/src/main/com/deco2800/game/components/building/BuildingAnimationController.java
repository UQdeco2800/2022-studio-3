package com.deco2800.game.components.building;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;

public class BuildingAnimationController extends Component {
    AnimationRenderComponent animator;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        entity.getEvents().addListener("HealthAnimation", this::updateAnimation);
    }

    void updateAnimation() {
        // Updates the health value in HealthComponent
        int health = this.entity.getComponent(CombatStatsComponent.class).getHealth();

        if (health <= 500 && health >= 250) {
            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.NORMAL);
        } else if (health < 250 && health > 0) {
            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.HALF);
        } else if (health <= 0) {
            animator.startAnimation("collapse");
            this.entity.getComponent(PhysicsComponent.class).getPhysics().addToDestroy(this.entity);
        }
        // Applies the correct animation
        if (this.enabled) {
            animator.startAnimation(entity.getComponent(HealthAnimation.class).getAnimation());
        }
    }
}
