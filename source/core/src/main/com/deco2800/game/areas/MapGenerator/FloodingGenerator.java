package main.com.deco2800.game.areas.MapGenerator;

import com.deco2800.game.areas.terrain.AtlantisTerrainFactory;

import java.util.Map;

public class FloodingGenerator {
    private final AtlantisTerrainFactory atlantisTerrainFactory;

    public FloodingGenerator(AtlantisTerrainFactory atlantisTerrainFactory) {
        this.atlantisTerrainFactory = atlantisTerrainFactory;
    }

    /**
     * ??TODO
     */
    public void triggerFloodEvent() {
        Map<Integer, Integer> tilesToBeFlooded = pickTilesToFlood();
        drawMap(tilesToBeFlooded);
    }

    /**
     * Algorithm that determines which tiles to be flooded on the next flooding event.
     */
    public Map<Integer, Integer> pickTilesToFlood() {
        // PICK TILES TO FLOOD AND UPDATE MAP STRUCTURE
        return null;
    }

    /**
     * Draw the new terrain to the game screen.
     */
    public void drawMap(Map<Integer, Integer> tilesToBeFlooded) {
        atlantisTerrainFactory.floodTiles(tilesToBeFlooded);
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