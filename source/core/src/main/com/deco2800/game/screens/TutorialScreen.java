package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.TutorialGameArea;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.maingame.*;
import com.deco2800.game.components.pausemenu.PauseMenuActions;
import com.deco2800.game.components.pausemenu.PauseMenuDisplay;
import com.deco2800.game.components.resources.ResourceCountDisplay;
import com.deco2800.game.components.story.StoryActions;
import com.deco2800.game.components.story.StoryDisplay;
import com.deco2800.game.components.tutorial.TutorialDisplay;
import com.deco2800.game.components.tutorial.TutorialActions;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.entities.factories.RenderFactory;
import com.deco2800.game.input.InputComponent;
import com.deco2800.game.input.InputDecorator;
import com.deco2800.game.input.InputService;
import com.deco2800.game.map.MapService;
import com.deco2800.game.physics.PhysicsEngine;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.rendering.Renderer;
import com.deco2800.game.services.GameTime;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.ui.terminal.Terminal;
import com.deco2800.game.ui.terminal.TerminalDisplay;
import com.deco2800.game.components.weather.WeatherIconDisplay;
import com.deco2800.game.components.resources.ResourceCountDisplay;
import com.deco2800.game.components.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the tutorial game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 * TODO: A new screen should be made for this - TutorialGameArea.java showing a simpler area
 * TODO: Integrate with existing actions (detect event and change dialogue response)
 */
public class TutorialScreen extends ScreenAdapter {
    private static final Logger logger = LoggerFactory.getLogger(TutorialScreen.class);
    private static final String[] mainGameTextures = {
            "images/heart.png",
            "images/bigblack.png",
            "images/resource_display.png",
            "images/gainstone.png",
            "images/gain10wood.png",
            "images/gainmetal.png",
            "images/Information_Box_Deepsea.png"
    };

    private static final Vector2 CAMERA_POSITION = new Vector2(11.5f, 2.5f);

    private final GdxGame game;
    private final Renderer renderer;
    private final PhysicsEngine physicsEngine;
    public DialogueBoxDisplay display = new DialogueBoxDisplay();

    public TutorialScreen(GdxGame game) {
        this.game = game;
        this.display = new DialogueBoxDisplay();


        logger.debug("Initialising main game screen services");
        ServiceLocator.registerTimeSource(new GameTime());

        PhysicsService physicsService = new PhysicsService();
        ServiceLocator.registerPhysicsService(physicsService);
        physicsEngine = physicsService.getPhysics();

        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerResourceService(new ResourceService());



        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
        ServiceLocator.registerMapService(new MapService());

        ServiceLocator.registerMapService(new MapService());

        ServiceLocator.registerMapService(new MapService());

        ServiceLocator.registerMapService(new MapService());

        renderer = RenderFactory.createRenderer();
        renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
        renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

        loadAssets();
        createUI();


        logger.debug("Initialising main game screen entities");

        // Create game area as an AtlantisGameArea with an AtlantisTerrainFactory
        AtlantisTerrainFactory terrainFactory = new AtlantisTerrainFactory(renderer.getCamera());
        TutorialGameArea tutorialGameArea = new TutorialGameArea(terrainFactory, this.display);
        tutorialGameArea.create();
    }

    @Override
    public void render(float delta) {
        physicsEngine.update();
        ServiceLocator.getEntityService().update();
        renderer.render();
    }

    @Override
    public void resize(int width, int height) {
        renderer.resize(width, height);
        logger.trace("Resized renderer: ({} x {})", width, height);
    }

    @Override
    public void pause() {
        logger.info("Game paused");
    }

    @Override
    public void resume() {
        logger.info("Game resumed");
    }

    @Override
    public void dispose() {
        logger.debug("Disposing tutorial game screen");

        renderer.dispose();
        unloadAssets();

        ServiceLocator.getEntityService().dispose();
        ServiceLocator.getRenderService().dispose();
        ServiceLocator.getResourceService().dispose();

        ServiceLocator.clear();
    }

    private void loadAssets() {
        logger.debug("Loading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.loadTextures(mainGameTextures);
        ServiceLocator.getResourceService().loadAll();
    }

    private void unloadAssets() {
        logger.debug("Unloading assets");
        ResourceService resourceService = ServiceLocator.getResourceService();
        resourceService.unloadAssets(mainGameTextures);
    }

    /**
     * Creates the main game's ui including components for rendering ui elements to the screen and
     * capturing and handling ui input.
     */
    private void createUI() {
        logger.debug("Creating ui");
        Stage stage = ServiceLocator.getRenderService().getStage();
        InputComponent inputComponent =
                ServiceLocator.getInputService().getInputFactory().createForTerminal();

        Entity ui = new Entity();

        // main game components
        ui.addComponent(new InputDecorator(stage, 9))
                .addComponent(new WeatherIconDisplay())
                .addComponent(new PauseMenuDisplay(this.game))
                .addComponent(new PauseMenuActions(this.game))
                .addComponent(new PerformanceDisplay())
                .addComponent(new MainGameActions(this.game))
                .addComponent(new MainGameExitDisplay())
                .addComponent(new Terminal())
                .addComponent(inputComponent)
                .addComponent(new ResourceCountDisplay())
                .addComponent(new TerminalDisplay());

        // tutorial components
        ui.addComponent(new TutorialDisplay())
                .addComponent(new InputDecorator(stage, 10))
                .addComponent(new TutorialActions(game, this.display));

        ServiceLocator.getEntityService().register(ui);
    }
}
