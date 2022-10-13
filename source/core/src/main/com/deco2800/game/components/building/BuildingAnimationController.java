package com.deco2800.game.components.building;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class BuildingAnimationController extends Component {
    AnimationRenderComponent animator;
    private int D_health;
    private boolean dispose = false;

    @Override
    public void create() {
        super.create();
        animator = this.entity.getComponent(AnimationRenderComponent.class);
        D_health = this.entity.getComponent(CombatStatsComponent.class).getHealth();
        entity.getEvents().addListener("HealthAnimation", this::updateAnimation);
    }

    void updateAnimation() {
        // Updates the health value in HealthComponent
        int health = this.entity.getComponent(CombatStatsComponent.class).getHealth();

        if (health <= D_health && health >= 0.5*D_health) {
            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.NORMAL);
        } else if (health < 0.5*D_health && health > 0) {
            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.HALF);
        } else if (health <= 0 && !dispose) {
            this.entity.getComponent(HealthAnimation.class).updateHealth(Health.DEAD);
            dispose = true;
        } else if (dispose) {
            this.entity.getComponent(PhysicsComponent.class).getPhysics().addToDestroy(this.entity);
        }
        // Applies the correct animation
        if (this.enabled) {
            animator.startAnimation(entity.getComponent(HealthAnimation.class).getAnimation());
        }
    }
}
