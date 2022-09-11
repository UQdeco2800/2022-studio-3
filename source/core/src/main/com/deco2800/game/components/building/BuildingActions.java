package com.deco2800.game.components.building;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.Component;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.helpers.Util;

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
     */
    public BuildingActions(Building type, int level) {
        this.type = type;
        this.level = level;
    }

    /**
     * @return level
     */
    public int getLevel() {
        return level;
    }

    public Building getType() {
        return type;
    }

    /**
     * Increments level by 1
     */
    public void addLevel() {
        this.level++;
        entity.getComponent(TextureRenderComponent.class).setTexture(ServiceLocator.getResourceService().getAsset("images/barracks_level_1.0.png", Texture.class));
    }

    /**
     * Called upon creation. Used to define events for event listener to trigger.
     */
    public void create() {
        entity.getEvents().addListener("levelUp", this::addLevel); // Not triggered by any event yet
    }

}
