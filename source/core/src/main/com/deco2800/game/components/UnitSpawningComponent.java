package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.events.EventHandler;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;

public class UnitSpawningComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
    private BuildingActions buildingActions;
    private ColliderComponent colliderComponent;
    private AtlantisTerrainFactory atlantisTerrainFactory;
    private static final GameTime timer = ServiceLocator.getTimeSource();
    private long lastTime;

    private EventHandler eventHandler;

    public UnitSpawningComponent(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void create() {
        buildingActions = this.getEntity().getComponent(BuildingActions.class);
        colliderComponent = this.getEntity().getComponent(ColliderComponent.class);
        lastTime = timer.getTime();
    }

    @Override
    public void update() {
        if (timer.getTimeSince(lastTime) >= 30000) {
            lastTime = timer.getTime();
            spawnUnit();
        }
    }

    /**
     * This function is a proof of concept for how spawning will work, at present
     * it will just spawn some unit entity, may they be workers, soldiers or enemies.
     *
     * It will be expanded further to include spawning based on the building type
     */
    private void spawnUnit() {
        Building buildingType = buildingActions.getType();
        PolygonShape shape = (PolygonShape) colliderComponent.getFixture().getShape();
        Vector2 spawnPoint = getEntity().getCenterPosition();

        switch(buildingType) {
            case BARRACKS:
                eventHandler.trigger("spawnSpearmint", spawnPoint);
                 break;
            case TITANSHRINE:
                eventHandler.trigger("spawnTitan", spawnPoint);
                break;
            case SHIP:
                eventHandler.trigger("spawnSnake", spawnPoint);
                break;
        }
    }
}
