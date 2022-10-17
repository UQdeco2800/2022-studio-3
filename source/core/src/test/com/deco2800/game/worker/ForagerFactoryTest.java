package com.deco2800.game.worker;

import com.deco2800.game.worker.components.CollectStatsComponent;
import com.deco2800.game.worker.components.ResourceCollectComponent;
import com.deco2800.game.worker.components.ResourceStatsComponent;
import com.deco2800.game.worker.components.WorkerInventoryComponent;
import com.deco2800.game.worker.components.type.ForagerComponent;
import com.deco2800.game.worker.components.type.MinerComponent;
import com.deco2800.game.worker.type.ForagerFactory;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.input.InputService;
import com.deco2800.game.physics.PhysicsService;
import com.deco2800.game.rendering.RenderService;
import com.deco2800.game.services.ResourceService;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.entities.EntityService;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.Gdx;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(GameExtension.class)
@ExtendWith(MockitoExtension.class)
public class ForagerFactoryTest {

    private static Entity forager1;

    @BeforeEach
    void BeforeEach() {
        ServiceLocator.clear();
        TextureAtlas builderAtlas = new TextureAtlas(Gdx.files.internal("images/forager.atlas"));
        ResourceService rs = mock(ResourceService.class);
        when(rs.getAsset(anyString(), eq(TextureAtlas.class))).thenReturn(builderAtlas);
        ServiceLocator.registerResourceService(rs);
        ServiceLocator.registerInputService(new InputService());
        ServiceLocator.registerPhysicsService(new PhysicsService());
        ServiceLocator.registerEntityService(new EntityService());
        ServiceLocator.registerRenderService(new RenderService());
    }

    @Test
    public void shouldDetectForagerComponents() {
        forager1 = ForagerFactory.createForager();
        ForagerComponent foragerComponent = forager1.getComponent(ForagerComponent.class);
        MinerComponent minerComponent = forager1.getComponent(MinerComponent.class);
        assertNotNull(foragerComponent);
        assertNull(minerComponent);
        assertEquals(1, foragerComponent.getIsForager());
    }

    @Test
    public void shouldDetectCollectStatsComponent(){
        forager1 = ForagerFactory.createForager();
        CollectStatsComponent forager_collect_stats = forager1.getComponent(CollectStatsComponent.class);
        assertEquals(2, forager_collect_stats.getCollectionAmount());
    }

    @Test
    public void shouldCollectAndUnloadResources(){
        forager1 = ForagerFactory.createForager();
        ResourceCollectComponent resourceCollectComponent = forager1.getComponent(ResourceCollectComponent.class);
        resourceCollectComponent.create();
        Entity tree = new Entity();
        ResourceStatsComponent treeStats = new ResourceStatsComponent(4, 0, 0);
        tree.addComponent(treeStats);
        WorkerInventoryComponent inventory = forager1.getComponent(WorkerInventoryComponent.class);

        assertEquals(0, inventory.getWood());
        resourceCollectComponent.collectWood(treeStats);
        assertEquals(2, inventory.getWood());
        resourceCollectComponent.collectWood(treeStats);
        assertEquals(4, inventory.getWood());

        Entity base = new Entity();
        base.addComponent(new ResourceStatsComponent(0, 0, 0));
        resourceCollectComponent.loadToBase(base.getComponent(ResourceStatsComponent.class));
        assertEquals(0, inventory.getWood());
    }
    
}
