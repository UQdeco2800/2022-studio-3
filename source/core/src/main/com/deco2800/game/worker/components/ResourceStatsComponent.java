package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceStatsComponent extends WorkerInventoryComponent {
    private static final Logger logger = LoggerFactory.getLogger(ResourceStatsComponent.class);

    public ResourceStatsComponent(int wood, int stone, int metal) {
        setWood(wood);
        setStone(stone);
        setMetal(metal);
    }

    /**
     * Returns true if the entity has 0 wood and 0 stone and 0 metal, otherwise false.
     *
     * @return is resource dead
     */
    public Boolean isDead() {
        return getWood() == 0 && getMetal() == 0 && getStone() == 0;
    }

    /**
     * Collects stone
     */
    public int collectStone(CollectStatsComponent collector){
        int collectionAmount;
        logger.info("[+] num of stone in Rock before : " + Integer.toString(getStone()));
        if (getStone() == 0) {
            collectionAmount = 0;
        } else if (collector.getCollectionAmount() > getStone()) {
            collectionAmount = getStone();
        } else {
            collectionAmount = collector.getCollectionAmount();
        }
        setStone(getStone() - collectionAmount);
        logger.info("[+] num of stone in Rock after : " + Integer.toString(getStone()));
        return collectionAmount;
    }

    /**
     * Collects stone
     */
    public int collectWood(CollectStatsComponent collector) {
        int collectionAmount;
        logger.info("[+] num of wood in Tree before : " + Integer.toString(getWood()));
        if (getWood() == 0) {
            collectionAmount = 0;
        } else if (collector.getCollectionAmount() > getWood()) {
            collectionAmount = getWood();
        } else {
            collectionAmount = collector.getCollectionAmount();
        }
        setWood(getWood() - collectionAmount);
        logger.info("[+] num of wood in Tree after : " + Integer.toString(getWood()));
        return collectionAmount;
    }

    public int collectMetal(CollectStatsComponent collector) {
        int collectionAmount;
        logger.info("[+] num of metal in Mine() before : " + Integer.toString(getMetal()));
        if (getMetal() == 0) {
            collectionAmount = 0;
        } else if (collector.getCollectionAmount() > getMetal()) {
            collectionAmount = getMetal();
        } else {
            collectionAmount = collector.getCollectionAmount();
        }
        setMetal(getMetal() - collectionAmount);
        logger.info("[+] num of metal in TreeFactory() after : " + Integer.toString(getMetal()));
        return collectionAmount;
    }
    
}
