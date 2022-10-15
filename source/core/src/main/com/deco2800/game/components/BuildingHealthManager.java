package com.deco2800.game.components;

import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;

/**
 * Component that sets the buildings health state.
 */
public class BuildingHealthManager extends Component {
    private BuildingHealth buildingHealthState;
    private BuildingActions buildingActions;
    private BuildingHealth previousHealthState;
    private CombatStatsComponent buildingStats;

    private int currentHealth;
    private int previousHealth;
    private int maxHealth;

    public BuildingHealthManager() {
        buildingHealthState = BuildingHealth.DEFAULT;
        previousHealthState = buildingHealthState;
    }

    @Override
    public void create() {
        buildingActions = this.getEntity().getComponent(BuildingActions.class);

        switch (buildingActions.getType()) {
            case SHIP -> this.getEntity().getEvents().trigger("setShip", true);
        }

        buildingStats = this.entity.getComponent(CombatStatsComponent.class);
        currentHealth = buildingStats.getHealth();
        previousHealth = currentHealth;
        maxHealth = buildingStats.getMaxHealth();
        buildingHealthState = newState();
        updateState();
    }

    @Override
    public void update() {
        updateState();
    }

    private void updateState() {
        currentHealth = buildingStats.getHealth();
        buildingHealthState = newState();

        if (previousHealthState != buildingHealthState) {
            previousHealthState = buildingHealthState;
            activateEvent();
        }

        if (previousHealth != currentHealth) {
            previousHealth = currentHealth;
            switch(buildingActions.getType()) {
                case SHIP:
                    switch (buildingHealthState) {
                        case FULL_HEALTH:
                            this.getEntity().getEvents().trigger("directionAttacked");
                            break;
                        case HALF_HEALTH:
                            this.getEntity().getEvents().trigger("underAttackHalf");
                            break;
                    }
                    break;
                default:
                    switch (buildingHealthState) {
                        case FULL_HEALTH:
                            this.getEntity().getEvents().trigger("underAttackFull");
                            break;
                        case HALF_HEALTH:
                            this.getEntity().getEvents().trigger("underAttackHalf");
                            break;
                    }
                    break;
            }
        }
    }

    private BuildingHealth newState() {
        float healthPerCent = ((float) currentHealth /
                              ((float) maxHealth));

        if (healthPerCent > 0.5f) {
            return BuildingHealth.FULL_HEALTH;
        } else if (healthPerCent <= 0.5f && healthPerCent > 0f) {
            return BuildingHealth.HALF_HEALTH;
        }

        return BuildingHealth.ZERO_HEALTH;
    }

    private void activateEvent() {
        int healthCode = 0;
        switch (buildingActions.getType()) {
            case SHIP:
                switch (buildingHealthState) {
                    case ZERO_HEALTH -> this.getEntity().getEvents().trigger("healthCode", 0);
                    case HALF_HEALTH -> this.getEntity().getEvents().trigger("healthCode", 50);
                    case FULL_HEALTH -> this.getEntity().getEvents().trigger("healthCode", 100);
                }
                break;
            default:
                switch(buildingHealthState) {
                    case ZERO_HEALTH -> this.getEntity().getEvents().trigger("collapsing");
                    case HALF_HEALTH -> this.getEntity().getEvents().trigger("damaged");
                    case FULL_HEALTH -> this.getEntity().getEvents().trigger("creation");
                }
                break;
        }
    }
}
