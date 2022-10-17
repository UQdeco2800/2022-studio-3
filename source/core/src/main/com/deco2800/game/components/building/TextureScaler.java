package com.deco2800.game.components.building;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Null;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.components.TextureImageComponent;
import com.deco2800.game.rendering.AnimationRenderComponent;
import com.deco2800.game.rendering.TextureRenderComponent;

import java.util.ArrayList;
import java.util.List;

public class TextureScaler extends Component{
    private Vector2 leftPoint;
    private Vector2 maxX;
    private Vector2 maxY;
    private float initialScale = 0;
    private Texture baseTexture = null;

    //Variables which should be accessed after scaling
    private int tileWidth = -1;
    private int tileHeight = -1;
    //Variables which should be accessed after placing
    private GridPoint2 spawn = null;

    //Debug variables - accessed in function in TextureRenderComponent
    public List<Vector2> drawPoints = new ArrayList<>();
    public List<Vector2> linePoints = new ArrayList<>();

    public TextureScaler(Vector2 leftPoint, Vector2 maxX, Vector2 maxY) {
        this.leftPoint = leftPoint;
        this.maxX = maxX;
        this.maxY = maxY;
    }

    /**
     * Used to construct a TextureScaler with an existing texture - used for entities
     * without a TextureRenderComponent. For example, an Entity with an AnimationRenderComponent
     * @param leftPoint bottom left point of entity
     * @param maxX bottom right point of entity (max x value)
     * @param maxY max y point of entity
     * @param baseTexture default texture of entity
     */
    public TextureScaler(Vector2 leftPoint, Vector2 maxX, Vector2 maxY, Texture baseTexture) {
        this.leftPoint = leftPoint;
        this.maxX = maxX;
        this.maxY = maxY;
        this.baseTexture = baseTexture;
    }

    /**
     * Scales an entity with a TextureRenderComponent such that it occupies the desired number of tiles
     * @param desiredScale desired number of tiles to occupy (potentially float, but preferably integer input)
     * @param scaleWidth scale the Entity's width if true, else scale Entitie's height
     * @return true if the entity was scaled, else false
     */
    public boolean setPreciseScale(float desiredScale, boolean scaleWidth) {
        if (entity == null ||
                (entity.getComponent(TextureRenderComponent.class) == null && baseTexture == null)) {
            return false;
        }

        initialScale = desiredScale;
        Vector2 baseScale = new Vector2(1,1);

        float[] points = new float[] {
                leftPoint.x, leftPoint.y, maxX.x, maxX.y, maxY.x, maxY.y
        };

        //Set building Texture to baseTexture if it has been set, else use TRC
        Texture buildingTexture = baseTexture == null
                ? entity.getComponent(TextureRenderComponent.class).getTextureOG() : baseTexture;
        PolygonRegion region = new PolygonRegion(new TextureRegion(buildingTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        //Determine the positions of both points of the image in world coordinates
        Vector2 leftPointWorldPosition = new Vector2(translatedCoords[0], translatedCoords[1])
                .scl(baseScale);
        Vector2 maxXWorldPosition = new Vector2(translatedCoords[2], translatedCoords[3])
                .scl(baseScale);
        Vector2 maxYWorldPosition = new Vector2(translatedCoords[4], translatedCoords[5])
                .scl(baseScale);

        float xDistanceWidth =  maxXWorldPosition.x - leftPointWorldPosition.x;
        float yDistanceWidth = maxXWorldPosition.y - leftPointWorldPosition.y;

        float xDistanceHeight = maxYWorldPosition.x - leftPointWorldPosition.x;
        float yDistanceHeight = maxYWorldPosition.y - leftPointWorldPosition.y;
        //Distance between the two points of the image in world coordinates
        float heightDistance = (float) Math.sqrt(Math.pow(xDistanceHeight,2) + Math.pow(yDistanceHeight, 2));
        float widthDistance = (float) Math.sqrt(Math.pow(xDistanceWidth,2) + Math.pow(yDistanceWidth, 2));

        //Calculate desired distance between points in world coordinates (0.25 increase in world y per tile, 0.5 increase in world x per tile)
        float desiredDistance = (float) Math.sqrt(Math.pow(0.25f * desiredScale, 2) + Math.pow(0.5f * desiredScale,2));

        //Calculate scale required to yield this desired distance
        float scale;

        //Used to determine a number of tiles required for a distance
        float tileConversionConstant = (float) Math.sqrt(Math.pow(0.5, 2) + Math.pow(0.25, 2));

        //Set the tile width, tile height and entity scale
        if (scaleWidth) {
            scale = desiredDistance / widthDistance;
            this.tileWidth = (int) Math.ceil(desiredScale);
            this.tileHeight = (int) Math.ceil((heightDistance * scale) / tileConversionConstant);
        } else {
            scale = desiredDistance / heightDistance;
            this.tileHeight = (int) Math.ceil(desiredScale);
            this.tileWidth = (int) Math.ceil((widthDistance * scale) / tileConversionConstant);
        }

        //Scale the entity
        entity.scaleWidth(scale);

        return true;
    }

    /**
     * Returns the width of this entity in tiles
     * @return tile width
     */
    public int getTileWidth() {
        return this.tileWidth;
    }

    /**
     * Returns the height of this entity in tiles
     * @return tile height
     */
    public int getTileHeight() {
        return this.tileHeight;
    }

    /**
     * Returns the Gridpoint2 (x,y) position of this entity tile wise
     * @return tile position of Entity
     */
    public GridPoint2 getPosition() {
        return this.spawn;
    }

    public void setSpawnPoint(GridPoint2 tilePoint, TerrainComponent terrain) {
        //Set spawn point
        this.spawn = tilePoint;
        //Get world position of tile to spawn this building at
        Vector2 worldPos = terrain.tileToWorldPosition(tilePoint);
        Vector2 centrePosition = worldPos.cpy();

        //Debug code to track the world position of the desired tile
        drawPoints.add(worldPos.cpy());

        float tileSize = terrain.getTileSize();
        //Adjust for centring building
        worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;


        //TEST: draw box around initial position, red
        drawPoints.add(new Vector2(worldPos));


        float[] points = new float[] {
                leftPoint.x, leftPoint.y
        };

        //Find world position of the edge of the building relative to its spawn point
        Texture buildingTexture = baseTexture == null ?
                entity.getComponent(TextureRenderComponent.class).getTextureOG() : baseTexture;
        PolygonRegion region = new PolygonRegion(new TextureRegion(buildingTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        Vector2 offsetPointPosition = new Vector2(translatedCoords[0], translatedCoords[1])
                                    .scl(entity.getScale())
                                    .add(worldPos.x, worldPos.y);

        //Test draw box around offset position, blue
        drawPoints.add(offsetPointPosition);
        //Test - draw box starting from the Tile's bottom left point
        drawPoints.add(centrePosition);

        //Determine the distance between the offset point and the centre point
        Vector2 offsetDistance = new Vector2(centrePosition.x - offsetPointPosition.x, centrePosition.y - offsetPointPosition.y);

        //Manual fixed offset added to make alignment look more precise
        float constantOffset = (tileSize / 4);

        //Translate the position of the entity by the distance between those points
        worldPos.x += offsetDistance.x;
        worldPos.y += offsetDistance.y + constantOffset;

        //Debug code - shows how the object was scaled - draws a line of initialScale tiles long on x and y axis
        linePoints.add(centrePosition.cpy().add(0, constantOffset));
        Vector2 lineShift = terrain.tileToWorldPosition(new GridPoint2(tilePoint.x + tileWidth, tilePoint.y))
                .add(0, constantOffset);
        linePoints.add(lineShift);

        linePoints.add(centrePosition.cpy().add(0, constantOffset));
        linePoints.add(terrain.tileToWorldPosition(new GridPoint2(tilePoint.x, tilePoint.y + (int) tileHeight))
                .add(0,  constantOffset));

        entity.setPosition(worldPos);
    }

    public void setSpawnPoint(GridPoint2 tilePoint, TerrainComponent terrain, Vector2 offset) {
        //Set spawn point
        this.spawn = tilePoint;
        //Get world position of tile to spawn this building at
        Vector2 worldPos = terrain.tileToWorldPosition(tilePoint);
        Vector2 centrePosition = worldPos.cpy();

        //Debug code to track the world position of the desired tile
        drawPoints.add(worldPos.cpy());

        float tileSize = terrain.getTileSize();
        //Adjust for centring building
        worldPos.x += (tileSize / 2) - entity.getCenterPosition().x;
        worldPos.y += (tileSize / 2) - entity.getCenterPosition().y;


        //TEST: draw box around initial position, red
        drawPoints.add(new Vector2(worldPos));


        float[] points = new float[] {
                leftPoint.x, leftPoint.y
        };

        //Find world position of the edge of the building relative to its spawn point
        Texture buildingTexture = baseTexture == null ?
                entity.getComponent(TextureRenderComponent.class).getTextureOG() : baseTexture;
        PolygonRegion region = new PolygonRegion(new TextureRegion(buildingTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        Vector2 offsetPointPosition = new Vector2(translatedCoords[0], translatedCoords[1])
                .scl(entity.getScale())
                .add(worldPos.x, worldPos.y);

        //Test draw box around offset position, blue
        drawPoints.add(offsetPointPosition);
        //Test - draw box starting from the Tile's bottom left point
        drawPoints.add(centrePosition);

        //Determine the distance between the offset point and the centre point
        Vector2 offsetDistance = new Vector2(centrePosition.x - offsetPointPosition.x, centrePosition.y - offsetPointPosition.y);

        //Manual fixed offset added to make alignment look more precise
        float constantOffset = (tileSize / 4);

        //Translate the position of the entity by the distance between those points
        worldPos.x += offsetDistance.x + offset.x;
        worldPos.y += offsetDistance.y + constantOffset + offset.y;

        //Debug code - shows how the object was scaled - draws a line of initialScale tiles long on x and y axis
        linePoints.add(centrePosition.cpy().add(offset.x, constantOffset + offset.y));
        Vector2 lineShift = terrain.tileToWorldPosition(new GridPoint2(tilePoint.x + tileWidth, tilePoint.y))
                .add(offset.x, offset.y + constantOffset);
        linePoints.add(lineShift);

        linePoints.add(centrePosition.cpy().add(offset.x, offset.y + constantOffset));
        linePoints.add(terrain.tileToWorldPosition(new GridPoint2(tilePoint.x, tilePoint.y + (int) tileHeight))
                .add(offset.x, offset.y + constantOffset));

        entity.setPosition(worldPos);
    }
}
