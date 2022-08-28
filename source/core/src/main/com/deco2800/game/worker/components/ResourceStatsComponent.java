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
     * Returns true if the entity has 0 wood & 0 stone & 0 metal, otherwise false.
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
     * Collects wood from the entity based on the amount which the
     * collector can collect at a time.
     * @param collector the collector
     */
    public void collect(CollectStatsComponent collector) {
        int newWood = getWood() - collector.getCollectionAmount();
        setWood(newWood);
    }

    /**
     * Collects stone
     */
    public int collectStone(CollectStatsComponent collector){
        int newStone = getStone() - collector.getCollectionAmount();
        logger.info("[+] num of stone in Stone() before : " + Integer.toString(getStone()));
        setStone(newStone);
        logger.info("[+] num of stone in Stone() after : " + Integer.toString(getStone()));
        return collector.getCollectionAmount();
    }

    /**
     * Collects stone
     */
    public int collectWood(CollectStatsComponent collector){
        int newWood = getWood() - collector.getCollectionAmount();
        logger.info("[+] num of wood in TreeFactory() before : " + Integer.toString(getWood()));
        setWood(newWood);
        logger.info("[+] num of wood in TreeFactory() after : " + Integer.toString(getWood()));
        return collector.getCollectionAmount();
    }

    public int collectmetal(CollectStatsComponent collector){
        int newmetal = getMetal() - collector.getCollectionAmount();
        logger.info("[+] num of metal in Mine() before : " + Integer.toString(getMetal()));
        setMetal(newmetal);
        logger.info("[+] num of stone in TreeFactory() after : " + Integer.toString(getMetal()));
        return collector.getCollectionAmount();
    }
    
}
