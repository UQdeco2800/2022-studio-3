package com.deco2800.game.screens;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.deco2800.game.GdxGame;
import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.MapGenerator.FloodingGenerator;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.components.LoadingBar;
import com.deco2800.game.components.buildingmenu.BuildingMenuButton;
import com.deco2800.game.components.buildingmenu.BuildingMenuDisplay;
import com.deco2800.game.components.maingame.DialogueBoxDisplay;
import com.deco2800.game.components.floodtimer.FloodTimerDisplay;
import com.deco2800.game.components.maingame.MainGameActions;
import com.deco2800.game.components.pausemenu.PauseMenuActions;
import com.deco2800.game.components.pausemenu.PauseMenuDisplay;
import com.deco2800.game.components.resources.ResourceCountDisplay;
import com.deco2800.game.components.soldiermenu.SoldierMenuDisplay;
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
import com.deco2800.game.components.maingame.MainGameExitDisplay;
import com.deco2800.game.components.friendlyunits.gamearea.PerformanceDisplay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The game screen containing the main game.
 *
 * <p>Details on libGDX screens: https://happycoding.io/tutorials/libgdx/game-screens
 */
public class MainGameScreen extends ScreenAdapter {
  private static final Logger logger = LoggerFactory.getLogger(MainGameScreen.class);
  private static final String[] mainGameTextures = {
          "images/heart.png",
          "images/bigblack.png",
          "images/resource_display.png",
          "images/gainstone.png",
          "images/gain10wood.png",
          "images/gainmetal.png",
          "images/character-selection-menu.png",
          "images/building-selection-menu.png",
          "images/CogWheel/Esc Menu/CogWheelBG.png",
          "images/barracks_level_1.0.png",
          "images/wooden_wall.png"
  };
  private static final Vector2 CAMERA_POSITION = new Vector2(11.5f, 2.5f);

  private final GdxGame game;
  private final Renderer renderer;
  private final PhysicsEngine physicsEngine;
  private FloodingGenerator floodingGenerator;

  public MainGameScreen(GdxGame game) {
    this.game = game;

    logger.debug("Initialising main game screen services");
    ServiceLocator.registerTimeSource(new GameTime());

    PhysicsService physicsService = new PhysicsService();
    ServiceLocator.registerPhysicsService(physicsService);
    physicsEngine = physicsService.getPhysics();

    ServiceLocator.registerInputService(new InputService());
//    ServiceLocator.registerResourceService(new ResourceService());
    // Resource Service created in Loading Screen

    ServiceLocator.registerEntityService(new EntityService());
    ServiceLocator.registerRenderService(new RenderService());
    ServiceLocator.registerMapService(new MapService());

    renderer = RenderFactory.createRenderer();
    renderer.getCamera().getEntity().setPosition(CAMERA_POSITION);
    renderer.getDebug().renderPhysicsWorld(physicsEngine.getWorld());

    loadAssets();
    createUI();

    logger.debug("Initialising main game screen entities");
    // Create game area as an AtlantisGameArea with an AtlantisTerrainFactory
    AtlantisTerrainFactory terrainFactory = new AtlantisTerrainFactory(renderer.getCamera());
    AtlantisGameArea atlantisGameArea = new AtlantisGameArea(terrainFactory);
    atlantisGameArea.create();
    this.floodingGenerator = new FloodingGenerator(terrainFactory, atlantisGameArea);
  }

  @Override
  public void render(float delta) {
    physicsEngine.update();
    ServiceLocator.getEntityService().update();
    floodingGenerator.update();
    if (floodingGenerator.status100p) {
      game.setScreen(GdxGame.ScreenType.ENDGAME);
    }
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
    logger.debug("Disposing main game screen");

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
    ui.addComponent(new InputDecorator(stage, 11))
        .addComponent(new WeatherIconDisplay())
        .addComponent(new PauseMenuDisplay(this.game))
        .addComponent(new PauseMenuActions(this.game))
        .addComponent(new PerformanceDisplay())
        .addComponent(new MainGameActions(this.game))
        .addComponent(new MainGameExitDisplay())
        .addComponent(new Terminal())
        .addComponent(inputComponent)
        .addComponent(new ResourceCountDisplay())
        .addComponent(new BuildingMenuButton())
        .addComponent(new SoldierMenuDisplay())
        .addComponent(new TerminalDisplay());

    ServiceLocator.getEntityService().register(ui);
  }
}
