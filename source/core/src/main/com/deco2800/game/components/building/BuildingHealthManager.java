package com.deco2800.game.components.building;

import com.badlogic.gdx.utils.Null;
import com.deco2800.game.components.*;

/**
 * Component that sets the buildings health state.
 */
public class BuildingHealthManager extends Component {
    private BuildingHealth buildingHealthState;
    private BuildingActions buildingActions;
    private BuildingHealth previousHealthState;
    private CombatStatsComponent buildingStats;

    private EntityDirection direction;

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

        buildingStats = this.entity.getComponent(CombatStatsComponent.class);

        currentHealth = buildingStats.getHealth();
        previousHealth = currentHealth;
        maxHealth = buildingStats.getMaxHealth();

        buildingHealthState = newState();

        try {
            EntityDirection newDirection = this.getEntity().getComponent(EntityDirectionComponent.class)
                                                           .getEntityDirection();
            updateState(newDirection);
        } catch (NullPointerException e) {
            updateState();
            direction = null;
        }
    }

    @Override
    public void update() {
        try {
            EntityDirection newDirection = this.getEntity().getComponent(EntityDirectionComponent.class)
                                                           .getEntityDirection();
            updateState(newDirection);
        } catch (NullPointerException e) {
            updateState();
            direction = null;
        }
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
            switch (buildingHealthState) {
                case FULL_HEALTH:
                    this.getEntity().getEvents().trigger("underAttackFull");
                    break;
                case HALF_HEALTH:
                    this.getEntity().getEvents().trigger("underAttackHalf");
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
        switch(buildingHealthState) {
            case ZERO_HEALTH -> {
                this.getEntity().getEvents().trigger("collapsing");
            }
            case HALF_HEALTH -> {
                this.getEntity().getEvents().trigger("damaged");
            }
            case FULL_HEALTH -> {
                this.getEntity().getEvents().trigger("creation");
            }
        }
    }

    private void updateState(EntityDirection newDirection) {
        currentHealth = buildingStats.getHealth();
        buildingHealthState = newState();
        if (this.direction != newDirection) {
            this.direction = newDirection;
            activateEvent(this.direction.toString());
        }

        if (previousHealthState != buildingHealthState) {
            previousHealthState = buildingHealthState;
            activateEvent(this.direction.toString());
        }

        if (previousHealth != currentHealth) {
            previousHealth = currentHealth;
            switch (buildingHealthState) {
                case FULL_HEALTH:
                    this.getEntity().getEvents().trigger("underAttackFull'", this.direction.toString());
                    break;
                case HALF_HEALTH:
                    this.getEntity().getEvents().trigger("underAttackHalf'", this.direction.toString());
                    break;
            }
        }
    }

    private void activateEvent(String direction) {
        switch(buildingHealthState) {
            case ZERO_HEALTH -> {
                this.getEntity().getEvents().trigger("collapsing'", direction);
            }
            case HALF_HEALTH -> {
                this.getEntity().getEvents().trigger("damaged'", direction);
            }
            case FULL_HEALTH -> {
                this.getEntity().getEvents().trigger("creation'", direction);
            }
        }
    }
}
