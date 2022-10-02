package com.deco2800.game.components.building;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.rendering.TextureRenderComponent;

import java.util.ArrayList;
import java.util.List;

public class TextureScaler extends Component{
    private Vector2 leftPoint;
    private Vector2 rightPoint;
    private float initialScale = 0;

    //testing
    public List<Vector2> drawPoints = new ArrayList<>();
    public List<Vector2> linePoints = new ArrayList<>();

    public TextureScaler(Vector2 leftPoint, Vector2 rightPoint) {
        this.leftPoint = leftPoint;
        this.rightPoint = rightPoint;
    }

    /**
     * Scales an entity with a TextureRenderComponent such that it occupies the desired number of tiles
     * @param desiredScale desired number of tiles to occupy (potentially float, but preferably integer input)
     * @return true if the entity was scaled, else false
     */
    public boolean setPreciseScale(float desiredScale) {
        if (entity == null) {
            return false;
        }
        initialScale = desiredScale;
        Vector2 baseScale = new Vector2(1,1);

        float[] points = new float[] {
                leftPoint.x, leftPoint.y, rightPoint.x, rightPoint.y
        };
        Texture buildingTexture = entity.getComponent(TextureRenderComponent.class).getTextureOG();
        PolygonRegion region = new PolygonRegion(new TextureRegion(buildingTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        //Determine the positions of both points of the image in world coordinates
        Vector2 leftPointWorldPosition = new Vector2(translatedCoords[0], translatedCoords[1])
                .scl(baseScale);
        Vector2 rightPointWorldPosition = new Vector2(translatedCoords[2], translatedCoords[3])
                .scl(baseScale);

        float xDistance =  rightPointWorldPosition.x - leftPointWorldPosition.x;
        float yDistance = rightPointWorldPosition.y - leftPointWorldPosition.y;
        //Distance between the two points of the image in world coordinates
        float distance = (float) Math.sqrt(Math.pow(xDistance,2) + Math.pow(yDistance , 2));

        //Calculate desired distance between points in world coordinates (0.25 increase in world y per tile, 0.5 increase in world x per tile)
        float desiredDistance = (float) Math.sqrt(Math.pow(0.25f * desiredScale, 2) + Math.pow(0.5f * desiredScale,2));

        //Calculate scale required to yield this desired distance
        float scale = desiredDistance / distance;

        //Scale the entity
        entity.scaleWidth(scale);

        return true;
    }

    public void setSpawnPoint(GridPoint2 tilePoint, TerrainComponent terrain) {
        //Get world position of tile to spawn this building at
        Vector2 worldPos = terrain.tileToWorldPosition(tilePoint);
        Vector2 centrePosition = worldPos.cpy();

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
        Texture buildingTexture = entity.getComponent(TextureRenderComponent.class).getTextureOG();
        PolygonRegion region = new PolygonRegion(new TextureRegion(buildingTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        Vector2 offsetPointPosition = new Vector2(translatedCoords[0], translatedCoords[1])
                                    .scl(entity.getScale())
                                    .add(worldPos.x, worldPos.y);

        //Test draw box around offset position, blue
        drawPoints.add(offsetPointPosition);

        drawPoints.add(centrePosition);

        //Determine the distance between the offset point and the centre point
        Vector2 offsetDistance = new Vector2(centrePosition.x - offsetPointPosition.x, centrePosition.y - offsetPointPosition.y);

        //Manual fixed offset added to make alignment look more precise
        float constantOffset = (tileSize / 4);

        //Translate the position of the entity by the distance between those points
        worldPos.x += offsetDistance.x;
        worldPos.y += offsetDistance.y + constantOffset;

        //Debug code - shows how the object was scaled
        linePoints.add(centrePosition.cpy().add(0, constantOffset));
        Vector2 lineShift = terrain.tileToWorldPosition(new GridPoint2(tilePoint.x + (int) initialScale, tilePoint.y))
                .add(0,  constantOffset);
        linePoints.add(lineShift);

        linePoints.add(centrePosition.cpy().add(0, constantOffset));
        linePoints.add(terrain.tileToWorldPosition(new GridPoint2(tilePoint.x, tilePoint.y + (int) initialScale))
                .add(0,  constantOffset));

        entity.setPosition(worldPos);
    }


}
