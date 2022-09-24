package com.deco2800.game.areas.MapGenerator;

import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.map.MapService;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.utils.random.Timer;

import java.util.Map;

public class FloodingGenerator {
    private final AtlantisTerrainFactory atlantisTerrainFactory;
    private MapService mapService;
    private MapGenerator mapGenerator;
    private Timer timer;

    public FloodingGenerator(AtlantisTerrainFactory atlantisTerrainFactory) {
        this.atlantisTerrainFactory = atlantisTerrainFactory;
        this.timer = new Timer(10000, 10000);
        this.timer.start();
        while (true) {
            if (this.timer.isTimerExpired()) {
                triggerFloodEvent();
                this.timer = new Timer(10000, 10000);
                this.timer.start();
            }
        }
        //TODO - How do we pause the timer when the game is paused?

        //TODO - IDEAS: Flash tile that is picked to be flooded next.
        //TODO - Visual Timer on the screen.
    }



    /**
     *
     */
    public void triggerFloodEvent() {
        this.floodTile();
    }

    /**
     * Algorithm that determines which tiles to be flooded on the next flooding event.
     */
    public void floodTile() {
        //TODO - Pick tile/tiles to be flooded
        int[] coords = pickTileToFlood();
        int x = coords[0];
        int y = coords[1];

        //The call to atlantisTerrainFactory updates the structure of mapGenerator
        this.mapGenerator = this.atlantisTerrainFactory.floodTiles(x, y);
    }

    public int[] pickTileToFlood() {
        //TODO - Pick Tiles to be flooded (Dito/Jordan)

        //Dummy code for now
        int[] coords = new int[2];
        coords[0] = 20;
        coords[1] = 20;
        return coords;
    }
}

//    private static void floodingEdges(TiledMapTileLayer layer) {
//        ResourceService resourceService = ServiceLocator.getResourceService();
//        TextureRegion isoOcean =
//                new TextureRegion(resourceService.getAsset("images/Ocean.png", Texture.class));
//        TerrainTile oceanTile = new TerrainTile(isoOcean);
//        char[][] map = mapGenerator.getMap();
//        char[][] mapOutline = new char[mapWidth][mapHeight];
//        for (int x = 1; x < mapWidth-1; x++) {
//            for (int y = 1; y < mapHeight-1; y++) {
//                Cell cell = new Cell();
//                if (map[y][x] == mapGenerator.getIslandChar()) {
//                    if (map[y-1][x] == mapGenerator.getOceanChar() ||
//                            map[y+1][x] == mapGenerator.getOceanChar() ||
//                            map[y][x-1] == mapGenerator.getOceanChar() ||
//                            map[y][x+1] == mapGenerator.getOceanChar())
//                        cell.setTile(oceanTile);
//                }
//                layer.setCell(x, mapHeight - y, cell);
//            }
//        }
//    }