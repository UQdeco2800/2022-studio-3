package com.deco2800.game.worker;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceStatsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ResourceStatsComponent.class);
    private int wood;

    public ResourceStatsComponent(int wood) {
        setWood(wood);
    }

    /**
     * Returns true if the entity has 0 wood, otherwise false.
     *
     * @return is resource dead
     */
    public Boolean isDead() {
        return wood == 0;
    }

    /**
     * Returns the entity's wood.
     *
     * @return entity's wood
     */
    public int getWood() {
        return wood;
    }

    /**
     * Sets the entity's wood. Wood has a minimum bound of 0.
     *
     * @param wood wood
     */
    public void setWood(int wood) {
        if (wood >= 0) {
            this.wood = wood;
        } else {
            this.wood = 0;
        }
        if (entity != null) {
            entity.getEvents().trigger("updateWood", this.wood);
        }
    }

    /**
     * Adds to the entity's wood. The amount added can be negative.
     *
     * @param wood wood to add
     */
    public void addWood(int wood) {
        setWood(this.wood + wood);
    }

    /**
     * Collects wood from the entity based on the amount which the
     * collector can collect at a time.
     * @param collector the collector
     */
    public void collect(CollectStatsComponent collector) {
        int newWood = getWood() - collector.getCollectionAmount();
        setWood(newWood);
    }
}
