package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.Component;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.services.ServiceLocator;

import java.security.Provider;

public class SelectableComponent extends Component {

    private boolean selected;

    public SelectableComponent() {
        selected = false;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("click", this::isIn);
        entity.getEvents().addListener("dragAndClick", this::containsUs);
        entity.getEvents().addListener("dragSelect", this::containsUs);
    }


    @Override
    public void update() {

    }

    public void isIn(int xCoordinate, int yCoordinate) {
        Vector2 pointInWorld = screenToWorldPosition(xCoordinate, yCoordinate);
        Vector2 startPosition = entity.getPosition();
        Vector2 endPosition = entity.getPosition().mulAdd(entity.getScale(), 1f);
        if (contains(pointInWorld.x, startPosition.x, endPosition.x)
                && contains(pointInWorld.y, startPosition.y, endPosition.y)) {
            selected = true;
        } else {
            selected = false;
        }
    }

    public void containsUs(int startX, int startY, int endX, int endY) {
        Vector2 centrePosition = entity.getCenterPosition();
        Vector2 pointStartWorld = screenToWorldPosition(startX, startY);
        Vector2 pointEndWorld = screenToWorldPosition(endX, endY);
        if (contains(centrePosition.x, pointStartWorld.x, pointEndWorld.x)
                && contains(centrePosition.y, pointStartWorld.y, pointEndWorld.y)) {
            selected = true;
        } else {
            selected = false;
        }
    }

    public boolean contains(float inside, float from, float to) {
        return inside <= from && inside >= to || inside >= from && inside <= to;
    }

    public Vector2 screenToWorldPosition(int screenX, int screenY) {
        Vector3 worldPos = ServiceLocator.getEntityService().getCamera().unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(worldPos.x, worldPos.y);
    }

    public boolean isSelected() {
        return this.selected;
    }
}
