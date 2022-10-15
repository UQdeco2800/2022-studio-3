package com.deco2800.game.components.building;

import com.badlogic.gdx.graphics.Texture;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with a building. Building events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class BuildingActions extends Component {
    private int level;
    private Building type;

    /**
     * Constructs components with specified level
     * @param level initial building level when created
     * @param type type of Building
     */
    public BuildingActions(Building type, int level) {
        this.type = type;
        this.level = level;
    }

    /**
     * @return Building level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return Building type
     */
    public Building getType() {
        return type;
    }

    /**
     * Increments level by 1
     */
    public void addLevel() {
        System.out.println("Barracks level up");
        CombatStatsComponent csc = entity.getComponent(CombatStatsComponent.class);
        // if (entity != null && (csc = entity.getComponent(CombatStatsComponent.class)) != null) {
        if (csc != null) {
            //Level up its stats: health and defence increased
            csc.setMaxHealth(csc.getMaxHealth() + 100);
            csc.setHealth(csc.getMaxHealth());
            csc.setBaseDefence(csc.getBaseDefence() + 20);
        }
        this.level++;
        ShopUIFunctionalityComponent.removeButton();
    }

    /**
     * Used for determining if an entity is a wall
     * @param type Building type
     * @return If the building is a wall -> true; else -> false
     */
    public static boolean isWall(Building type) {
        return (type == Building.WALL || type == Building.WALL_NE || type == Building.WALL_SE);
    }

    /**
     * Sets wall type and texture to default (facing in no particular direction)
     */
    public void setWallDefault() {
        if (!isWall(type) || entity.getComponent(TextureRenderComponent.class) == null) {
            return;
        }
        type = Building.WALL;
        switch (level) {
            case 1:
                entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/wooden_wall.png", Texture.class));
                break;
            default:
            case 2:
                entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/stone_wall.png", Texture.class));
                break;
        }
    }

    /**
     * Sets wall type and texture to north-east facing wall
     */
    public void setWallNE() {
        if (!isWall(type) || entity.getComponent(TextureRenderComponent.class) == null) {
            return;
        }
        type = Building.WALL_NE;
        switch (level) {
            case 1:
                entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/wooden_wall_2.png", Texture.class));
                break;
            default:
            case 2:
                entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/stone_wall_2_.png", Texture.class));
                break;
        }
    }

    /**
     * Sets wall type and texture to south-east facing wall
     */
    public void setWallSE() {
        if (!isWall(type) || entity.getComponent(TextureRenderComponent.class) == null) {
            return;
        }
        type = Building.WALL_SE;
        switch (level) {
            case 1:
                entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/wooden_wall_3.png", Texture.class));
                break;
            default:
            case 2:
                entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService()
                        .getAsset("images/stone_wall_3.png", Texture.class));
                break;
        }
    }


    /**
     * Called upon creation. Used to define events for event listener to trigger.
     */
    public void create() {
        System.out.println("Added level up event");
        entity.getEvents().addListener("levelUp", this::addLevel);
    }

}
