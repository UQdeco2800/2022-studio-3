package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.deco2800.game.GdxGame;
import com.deco2800.game.components.building.Building;
import com.deco2800.game.components.building.BuildingActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.random.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UnitSpawningComponent extends Component {
    private static final Logger logger = LoggerFactory.getLogger(GdxGame.class);
    private static BuildingActions buildingActions;
    private static ColliderComponent colliderComponent;

    private static final GameTime timer = ServiceLocator.getTimeSource();
    private long lastTime;

    @Override
    public void create() {
        buildingActions = this.getEntity().getComponent(BuildingActions.class);
        colliderComponent = this.getEntity().getComponent(ColliderComponent.class);
        entity.getEvents().addListener("spawnUnit", this::spawnUnit);
        lastTime = timer.getTime();
    }

    @Override
    public void update() {
        if (timer.getTimeSince(lastTime) >= 1000) {
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

        Vector2 spawnPoint = new Vector2();
        shape.getVertex(0, spawnPoint);

        System.out.println(spawnPoint);
    }
}
