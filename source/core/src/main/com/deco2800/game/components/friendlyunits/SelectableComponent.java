package com.deco2800.game.components.friendlyunits;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.Component;

public class SelectableComponent extends Component {

    private boolean selected;

    public SelectableComponent() {
        selected = false;
    }

    @Override
    public void create() {
        entity.getEvents().addListener("click", this::isIn);
        entity.getEvents().addListener("dragAndClick", this::containsUs);
    }

    @Override
    public void update() {

    }

    public void isIn(int xCoordinate, int yCoordinate) {

    }

    public void containsUs(int startX, int startY, int endX, int endY) {
        Vector2 centrePosition = entity.getCenterPosition();
    }
}
