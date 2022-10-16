package com.deco2800.game.components.building;

import com.badlogic.gdx.assets.AssetManager;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

@ExtendWith(GameExtension.class)
class BuildingActionsTest {

    @BeforeEach
    void setUp() {
        String[] textures = new String[]{
                // Barracks
                "images/barracks_level_1.0.png",
                // Walls
                "images/wooden_wall.png",
                "images/wooden_wall_2.png",
                "images/wooden_wall_3.png",
        };
        AssetManager assetManager = spy(AssetManager.class);
        ResourceService resourceService = new ResourceService(assetManager);
        resourceService.loadTextures(textures);
        resourceService.loadAll();

        ServiceLocator.registerResourceService(resourceService);
        ServiceLocator.registerEntityService(new EntityService());
    }

    @Test
    void getLevel() {
        assertEquals(new BuildingActions(Building.WALL, 5).getLevel(), 5);
    }

    @Test
    void addLevel() {
        BuildingActions ba = new BuildingActions(Building.WALL, 1);
        Entity e = new Entity().addComponent(ba);
        ServiceLocator.getEntityService().register(e);
        ba.addLevel();
        assertEquals(ba.getLevel(), 2);
        ba.addLevel();
        assertEquals(ba.getLevel(), 3);
    }

    @Test
    void getType() {
        assertEquals(new BuildingActions(Building.WALL, 5).getType(), Building.WALL);
        assertEquals(new BuildingActions(Building.TOWNHALL, 5).getType(), Building.TOWNHALL);
        assertEquals(new BuildingActions(Building.BARRACKS, 5).getType(), Building.BARRACKS);
    }

    @Test
    void isWall() {
        assertTrue(BuildingActions.isWall(Building.WALL));
        assertTrue(BuildingActions.isWall(Building.WALL_NE));
        assertTrue(BuildingActions.isWall(Building.WALL_SE));
        assertFalse(BuildingActions.isWall(Building.TOWNHALL));
        assertFalse(BuildingActions.isWall(Building.BARRACKS));
    }

    @Test
    void setWallDefault() {
        Entity wall = new Entity().addComponent(new BuildingActions(Building.WALL_NE, 1));
        wall.getComponent(BuildingActions.class).setWallDefault();
        assertEquals(Building.WALL_NE, wall.getComponent(BuildingActions.class).getType());
        wall.addComponent(new TextureRenderComponent("images/wooden_wall.png"));
        wall.getComponent(BuildingActions.class).setWallDefault();
        assertEquals(Building.WALL, wall.getComponent(BuildingActions.class).getType());


        Entity barracks = new Entity().addComponent(new BuildingActions(Building.BARRACKS, 1));
        barracks.addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"));
        barracks.getComponent(BuildingActions.class).setWallDefault();
        assertEquals(Building.BARRACKS, barracks.getComponent(BuildingActions.class).getType());
    }

    @Test
    void setWallNE() {
        Entity wall = new Entity().addComponent(new BuildingActions(Building.WALL, 1));
        wall.getComponent(BuildingActions.class).setWallNE();
        assertEquals(Building.WALL, wall.getComponent(BuildingActions.class).getType());
        wall.addComponent(new TextureRenderComponent("images/wooden_wall_2.png"));
        wall.getComponent(BuildingActions.class).setWallNE();
        assertEquals(Building.WALL_NE, wall.getComponent(BuildingActions.class).getType());


        Entity barracks = new Entity().addComponent(new BuildingActions(Building.BARRACKS, 1));
        barracks.addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"));
        barracks.getComponent(BuildingActions.class).setWallNE();
        assertEquals(Building.BARRACKS, barracks.getComponent(BuildingActions.class).getType());
    }

    @Test
    void setWallSE() {
        Entity wall = new Entity().addComponent(new BuildingActions(Building.WALL, 1));
        wall.getComponent(BuildingActions.class).setWallSE();
        assertEquals(Building.WALL, wall.getComponent(BuildingActions.class).getType());
        wall.addComponent(new TextureRenderComponent("images/wooden_wall_3.png"));
        wall.getComponent(BuildingActions.class).setWallSE();
        assertEquals(Building.WALL_SE, wall.getComponent(BuildingActions.class).getType());


        Entity barracks = new Entity().addComponent(new BuildingActions(Building.BARRACKS, 1));
        barracks.addComponent(new TextureRenderComponent("images/barracks_level_1.0.png"));
        barracks.getComponent(BuildingActions.class).setWallSE();
        assertEquals(Building.BARRACKS, barracks.getComponent(BuildingActions.class).getType());
    }
}