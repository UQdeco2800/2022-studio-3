package com.deco2800.game.worker.components;

import com.deco2800.game.components.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ResourceStatsComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(ResourceStatsComponent.class);
    private int wood;
    private int stone;
    private int iron;

    public ResourceStatsComponent(int wood, int stone, int iron) {
        setWood(wood);
        setStone(stone);
        setIron(iron);
    }

    /**
     * Returns true if the entity has 0 wood & 0 stone & 0 iron, otherwise false.
     *
     * @return is resource dead
     */
    public Boolean isDead() {
        return wood == 0 && stone == 0 && iron == 0;
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
     * Returns the entity's iron.
     *
     * @return entity's iron
     */
    public int getIron() {
        return iron;
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

    public void setIron(int iron){
        if (iron >= 0) {
            this.iron = iron;
        } else {
            this.iron = 0;
        }
        if (entity != null) {
            entity.getEvents().trigger("updateIron", this.iron);
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

    public void addIron(int iron) {
        setIron(this.iron + iron);
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
        logger.info("[+] num of stone in Tree() before : " + Integer.toString(getWood()));
        setWood(newWood);
        logger.info("[+] num of stone in Tree() after : " + Integer.toString(getWood()));
        return collector.getCollectionAmount();
    }
}
