package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CollectStatsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(CollectStatsComponent.class);
    private int collectionAmount;

    public CollectStatsComponent(int collectionAmount) {
        setCollectionAmount(collectionAmount);
    }

    /**
     * Returns the amount which entity can collect.
     *
     * @return amount to collect
     */
    public int getCollectionAmount() {
        return collectionAmount;
    }

    /**
     * Sets the entity's collection amount. Collection amount has a minimum bound of 0.
     *
     * @param collectionAmount collection amount
     */
    public void setCollectionAmount(int collectionAmount) {
        if (collectionAmount >= 0) {
            this.collectionAmount = collectionAmount;
        } else {
            this.collectionAmount = 0;
        }
        if (entity != null) {
            entity.getEvents().trigger("collectResource", this.collectionAmount);
        }
    }

    /**
     * Adds to the amount which an entity can collect. The amount added can be negative.
     *
     * @param collectionAmount collection amount to add
     */
    public void addCollectionAmount(int collectionAmount) {
        setCollectionAmount(this.collectionAmount + collectionAmount);
    }
}
