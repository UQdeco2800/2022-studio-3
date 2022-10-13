package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.components.LoadingBar;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoadingScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(LoadingScreen.class);
    private final GdxGame game;
    private final Renderer renderer;
    private Entity ui;

    private final String[] forestTextures = AtlantisGameArea.forestTextures;

    public static final String[] uiTextures = AtlantisGameArea.uiTextures;
    public static final String[] forestTextureAtlases = AtlantisGameArea.forestTextureAtlases;
    public static final String[] atlantisSounds = AtlantisGameArea.atlantisSounds;
    public static final String[] buildingPlacementTextures = AtlantisGameArea.buildingPlacementTextures;

    private static final String[] assets = {
            "images/title-atlantis.png",
            "images/loading bar_4.png",
            "images/loading bar_5.png",
    };

    public LoadingScreen(GdxGame game) {
        this.game = game;
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());

        logger.debug("Initialising Loading Screen");
        renderer = RenderFactory.createRenderer();
        loadingBarAssets();
        loadGameAssets();
        createLoadingBar();
    }

    @Override
    public void render(float delta) {
        ServiceLocator.getEntityService().update();
        if (ui.getComponent(LoadingBar.class).isLoaded()) {
            game.setScreen(GdxGame.ScreenType.MAIN_GAME);
        }
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    /**
     * Load assets required by loading screen
     */
    private void loadingBarAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(assets);
        resourceService.loadAll();
    }

    /**
     * Unloads loading screen assets
     */
    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(assets);
    }

    /**
     * Creates the background and loading bar to be displayed to the screen
     */
    private void createLoadingBar() {
        logger.debug("Creating loading bar");
        ui = new Entity().addComponent(new LoadingBar());
        ServiceLocator.getEntityService().register(ui);
    }

    /**
     * Assets required by AtlantisGameArea added to loading queue
     */
    private void loadGameAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(forestTextures);
        resourceService.loadTextures(uiTextures);
        resourceService.loadTextureAtlases(forestTextureAtlases);
        resourceService.loadSounds(atlantisSounds);
        resourceService.loadTextures(buildingPlacementTextures);
    }

    public void dispose() {
        logger.debug("Disposing Loading Screen");
        renderer.dispose();
        unloadAssets();
        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
    }
}
