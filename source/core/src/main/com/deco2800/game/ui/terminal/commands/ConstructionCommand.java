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
    private ConstructionInputComponent activeBuild;

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
                return construct(BuildingType.BARRACKS);
            case "wall":
            case "2":
                return construct(BuildingType.WALL);
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

    /**
     * Takes a BuildingType and produces a corresponding input component and
     * highlight
     * @param type which building type we want to build
     * @return whether or not the input component was successfully created
     */
    public boolean construct(BuildingType type) {

        if (activeBuild != null && !activeBuild.isFinished()) {
            return false;
        }

        String buildPath;
        switch (type) {
            case BARRACKS -> buildPath = "images/barracks_highlight_green.png";
            case WALL -> buildPath = "images/wooden_wall_green.png";
            default -> {
                return false;
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
        // gotta be a cleaner way to set this up
        if (activeBuild != null)
            activeBuild.dispose();
        activeBuild =
                new ConstructionInputComponent(constructHighlight, type);
        ServiceLocator.getInputService().register(activeBuild);
        return true;
    }

    public boolean isFinished() {
        return activeBuild == null || activeBuild.isFinished();
    }
}
