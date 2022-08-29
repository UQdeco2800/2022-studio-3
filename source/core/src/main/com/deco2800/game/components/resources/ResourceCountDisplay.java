package com.deco2800.game.components.resources;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.UIComponent;

import java.util.ArrayList;

/**
 * A UI component for displaying the current game resource count
 * TODO: integrate with UnitInventoryComponent
 */

public class ResourceCountDisplay extends UIComponent {

    private Label woodLabel;
    private Label stoneLabel;
    private Label metalLabel;

    private Image resourceLabel;

    private int woodCount;
    private int stoneCount;
    private int metalCount;

    /* Adding images to count */
    public static ArrayList<Image> resourceImage = new ArrayList<Image>();

    @Override
    public void create() {
        super.create();
        addActors();
    }

    private void addActors() {
        woodLabel = new Label(String.format("Wood: %d", woodCount), skin);
        stoneLabel = new Label(String.format("Stone: %d", stoneCount), skin);
        metalLabel = new Label(String.format("Metal: %d", metalCount), skin);

        resourceLabel = new Image(ServiceLocator.getResourceService().getAsset("images/resource_display.png", Texture.class));

        woodLabel.setFontScale(1.5f);
        stoneLabel.setFontScale(1.5f);
        metalLabel.setFontScale(1.5f);
        resourceLabel.setSize(750, 750);

        stage.addActor(resourceLabel);
        stage.addActor(woodLabel);
        stage.addActor(stoneLabel);
        stage.addActor(metalLabel);

    }

    @Override
    public void draw(SpriteBatch batch)  {
        // change position in case of window resize
        int screenHeight = Gdx.graphics.getHeight();
        int screenWidth = Gdx.graphics.getWidth();
        float offsetX = 10f;
        float offsetY = 80f;
        resourceLabel.setPosition(screenWidth - 515f, screenHeight - offsetY - 590f);
        woodLabel.setPosition(screenWidth - 200f, screenHeight - offsetY - 30f);
        stoneLabel.setPosition(screenWidth - 200f, screenHeight - offsetY - 60f);
        metalLabel.setPosition(screenWidth - 200f, screenHeight - offsetY - 90f);
    }

    /**
     * Updates the player's resource stats on the ui.
     * @param resource player resource to update
     * @param count player resource count
     */
    public void updatePlayerResourceUI(String resource, int count) {
        CharSequence text = String.format("%s: %d", resource, count);

        switch (resource) {
            case "wood" -> woodLabel.setText(text);
            case "stone" -> stoneLabel.setText(text);
            case "metal" -> metalLabel.setText(text);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        woodLabel.remove();
        stoneLabel.remove();
        metalLabel.remove();

    }
}
