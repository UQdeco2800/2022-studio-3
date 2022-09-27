// package com.deco2800.game.map;

// import static org.junit.jupiter.api.Assertions.*;

// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.BeforeAll;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.extension.ExtendWith;

// import com.deco2800.game.entities.Entity;
// import com.deco2800.game.entities.EntityService;
// import com.deco2800.game.extensions.GameExtension;
// import com.deco2800.game.services.ServiceLocator;

// @ExtendWith(GameExtension.class)
// public class MapServiceTest {
//     // @BeforeEach
//     // void setup() {
//     //     ServiceLocator.registerMapService(new MapService());
//     //     ServiceLocator.registerEntityService(new EntityService());
//     // }
    
//     @Test
//     void checkComponentRegisters() {
//         // Entity et = new Entity().addComponent(new MapComponent());
//         ServiceLocator.registerMapService(new MapService());
//         ServiceLocator.getMapService().register(new MapComponent());
//         assertFalse(ServiceLocator.getMapService().getMapComponents().size() > 0);
//     }

//     // @Test
//     // void checkIslandTileRegisters() {
//     //     ServiceLocator.getMapService().addIslandTile(0, 0);
//     //     assertTrue(ServiceLocator.getMapService().getIslandTiles().size() > 0);
//     // }
// }
