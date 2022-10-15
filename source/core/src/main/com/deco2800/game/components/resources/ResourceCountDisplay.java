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
    private Image stoneImageLabel;
    private Image woodImageLabel;
    private Image metalImageLabel;

    /* Modify these to update the resource count */
    private int woodCount = 0;
    private int stoneCount = 0;
    private int metalCount = 0;


    @Override
    public void create() {
        super.create();
        addActors();
    }

    @Override
    public void update(){
        // Remove old actors and create a new one
        woodLabel.remove();
        stoneLabel.remove();
        metalLabel.remove();
        woodLabel = new Label(String.format(" %d", woodCount), skin);
        stoneLabel = new Label(String.format(" %d", stoneCount), skin);
        metalLabel = new Label(String.format(" %d", metalCount), skin);
        woodLabel.setFontScale(1.5f);
        stoneLabel.setFontScale(1.5f);
        metalLabel.setFontScale(1.5f);
        stage.addActor(woodLabel);
        stage.addActor(stoneLabel);
        stage.addActor(metalLabel);
    }

    private void addActors() {
        woodLabel = new Label(String.format(" %d", woodCount), skin);
        stoneLabel = new Label(String.format(" %d", stoneCount), skin);
        metalLabel = new Label(String.format(" %d", metalCount), skin);

        /* Resource image labels */
        resourceLabel = new Image(ServiceLocator.getResourceService().getAsset("images/resource_display.png", Texture.class));
        stoneImageLabel = new Image(ServiceLocator.getResourceService().getAsset("images/gainstone.png", Texture.class));
        woodImageLabel = new Image(ServiceLocator.getResourceService().getAsset("images/gain10wood.png", Texture.class));
        metalImageLabel = new Image(ServiceLocator.getResourceService().getAsset("images/gainmetal.png", Texture.class));

        woodLabel.setFontScale(1.5f);
        stoneLabel.setFontScale(1.5f);
        metalLabel.setFontScale(1.5f);
        resourceLabel.setSize(640, 600);
        stoneImageLabel.setSize(40, 40);
        woodImageLabel.setSize(40, 40);
        metalImageLabel.setSize(40, 40);

        stage.addActor(resourceLabel);
        stage.addActor(woodImageLabel);
        stage.addActor(stoneImageLabel);
        stage.addActor(metalImageLabel);
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
        resourceLabel.setPosition(screenWidth - 450f, screenHeight - offsetY - 500f);
        stoneImageLabel.setPosition(screenWidth - 200f + 20f, screenHeight - offsetY - 90f);
        metalImageLabel.setPosition(screenWidth - 200f + 20f, screenHeight - offsetY - 130f);
        woodImageLabel.setPosition(screenWidth - 200f + 20f, screenHeight - offsetY - 50f);
        woodLabel.setPosition(screenWidth - 200f + 50f + 10f, screenHeight - offsetY - 40f);
        stoneLabel.setPosition(screenWidth - 200f + 50f + 10f, screenHeight - offsetY - 80f);
        metalLabel.setPosition(screenWidth - 200f + 50f + 10f, screenHeight - offsetY - 120f);
    }

    /**
     * Updates the player's resource stats on the ui.
     * @param resource player resource to update
     * @param count player resource count
     */
    public void updatePlayerResourceUI(String resource, int count) {
        switch (resource) {
            case "wood" -> woodCount = count;
            case "stone" -> stoneCount = count;
            case "metal" -> metalCount = count;
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        woodLabel.remove();
        stoneLabel.remove();
        metalLabel.remove();
        woodImageLabel.remove();
        stoneImageLabel.remove();
        metalImageLabel.remove();

    }
}
