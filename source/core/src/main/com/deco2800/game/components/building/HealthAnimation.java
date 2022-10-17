package com.deco2800.game.components.building;
import com.deco2800.game.components.Component;

enum Health {
    NORMAL, // 100 - 50 Health
    HALF, // 50 - 0 Health
    DEAD
}

public class HealthAnimation extends Component {

    public Health health;

    public HealthAnimation() {
        health = Health.NORMAL;
    }

    public String getAnimation() {
        if (health == Health.NORMAL) {
            return "100-idle";
        } else if (health == Health.HALF) {
            return "50";
        } else if (health == Health.DEAD) {
            return "collapse";
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

    public Health getHealth(){
        return health;
    }
}
