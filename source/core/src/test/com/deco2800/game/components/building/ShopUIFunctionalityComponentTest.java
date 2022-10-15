package com.deco2800.game.components.building;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ServiceLoader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.components.CombatStatsComponent;
import com.deco2800.game.components.friendly.FriendlyComponent;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.worker.components.type.BaseComponent;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ShopUIFunctionalityComponentTest {

    @BeforeEach
    public void setup() {
        ServiceLocator.registerEntityService(new EntityService());

        BaseComponent baseComponent = mock(BaseComponent.class);
        Entity e = new Entity();
        e.addComponent(new BuildingActions(Building.TOWNHALL, 1))
         .addComponent(baseComponent);
        ServiceLocator.getEntityService().register(e);

        when(baseComponent.getWood()).thenReturn(100);
        when(baseComponent.getMetal()).thenReturn(100);
        when(baseComponent.getStone()).thenReturn(100);
    }

    @Test
    public void spendResourcesTest() {
        assertTrue(ShopUIFunctionalityComponent.spendResources(-10, -10, -10));
    }

    @Test
    public void onUnitUpgradeTest() {
        Entity e = new Entity();
        CombatStatsComponent csc = new CombatStatsComponent(10, 10, 10);
        e.addComponent(csc)
         .addComponent(new FriendlyComponent());
        ServiceLocator.getEntityService().register(e);

        RenderService rs = new RenderService();
        Stage s = mock(Stage.class);
        rs.setStage(s);
        when(s.getActors()).thenReturn(new Array<>());
        ServiceLocator.registerRenderService(rs);

        ShopUIFunctionalityComponent shop = new ShopUIFunctionalityComponent();
        shop.onUnitUpgrade();

        assertTrue(csc.getBaseAttack() == 30);
        assertTrue(csc.getBaseDefence() == 30);
        assertTrue(csc.getMaxHealth() == 30);
        assertTrue(csc.getHealth() == 30);
    }

    @Test
    public void onWallUpgradeTest() {
        CombatStatsComponent csc = new CombatStatsComponent(10, 0, 10);
        BuildingActions ba = new BuildingActions(Building.WALL, 1);
        Entity e = new Entity();
        e.addComponent(ba)
         .addComponent(csc);
        ServiceLocator.getEntityService().register(e);

        RenderService rs = new RenderService();
        Stage s = mock(Stage.class);
        rs.setStage(s);
        when(s.getActors()).thenReturn(new Array<>());
        ServiceLocator.registerRenderService(rs);

        ShopUIFunctionalityComponent shop = new ShopUIFunctionalityComponent();
        shop.onWallUpgrade();

        assertTrue(csc.getBaseAttack() == 0);
        assertTrue(csc.getBaseDefence() == 30);
        assertTrue(csc.getMaxHealth() == 110);
        assertTrue(csc.getHealth() == 110);
        assertTrue(ba.getLevel() == 2);
    }
}