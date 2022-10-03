package com.deco2800.game.areas.MapGenerator;

import com.deco2800.game.areas.AtlantisGameArea;
import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;
import com.deco2800.game.map.MapService;
import com.deco2800.game.areas.MapGenerator.MapGenerator;
import com.deco2800.game.utils.random.Timer;
import com.deco2800.game.components.Component;
import com.deco2800.game.utils.random.PseudoRandom;

import java.util.Map;
import java.util.Random;

public class FloodingGenerator extends Component {
    private final AtlantisTerrainFactory atlantisTerrainFactory;
    private MapService mapService;
    private MapGenerator mapGenerator;
    private AtlantisGameArea atlantisGameArea;
    private Timer timer;

    public FloodingGenerator(AtlantisTerrainFactory atlantisTerrainFactory, AtlantisGameArea atlantisGameArea) {
        this.atlantisTerrainFactory = atlantisTerrainFactory;
        this.atlantisGameArea = atlantisGameArea;
        this.timer = new Timer(1000, 1001);
        this.timer.start();
        //TODO - How do we pause the timer when the game is paused?
        //TODO - IDEAS: Flash tile that is picked to be flooded next.
        //TODO - Visual Timer on the screen.
    }

    @Override
    public void create() {
        super.create();
    }

    @Override
    public void update() {
        if (this.timer.isTimerExpired()) {
            triggerFloodEvent();
            this.atlantisGameArea.flood();
            this.timer = new Timer(100, 101);
            this.timer.start();
        }
    }

    @Override
    public void dispose() {
        super.dispose();
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
        coords[0] = PseudoRandom.seedRandomInt(1,89);
        coords[1] = PseudoRandom.seedRandomInt(1,199);
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