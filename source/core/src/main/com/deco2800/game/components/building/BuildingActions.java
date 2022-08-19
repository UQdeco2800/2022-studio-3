package com.deco2800.game.components.building;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
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

    private boolean placed;
    private TextureRenderComponent textureRenderComponent;
    private PhysicsComponent physicsComponent;
    private CameraComponent cameraComponent;

    public void create() {
        entity.getEvents().addListener("placing", this::placing);
        entity.getEvents().addListener("place", this::place);
        textureRenderComponent = entity.getComponent(TextureRenderComponent.class);
        textureRenderComponent.setEnabled(true);
        physicsComponent = entity.getComponent(PhysicsComponent.class);
        physicsComponent.setEnabled(false);
        cameraComponent = entity.getComponent(CameraComponent.class);
        placed = false;
    }

    public void placing(Vector2 position) {
        if (placed) {return;}
        // position.scl(2f/Gdx.graphics.getWidth(), -2f/Gdx.graphics.getHeight());
        Vector2 buildingPosition = position.cpy();
        buildingPosition.scl(10f / Gdx.graphics.getWidth(), 10f / Gdx.graphics.getHeight());
        entity.setPosition(buildingPosition);
    }

    public void place(Vector2 position) {
        if (placed) {return;}
        placed = true;
        physicsComponent.setEnabled(true);
        Vector2 buildingPosition = position.cpy();
        buildingPosition.scl(10f / Gdx.graphics.getWidth(), 10f / Gdx.graphics.getHeight());
        entity.setPosition(buildingPosition);
    }

}
