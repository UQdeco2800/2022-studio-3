package com.deco2800.game.entities.factories;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.building.*;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.configs.*;
import com.deco2800.game.files.FileLoader;
import com.deco2800.game.map.MapComponent;
import com.deco2800.game.physics.PhysicsLayer;
import com.deco2800.game.physics.components.ColliderComponent;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.HighlightedTextureRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.components.friendlyunits.SelectableComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.type.BaseComponent;
import com.deco2800.game.worker.resources.ResourceConfig;

/**
 * Factory to create a building entity with predefined components.
 * <p> Each building entity type should have a creation method that returns a corresponding entity.
 * <p> Predefined buildings properties are loaded from a config stored as a json file and should have
 * the properties stored in 'BuildingConfigs'.
 */
public class BuildingFactory {
    private static final BuildingConfigs configs =
            FileLoader.readClass(BuildingConfigs.class, "configs/buildings.json");
    private static final ResourceConfig stats =
            FileLoader.readClass(ResourceConfig.class, "configs/base.json");

    /**
     * Width in tiles of a wall pillar entity
     */
    public static final float CORNER_SCALE = 2f;

    /**
     * Width in tiles of wall connectors and gates - do not change as they are contingent on CORNER_SCALE
     */
    public static final float CONNECTOR_SCALE = 2f * CORNER_SCALE;
    public static final float GATE_SCALE = (2f * CORNER_SCALE) + CONNECTOR_SCALE;

    /**
     * Use this as a base entity for creating buildings
     * @return a new Entity with universal building components
     */
    public static Entity createBaseBuilding() {
        return new Entity()
                .addComponent(new PhysicsComponent().setBodyType(BodyDef.BodyType.StaticBody))
                .addComponent(new SelectableComponent())
                .addComponent(ServiceLocator.getInputService().getInputFactory().createForFriendlyUnit())
                .addComponent(new ColliderComponent().setLayer(PhysicsLayer.OBSTACLE));
    }

    /**
     * Creates entity, adds and configures TownHall components
     * @return TownHall Entity
     */
    public static Entity createTownHall() {
        final float TH_SCALE = 7f;
        Entity townHall = createBaseBuilding();
        TownHallConfig config = configs.townHall;

        Vector2 leftPoint = new Vector2(21f, 632f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(500f, 856f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(507f, 359f); //NW edge

        float[] selectionPoints = new float[] {
                2f, 608f,
                505f, 855f,
                962f, 507f,
                941f, 154f,
                506f, 108f,
                179f, 346f
        };

        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.BROWN);
        townHall.addComponent(new TextureRenderComponent("images/base.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new ResourceStatsComponent(stats.wood, stats.stone, stats.metal))
                .addComponent(new BaseComponent())
                .addComponent(new HighlightedTextureRenderComponent("images/Base_Highlight.png"))
                .addComponent(mp)
                .addComponent(new TextureScaler(leftPoint, maxX, maxY))
                .addComponent(new SelectionCollider());

        townHall.getComponent(TextureScaler.class).setPreciseScale(TH_SCALE, true);
        // Setting Isometric Collider

        //Add precise selection collider
        townHall.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        townHall.setEntityName("TownHall");

        // Points (in pixels) on the texture to set the collider to
        float[] points = new float[] {      // Four vertices
                31f, 607f,      // Vertex 0       3--2
                499f, 835f,     // Vertex 1      /  /
                958f, 515f,     // Vertex 2     /  /
                486f, 289f      // Vertex 3    0--1
        };
        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/base.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(townHall.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();  // Collider shape
        boundingBox.set(vertices);
        townHall.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return townHall;
    }

    /**
     * Creates entity, adds and configures Barracks components
     * @return Barracks Entity
     */
    public static Entity createBarracks() {
        final float BARRACKS_SCALE = 4f;
        Entity barracks = createBaseBuilding();
        BarracksConfig config = configs.barracks;

        Vector2 leftPoint = new Vector2(155f, 858f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(591f, 1037f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(605f, 675f); //NW edge

        // Points (in pixels) on the texture to set the collider to
        float[] points = new float[]{
                605f, 1036f,    // Vertex 0        3
                982f, 889f,     // Vertex 1    4 /   \ 2
                982f, 761f,     // Vertex 2     |     |
                605f, 581f,     // Vertex 3    5 \   / 1
                222f, 736f,     // Vertex 4        0
                222f, 874f      // Vertex 5
        };

        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.GOLDENROD);
        barracks.addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"))
                .addComponent(new BuildingActions(config.type, config.level))
                .addComponent(new HighlightedTextureRenderComponent("images/barracks_level_1.0_Highlight.png"))
                .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence))
                .addComponent(new TextureScaler(leftPoint, maxX, maxY))
                .addComponent(mp)
                .addComponent(new SelectionCollider());

        barracks.getComponent(TextureScaler.class).setPreciseScale(BARRACKS_SCALE, true);

        barracks.getComponent(SelectionCollider.class).setPoints(points);

        // Setting Isometric Collider
        // Defines a polygon shape on top of a texture region
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/barracks_level_1.0.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(barracks.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        barracks.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return barracks;
    }

    /**
     * Creates a farm building entity to be spawned in the game.
     * @return farm entity
     */
    public static Entity createFarm() {
        Entity farm = createBaseBuilding();
        final float FARM_SCALE = 5f;

        Vector2 leftPoint = new Vector2(0f, 220f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(207f, 322f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(247f, 95f); //NW edge

        //Define selection hitbox
        float[] selectionPoints = new float[] {
                27f, 203f,
                116f, 243f,
                256f, 172f,
                262f, 114f,
                213f, 50f,
                159f, 66f,
                22f, 140f
        };

        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.ORANGE);
        farm.addComponent(new TextureRenderComponent("images/farm.png"))
               .addComponent(mp)
               .addComponent(new HighlightedTextureRenderComponent("images/highlightedFarm.png"))
               .addComponent(new TextureScaler(leftPoint, maxX, maxY))
               .addComponent(new SelectionCollider());

        farm.getComponent(TextureScaler.class).setPreciseScale(FARM_SCALE, true);

        //Set selection hitbox
        farm.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        // Methodology sourced from BuildingFactory.java:createTownHall()
        float[] points = new float[] {      // Four vertices
            33f, 199f,      // Vertex 0       3--2
            113f, 240f,     // Vertex 1      /  /
            253f, 173f,     // Vertex 2     /  /
            169f, 137f      // Vertex 3    0--1
        };
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/farm.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(FARM_SCALE);
        }
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.set(vertices);
        farm.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return farm;
    }

    /**
     * Creates entity, adds and configures Wall components
     * @return wall Entity
     */
    public static Entity createWall() {
        Entity wall = createBaseBuilding();
        WallConfig config = configs.wall;


        wall.addComponent(new TextureRenderComponent("images/wooden_wall.png"))
            .addComponent(new BuildingActions(config.type, config.level))
            .addComponent(new CombatStatsComponent(config.health, config.baseAttack, config.baseDefence));

        wall.scaleWidth(2.2f);
        // Setting Isometric Collider (Normal collider rotated 60 degrees)
        PolygonShape boundingBox = new PolygonShape();
        Vector2 center = wall.getCenterPosition(); // Collider to be set around center of entity
        boundingBox.setAsBox(center.x * 0.25f, center.y * 0.25f, center, (float) (60 * Math.PI / 180));
        wall.getComponent(ColliderComponent.class).setShape(boundingBox);

        return wall;
    }

    public static Entity createCornerWall() {
        Entity cornerWall = createBaseBuilding();
        //Set up building points for texture scaling
        Vector2 leftPoint = new Vector2(88f, 153f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(120f, 134f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(120f, 139f); //NW edge

        //Points of image used to create a polygon around the image for selection
        float[] selectionPoints = new float[] {
                87f, 61f,
                89f, 154f,
                121f, 173f,
                153f, 153f,
                156f, 64f,
                122f, 43f
        };

        //Set up building points for isometric collider
        float[] points = new float[] {
                88f, 153f,
                120f, 170f,
                152f, 152f,
                119, 138
        };
        cornerWall.addComponent(new TextureRenderComponent("images/wall_pillar.png"))
                .addComponent(new TextureScaler(leftPoint, maxX, maxY))
                .addComponent(new SelectionCollider());

        //Scale edge wall precisely
        cornerWall.getComponent(TextureScaler.class).setPreciseScale(CORNER_SCALE, true);

        //Set selection collider
        cornerWall.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(cornerWall.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        cornerWall.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return cornerWall;
    }

    /**
     * Creates and returns a library building entity
     * @return library
     */
    public static Entity createLibrary() {
        Entity library = createBaseBuilding();
        final float LIBRARY_SCALE = 5f;

        Vector2 leftPoint = new Vector2(69f, 351f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(280f, 457f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(281f, 260f); //NW edge

        //Define selection hitbox
        float[] selectionPoints = new float[] {
                78f, 355f,
                195f, 412f,
                367f, 411f,
                444f, 370f,
                431f, 175f,
                205f, 120f,
                111f, 160f,
                78f, 220f
        };

        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.ORANGE);
        library.addComponent(new TextureRenderComponent("images/library.png"))
               .addComponent(mp)
               .addComponent(new HighlightedTextureRenderComponent("images/highlightedLeftFacingLibrary.png"))
               .addComponent(new TextureScaler(leftPoint, maxX, maxY))
               .addComponent(new SelectionCollider());

        library.getComponent(TextureScaler.class).setPreciseScale(LIBRARY_SCALE, true);

        //Add selection hitbox
        library.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        // Methodology sourced from BuildingFactory.java:createTownHall()
        float[] points = new float[] {      // Six vertices
            81f, 354f,
            191f, 411f,
            276f, 370f,
            361f, 420f,
            446f, 375f,
            247f, 273f
        };
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/library.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(library.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.set(vertices);
        library.getComponent(ColliderComponent.class).setShape(boundingBox);

        return library;
    }

    /**
     * Creates and returns a blacksmith building entity
     * @return blacksmith entity
     */
    public static Entity createBlacksmith() {
        Entity bs = createBaseBuilding();
        final float BLACKSMITH_SCALE = 5f;

        Vector2 leftPoint = new Vector2(5f, 176f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(123f, 251f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(115f, 143f); //NW edge

        //Define pixel points for selecting a blacksmith
        float[] selectionPoints = new float[] {
                12f, 47f,
                13f, 186f,
                145f, 252f,
                247f, 191f,
                246f, 151f,
                209f, 113f,
                96f, 54f
        };

        MapComponent mp = new MapComponent();
        mp.display();
        mp.setDisplayColour(Color.BLACK);
        bs.addComponent(new TextureRenderComponent("images/blacksmith.png"))
          .addComponent(new HighlightedTextureRenderComponent("images/highlightedBlacksmith.png"))
          .addComponent(mp)
          .addComponent(new TextureScaler(leftPoint, maxX, maxY))
          .addComponent(new SelectionCollider());

        bs.getComponent(TextureScaler.class).setPreciseScale(BLACKSMITH_SCALE, true);

        //Add selection hitbox
        bs.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        // Methodology sourced from BuildingFactory.java:createTownHall()
        float[] points = new float[] {      // Four vertices
            5f, 176f,       // Vertex 0        3--2
            123f, 251f,     // Vertex 1      /  /
            244f, 192f,     // Vertex 2     /  /
            126f, 117f      // Vertex 3    0--1
        };
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/blacksmith.png", Texture.class)), points, null);
        float[] cords = region.getTextureCoords();

        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < cords.length / 2; i++) {
            vertices[i] = new Vector2(cords[2*i], cords[2*i+1]).scl(bs.getScale().x);
        }
        PolygonShape boundingBox = new PolygonShape();
        boundingBox.set(vertices);
        bs.getComponent(ColliderComponent.class).setShape(boundingBox);
        
        return bs;
    }

    /**
     * Creates a connector between wall pillars, oriented to face north/south
     * @return wall connector oriented to face north/south
     */
    public static Entity createNSConnector() {
        Entity connector = createBaseBuilding();

        //Set up building points for texture scaling
        //Vector2 leftPoint = new Vector2(71f, 136f); //Bottom leftmost edge in pixels - offset slightly to centre in wall
        Vector2 leftPoint = new Vector2(78f, 131f); //Bottom leftmost edge in pixels
        Vector2 maxX= new Vector2(138f, 162f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(88f, 123f);  //NW edge

        //Define selection collider points
        float[] selectionPoints = new float[] {
                89f, 48f,
                79f, 54f,
                78f, 132f,
                138f, 162f,
                145f, 156f,
                145f, 76f
        };

        //Set up building points for isometric collider
        float[] points = new float[] {
                79f, 131f,
                138f, 162f,
                145f, 156f,
                86f, 126f
        };

        connector.addComponent(new TextureRenderComponent("images/connector_ns.png"))
                .addComponent(new TextureScaler(leftPoint, maxX, maxY))
                .addComponent(new SelectionCollider());

        //Scale connector precisely
        connector.getComponent(TextureScaler.class).setPreciseScale(CONNECTOR_SCALE, true);

        //Set selection collider
        connector.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        //Set isometric collider
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(connector.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        connector.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return connector;
    }

    /**
     * Creates a connector between wall pillars, oriented to face east/west
     * @return wall connector oriented to face east/west
     */
    public static Entity createEWConnector() {
        Entity connector = createBaseBuilding();

        //Set up building points for texture scaling
        //Vector2 leftPoint = new Vector2(73f, 147f); //Bottom leftmost edge in pixels - offset slightly to centre in wall
        //-6 in x, -5 in y
        Vector2 leftPoint = new Vector2(79f, 152f); //Bottom leftmost edge in pixels - offset slightly to centre in wall
        Vector2 maxY = new Vector2(138f, 124f); //Bottom rightmost edge in pixels
        Vector2 maxX = new Vector2(87f, 158f);  //NW edge

        float[] selectionPoints = new float[] {
                79f, 73f,
                79f, 155f,
                87f, 159f,
                146f, 128f,
                145f, 50f,
                135f, 45f
        };

        //Set up building points for isometric collider
        float[] points = new float[] {
                79f, 152f,
                87f, 159f,
                146f, 127f,
                138f, 124f
        };

        connector.addComponent(new TextureRenderComponent("images/connector_ew.png"))
                .addComponent(new TextureScaler(leftPoint, maxX, maxY))
                .addComponent(new SelectionCollider());

        //Scale connector precisely
        connector.getComponent(TextureScaler.class).setPreciseScale(CONNECTOR_SCALE, false);

        //Add selection collider
        connector.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        //Set isometric collider
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(connector.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        connector.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return connector;
    }


    /**
     * Creates a north/south facing gate entity, that allows friendly units to leave and enter the city
     * @return North/South Gate Entity
     */
    public static Entity createNSGate() {
        Entity gate = createBaseBuilding();

        //Create animation component
        TextureAtlas gateAnimationAtlas = ServiceLocator.getResourceService().getAsset("images/ns_gate.atlas", TextureAtlas.class);
        AnimationRenderComponent gateARC = new AnimationRenderComponent(gateAnimationAtlas);
        gateARC.addAnimation("open_gate", 0.1f, Animation.PlayMode.NORMAL);
        gateARC.addAnimation("close_gate", 0.1f, Animation.PlayMode.NORMAL);

        //Set up building points
        Vector2 leftPoint = new Vector2(37f, 125f); //Bottom leftmost edge in pixels
        Vector2 maxX = new Vector2(170f, 196f); //Bottom rightmost edge in pixels
        Vector2 maxY = new Vector2(68f, 107f);  //NW edge

        //Define Selection hitbox
        float[] selectionPoints = new float[] {
                34f, 36f,
                36f, 127f,
                173f, 197f,
                206f, 178f,
                206f, 89f,
                70f, 19f
        };

        //Set up building points for isometric collider
        float[] points = new float[] {
                37f, 125f,
                170f, 196f,
                202f, 178f,
                63f, 109f
        };


        //Add all components
        gate.addComponent(new TextureRenderComponent("images/gate_ns_closed.png"))
            .addComponent(new GateCollider())
            .addComponent(gateARC)
            .addComponent(new TextureScaler(leftPoint, maxX, maxY))
            .addComponent(new BuildingActions(Building.GATE_NS, 1))
            .addComponent(new SelectionCollider());
        
        //Scale building precisely
        gate.getComponent(TextureScaler.class).setPreciseScale(GATE_SCALE, true);


        //Add Selection hitbox
        gate.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        //Set isometric collider
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(gate.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        gate.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return gate;
    }

    /**
     * Creates a east/west facing gate entity, that allows friendly units to leave and enter the city
     * @return East/West facing Gate Entity
     */
    public static Entity createEWGate() {
        Entity gate = createBaseBuilding();

        //Create animation component
        TextureAtlas gateAnimationAtlas = ServiceLocator.getResourceService().getAsset("images/ew_gate.atlas", TextureAtlas.class);
        AnimationRenderComponent gateARC = new AnimationRenderComponent(gateAnimationAtlas);
        gateARC.addAnimation("open_gate", 0.1f, Animation.PlayMode.NORMAL);
        gateARC.addAnimation("close_gate", 0.1f, Animation.PlayMode.NORMAL);

        //Define selection collider
        float[] selectionPoints = new float[] {
                38f, 87f,
                38f, 181f,
                71f, 196f,
                204f, 125f,
                204f, 37f,
                172f, 18f
        };

        //Set up building points
        Vector2 leftPoint = new Vector2(37f, 178f); //Bottom leftmost edge in pixels
        Vector2 maxY = new Vector2(170f, 113f); //Bottom rightmost edge in pixels
        Vector2 maxX = new Vector2(70f, 196f);

        //Set up building points for isometric collider
        float[] points = new float[] {
                37f, 178f,
                70f, 197f,
                203f, 125f,
                170f, 113f
        };

        //Add all components
        gate.addComponent(new TextureRenderComponent("images/gate_ew_closed.png"))
                .addComponent(new GateCollider())
                .addComponent(gateARC)
                .addComponent(new TextureScaler(leftPoint, maxX, maxY))
                .addComponent(new BuildingActions(Building.GATE_EW, 1))
                .addComponent(new SelectionCollider());

        //Scale building precisely
        gate.getComponent(TextureScaler.class).setPreciseScale(GATE_SCALE, false);
        
        //Add selection hitbox
        gate.getComponent(SelectionCollider.class).setPoints(selectionPoints);

        //Set isometric collider
        PolygonRegion region = new PolygonRegion(new TextureRegion(ServiceLocator.getResourceService()
                .getAsset("images/wall_pillar.png", Texture.class)), points, null);

        float[] coords = region.getTextureCoords();
        Vector2[] vertices = new Vector2[region.getTextureCoords().length / 2];
        for (int i = 0; i < coords.length / 2; i++) {
            vertices[i] = new Vector2(coords[2*i], coords[2*i+1]).scl(gate.getScale().x);
        }

        PolygonShape boundingBox = new PolygonShape(); // Collider shape
        boundingBox.set(vertices);
        gate.getComponent(ColliderComponent.class).setShape(boundingBox); // Setting Isometric Collider

        return gate;
    }

    private BuildingFactory() {
        throw new IllegalStateException("Instantiating static util class");
    }
}
