package com.deco2800.game.components.building;

import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;

public class AnimationTestingComponent extends Component {
    CombatStatsComponent combatStatsComponent;
    private static final GameTime timer = ServiceLocator.getTimeSource();
    private long lastTime;

    @Override
    public void create() {
        combatStatsComponent = this.getEntity().getComponent(CombatStatsComponent.class);
        lastTime = timer.getTime();
    }

    @Override
    public void update() {
        if (timer.getTimeSince(lastTime) >= 5000) {
            lastTime = timer.getTime();
            combatStatsComponent.hit(new CombatStatsComponent(1000, 50, 0));
        }
    }
}
