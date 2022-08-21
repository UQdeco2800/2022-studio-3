package com.deco2800.game.worker;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A component intended to be used by a worker to track inventory.
 *
 */
public class WorkerInventoryComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(WorkerInventoryComponent.class);
    private int wood;
    private int stone;
    private int iron;

    public WorkerInventoryComponent(int wood, int stone, int iron) {
        setWood(wood);
    }

    /**
     * Returns the worker's wood.
     *
     * @return entity's wood
     */
    public int getWood() {
        return this.wood;
    }

    /**
     * Sets the worker's wood. Wood has a minimum bound of 0.
     *
     * @param wood wood
     */
    public void setWood(int wood) {
        if (wood >= 0) {
            this.wood = wood;
        } else {
            this.wood = 0;
        }
        logger.debug("Setting wood to {}", this.wood);
    }

    /**
     * Returns if the player has a certain amount of wood.
     * @param wood required amount of wood
     * @return player has greater than or equal to the required amount of wood
     */
    public Boolean hasWood(int wood) {
        return this.wood >= wood;
    }

    /**
     * Adds to the player's wood. The amount added can be negative.
     * @param wood wood to add
     */
    public void addWood(int wood) {
        setWood(this.wood + wood);
    }
}
