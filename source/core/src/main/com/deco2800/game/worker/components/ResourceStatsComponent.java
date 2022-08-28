package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceStatsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ResourceStatsComponent.class);
    private int wood;
    private int stone;
    private int metal;

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
        return wood == 0 && stone == 0 && metal == 0;
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
     * Returns the entity's stone.
     *
     * @return entity's stone
     */
    public int getStone() {
        return stone;
    }

    /**
     * Returns the entity's metal.
     *
     * @return entity's metal
     */
    public int getMetal() {
        return metal;
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
    
    public void setStone(int stone){
        if (stone >= 0) {
            this.stone = stone;
        } else {
            this.stone = 0;
        }
        if (entity != null) {
            entity.getEvents().trigger("updateStone", this.stone);
        }
    }

    public void setMetal(int metal){
        if (metal >= 0) {
            this.metal = metal;
        } else {
            this.metal = 0;
        }
        if (entity != null) {
            entity.getEvents().trigger("updateMetal", this.metal);
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

    public void addStone(int stone) {
        setStone(this.stone + stone);
    }

    public void addMetal(int metal) {
        setMetal(this.metal + metal);
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
        setWood(getStone() - collectionAmount);
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

    public int collectmetal(CollectStatsComponent collector){
        int newmetal = getMetal() - collector.getCollectionAmount();
        logger.info("[+] num of metal in Mine() before : " + Integer.toString(getMetal()));
        setMetal(newmetal);
        logger.info("[+] num of stone in TreeFactory() after : " + Integer.toString(getMetal()));
        return collector.getCollectionAmount();
    }
    
}
