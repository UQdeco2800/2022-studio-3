package com.deco2800.game.components.building;
import com.deco2800.game.components.Component;

enum Health {
    NORMAL, // 100 - 50 Health
    HALF // 50 - 0 Health
}

public class HealthAnimation extends Component {

    public Health health;

    public HealthAnimation() {
        health = Health.NORMAL;
    }

    public String getAnimation() {
        if (health == Health.NORMAL) {
            return "default";
        } else if (health == Health.HALF) {
            return "50-idle";
        }
        return null;
    }

    /**
     * update the Building's current health
     * @param health the new health
     */
    public void updateHealth(Health health){
        this.health = health;
    }
}
