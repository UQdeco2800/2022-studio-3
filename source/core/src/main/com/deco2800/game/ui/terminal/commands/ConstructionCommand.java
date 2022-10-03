package com.deco2800.game.ui.terminal.commands;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.building.ConstructionInputComponent;
import com.deco2800.game.entities.BuildingType;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Locale;

public class ConstructionCommand implements Command{
    private static final Logger logger = LoggerFactory.getLogger(DebugCommand.class);
    private Entity constructHighlight =
            new Entity();

    private final String[] requisiteTextures = {
            "images/barracks_highlight_red.png",
            "images/barracks_highlight_green.png",
            "images/wooden_wall_green.png",
            "images/wooden_wall_red.png"
    };

 public ConstructionCommand() {
     ServiceLocator.getResourceService().loadTextures(requisiteTextures);
 }

    @Override
    public boolean action(ArrayList<String> args) {
        if (args.size() != 1) {
            logger.debug("Invalid arguments received for 'build' command: {}",
                    args);
            return false;
        }
        String arg = args.get(0).toLowerCase(Locale.ROOT);
        switch(arg) {
            case "barracks":
            case "1":
                construct(BuildingType.BARRACKS);
                return true;
            case "wall":
            case "2":
                construct(BuildingType.WALL);
                return true;
            case "town-hall":
            case "town_hall":
            case "townhall":
                logger.debug("Town hall cannot currently be constructed");
                return false;
            default:
                logger.debug("Invalid arguments received for 'build' command: {}",
                        args);
                return false;
        }
    }

    public void construct(BuildingType type) {
        String buildPath;
        switch (type) {
            case BARRACKS -> buildPath = "images/barracks_highlight_green.png";
            case WALL -> buildPath = "images/wooden_wall_green.png";
            default -> {
                return;
            }
        }
        constructHighlight.dispose();
        constructHighlight =
                new Entity().addComponent(new TextureRenderComponent(buildPath));
        // stolen from BuildingFactory, which is kind of a mess
        float BARRACKS_SCALE = 5f;
        float WALL_SCALE = 2.2f;
        switch (type) {
            case BARRACKS ->constructHighlight.scaleWidth(BARRACKS_SCALE);
            case WALL -> constructHighlight.scaleWidth(WALL_SCALE);
        }
        InputComponent constructInput =
                new ConstructionInputComponent(constructHighlight, type);
        ServiceLocator.getInputService().register(constructInput);


    }
}
