package com.deco2800.game.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.GridPoint2;
import com.badlogic.gdx.math.Vector3;
import com.deco2800.game.components.CameraComponent;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.deco2800.game.services.GameTime;

import static com.deco2800.game.services.GameTime.ifPaused;

public class CameraInputComponent extends InputComponent {
    /**
     * Not used (set to 0) currently, designed to stop scrolling in resizable mode
     */
    private final float buffer2 = 0;
    /**
     * Distance in pixels between the edge of the screen and a camera movement trigger
     */
    private final float buffer = 80;
    /**
     * Default scrolling speed of camera
     */
    private final float defaultSpeed = 0.3f;
    /**
     * Scrolling speed of camera diagonally
     */
    private final float fastSpeed = 0.4f;

    /**
     * Current speed being scrolled at
     */
    private float currentSpeed = defaultSpeed;
    /**
     * Direction to scroll in horizontally (-1, 0, 1)
     */
    private float horizontalChange = 0;
    /**
     * Direction to scroll in vertically (-1, 0, 1)
     */
    private float verticalChange = 0;

    /**
     * Current tileSize of game
     */
    private float tileSize = -1;

    /**
     * Width of game map
     */
    private int mapWidth = 0;

    /**
     * Height of game map
     */
    private int mapHeight = 0;

    /**
     * Degree of how far the game is currently zoomed out - 0 is default
     */
    private float zoom = 0;

    /**
     * The maximum distance the player may zoom out of the game
     */
    private final float maxZoom = 10;

    public boolean ifPaused() {
        return GameTime.ifPaused();
    }

    public CameraInputComponent() {
        super(5);
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        //Mouse moved, check if it is at a screen extremity
        updateDirection();
        return false;
    }

    /**
     * Moves the camera relative to the change and current speed
     */
    @Override
    public void update() {
        float cameraX = super.entity.getPosition().x;
        float cameraY = super.entity.getPosition().y;

        //If game is paused, disable this function
        if (ifPaused()){
            return;
        }

        //If tileSize has been set, check to see where the camera is relative to the tiled map
        if (!(tileSize == -1)) {
            GridPoint2 tileCoords = worldToTile(cameraX, cameraY);
            //If camera is being moved off map and it is already too far in that direction, cancel movement
            if ((tileCoords.x >= mapWidth && horizontalChange == 1) || (tileCoords.x <= 0 && horizontalChange == -1)) {
                horizontalChange = 0;
            }
            if ((tileCoords.y >= mapHeight && verticalChange == 1) || (tileCoords.y <= 0 && verticalChange == -1)) {
                verticalChange = 0;
            }
        }

        super.entity.setPosition(cameraX + horizontalChange * currentSpeed,
                cameraY + verticalChange * currentSpeed);


    }

    //test function
    public boolean keyTyped(char character) {
        //testing
        int currentX = Gdx.input.getX();
        int currentY = Gdx.input.getY();
        OrthographicCamera camera = (OrthographicCamera) super.entity.getComponent(CameraComponent.class).getCamera();
        Vector3 worldCoords = camera.unproject(new Vector3(currentX, currentY,0));
        GridPoint2 tileCoords = worldToTile(worldCoords.x, worldCoords.y);
        System.out.println("TileX: " + tileCoords.x + " | TileY: " + tileCoords.y);
        return true;
    }

    /**
     * Checks to see if the cursor is hovered to a screen edge, and if so updates the direction it
     * must move in
     */
    public void updateDirection() {
        int currentX = Gdx.input.getX();
        int currentY = Gdx.input.getY();

        int screenWidth = Gdx.graphics.getWidth();
        int screenHeight = Gdx.graphics.getHeight();

        //If the mouse is at either of the horizontal extremities
        if (currentX >= screenWidth - buffer && !(currentX > screenWidth - buffer2)) {
            horizontalChange = 1;
        } else if (currentX <= buffer && !(currentX < buffer2)) {
            horizontalChange = -1;
        } else {
            horizontalChange = 0;
        }

        //If the mouse is at either of the vertical extremities
        if (currentY >= screenHeight - buffer && !(currentY > screenHeight - buffer2)) {
            verticalChange = -1;
        } else if (currentY <= buffer && !(currentY < buffer2)) {
            verticalChange = 1;
        } else {
            verticalChange = 0;
        }

        //If the camera is to move diagonally, increase the speed
        if (verticalChange != 0 && horizontalChange != 0) {
            currentSpeed = fastSpeed + (zoom * 0.1f);
        } else {
            currentSpeed = defaultSpeed + (zoom * 0.1f);
        }
    }
    
    /**
     * Adjusts the camera zoom amount according to scroll speed.
     */
    @Override
    public boolean scrolled(float amountX, float amountY) {
      OrthographicCamera camera = (OrthographicCamera) super.entity.getComponent(CameraComponent.class).getCamera();
      zoom = camera.zoom + amountY;
      if (zoom > 0 && !(zoom > maxZoom)) {
    	  camera.zoom = zoom;
    	  camera.update();
      }
      return true;
    }

    /**
     * Sets the tile size from a terrainComponent
     * @param tileSize tileSize in current game
     */
    public void setMapDetails(float tileSize, int mapWidth, int mapHeight) {
        this.tileSize = tileSize;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    /**
     * Converts a world point to a position on the tile map
     * @param worldX world y position of an entity
     * @param worldY world y position of an entity
     * @return position of the world point on the tile map
     */
    public GridPoint2 worldToTile(float worldX, float worldY) {
        //Define constants to make expression simpler
        float b = tileSize / 2;
        float a = tileSize / 3.724f;

        int tileX = (int) ((worldX / b) - (worldY / a)) / 2;
        int tileY = (int) (worldY / a) + tileX;

        return new GridPoint2(tileX, tileY);
    }

    /**
     * Converts a world point to a position on the tile map
     * @param tileSize tileSize returned by TerrainComponent of map
     * @param worldX world y position of an entity
     * @param worldY world y position of an entity
     * @return position of the world point on the tile map
     */
    public static GridPoint2 worldToTile(float tileSize, float worldX, float worldY) {
        //Define constants to make expression simpler
        float b = tileSize / 2;
        float a = tileSize / 3.724f;

        int tileX = (int) ((worldX / b) - (worldY / a)) / 2;
        int tileY = (int) (worldY / a) + tileX;

        return new GridPoint2(tileX, tileY);
    }

}
