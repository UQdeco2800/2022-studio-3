package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.CameraComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.player.InventoryComponent;
import com.deco2800.game.components.player.TouchPlayerInputComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BuildingActions extends Component {

    private static final Logger logger = LoggerFactory.getLogger(BuildingActions.class);
    private boolean placed;
    private TextureRenderComponent textureRenderComponent;
    private PhysicsComponent physicsComponent;
    private CameraComponent cameraComponent;

    private boolean placingBuilding;

    public void create() {
        entity.getEvents().addListener("placing", this::placing);
        entity.getEvents().addListener("place", this::place);
        entity.getEvents().addListener("spawnTownHall", this::spawnTownHall);
        entity.getEvents().addListener("spawnWall", this::spawnWall);
        entity.getEvents().addListener("spawnBarracks", this::spawnBarracks);
        entity.getEvents().addListener("spawnMedievalBarracks", this::spawnMedievalBarracks);
        textureRenderComponent = entity.getComponent(TextureRenderComponent.class);
        textureRenderComponent.setEnabled(true);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.setEnabled(false);
        cameraComponent = entity.getComponent(CameraComponent.class);
        placed = false;
        placingBuilding = false;
    }

    public void spawnTownHall() {
        logger.info("will spawn town hall");
    }

    public void spawnWall() {
        logger.info("will spawn wall");
    }

    public void spawnBarracks() {
        logger.info("will spawn barracks");
    }

    public void spawnMedievalBarracks() {
        logger.info("will spawn Medieval barracks");
    }
    public void placing(Vector2 position) {
        if (placed) {return;}
        // position.scl(2f/Gdx.graphics.getWidth(), -2f/Gdx.graphics.getHeight());
        Vector2 buildingPosition = position.cpy();
        //buildingPosition.scl(10f / Gdx.graphics.getWidth(), 10f / Gdx.graphics.getHeight());
        entity.setPosition(buildingPosition);
    }

    public void place(Vector2 position) {
        if (placed) {return;}
        //placed = true;
        physicsComponent.setEnabled(true);
        Vector2 buildingPosition = position.cpy();
        //buildingPosition.scl(10f / Gdx.graphics.getWidth(), 10f / Gdx.graphics.getHeight());
        logger.info("x is " + Float.toString(position.x)+" y is " + Float.toString(position.y));
        entity.setPosition(buildingPosition);
    }

}
