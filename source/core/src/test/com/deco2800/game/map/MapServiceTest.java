package com.deco2800.game.map;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;

import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.entities.EntityService;
import com.deco2800.game.extensions.GameExtension;
import com.deco2800.game.services.ServiceLocator;

@ExtendWith(GameExtension.class)
public class MapServiceTest {
    @BeforeEach
    void setup() {
        ServiceLocator.registerMapService(new MapService());
        ServiceLocator.registerEntityService(new EntityService());
    }
    
    @Test
    void checkComponentRegisters() {
        Entity et = (new Entity()).addComponent(new MapComponent());
        ServiceLocator.getEntityService().register(et);
        assertTrue(ServiceLocator.getMapService().getMapComponents().size() == 1);
    }

    @Test
    void checkIslandTileRegisters() {
        ServiceLocator.getMapService().addIslandTile(0, 0);
        assertTrue(ServiceLocator.getMapService().getIslandTiles().size() == 1);
    }

    // @Test
    // void testTileOccupation() {
    //     for (int x = 0; x < 10; x++) {
    //         for (int y = 0; y < 10; y++) {
    //             ServiceLocator.getMapService().addIslandTile(x, y);
    //         }
    //     }

    //     Entity et = (new Entity()).addComponent(new MapComponent());
    //     et.setPosition(MapService.tileToWorldPosition(new GridPoint2(4, 9)));
    //     et.setScale(MapService.tileToWorldPosition(new GridPoint2(-1, 1)));
    //     ServiceLocator.getEntityService().register(et);

    //     Entity et2 = (new Entity()).addComponent(new MapComponent());
    //     et2.setPosition(MapService.tileToWorldPosition(new GridPoint2(4, 0)));
    //     et2.setScale(MapService.tileToWorldPosition(new GridPoint2(-1, 1)));
    //     ServiceLocator.getEntityService().register(et2);

    //     assertTrue(ServiceLocator.getMapService().getMapComponents().size() == 2, "Components did not register");

    //     Map<MapComponent, List<GridPoint2>> etp = ServiceLocator.getMapService().getAllEntityPositions();
    //     assertTrue(etp.size() == 2, "Entities on map: " + etp.size());
    //     for (List<GridPoint2> l : etp.values()) {
    //         assertTrue(l.size() == 1, "Entity occupied positions: " + l.toString());
    //     }
        
    //     Map<GridPoint2, MapComponent> pte = ServiceLocator.getMapService().getEntityOccupiedPositions();
    //     assertTrue(pte.size() == 2, "Occupied map positions: " + pte.keySet().toString());

    //     for (int x = 0; x < 10; x++) {
    //         for (int y = 0; y < 10; y++) {
    //             if ((x == 4 && y ==9) || (x == 4 && y == 0)) {
    //                 assertTrue(ServiceLocator.getMapService().isOccupied(new GridPoint2(x,y)), "This tile should be occupied: " + x + "," + y);
    //             } else {
    //                 assertFalse(ServiceLocator.getMapService().isOccupied(new GridPoint2(x,y)), "This tile should not be occupied: " + x + "," + y);
    //             }
    //         }
    //     }
    // }

    @Test
    void testBFS() {
        for (int x = 0; x < 10; x++) {
            for (int y = 0; y < 10; y++) {
                ServiceLocator.getMapService().addIslandTile(x, y);
            }
        }

        Entity et = (new Entity()).addComponent(new MapComponent());
        et.setPosition(MapService.tileToWorldPosition(new GridPoint2(4, 9)));
        ServiceLocator.getEntityService().register(et);

        Entity et2 = (new Entity()).addComponent(new MapComponent());
        et2.setPosition(MapService.tileToWorldPosition(new GridPoint2(4, 0)));
        ServiceLocator.getEntityService().register(et2);

        List<GridPoint2> path = ServiceLocator.getMapService().getPath(new GridPoint2(0, 0), new GridPoint2(9, 9));
        assertTrue(path.size() == 9, "Path: " + path.toString());
    }
}
