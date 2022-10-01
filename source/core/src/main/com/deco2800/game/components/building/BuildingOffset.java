package com.deco2800.game.components.building;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.areas.terrain.TerrainComponent;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.rendering.TextureRenderComponent;
import com.deco2800.game.services.ServiceLocator;

import java.util.ArrayList;
import java.util.List;

public class BuildingOffset extends Component{
    private float xOffset;
    private float yOffset;

    //testing
    public List<Vector2> drawPoints = new ArrayList<>();

    public BuildingOffset(float xOffset, float yOffset) {
        this.xOffset = xOffset;
        this.yOffset = yOffset;
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
                xOffset, yOffset
        };

        //Find world position of the edge of the building relative to its spawn point
        Texture buildingTexture = entity.getComponent(TextureRenderComponent.class).getTextureOG();
        PolygonRegion region = new PolygonRegion(new TextureRegion(buildingTexture), points, null);
        float[] translatedCoords = region.getTextureCoords();
        Vector2 offsetPointPosition = new Vector2(translatedCoords[0], translatedCoords[1]).scl(entity.getScale());
        offsetPointPosition.x += worldPos.x;
        offsetPointPosition.y += worldPos.y;

        //Determine the centre position of the entity -> this is the start of the tile to place the texture
        //Vector2 centrePosition = worldPos.cpy().mulAdd(entity.getScale(), 0.5f);

        //Test draw box around offset position, blue
        drawPoints.add(offsetPointPosition);

        drawPoints.add(centrePosition);

        //Determine the distance between the offset point and the centre point
        Vector2 offsetDistance = new Vector2(centrePosition.x - offsetPointPosition.x, centrePosition.y - offsetPointPosition.y);

        //Translate the position of the entity by the distance between those points
        worldPos.x += offsetDistance.x;
        worldPos.y += offsetDistance.y + (tileSize / 2) - 0.15f;

        entity.setPosition(worldPos);
    }


}
