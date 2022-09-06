package com.deco2800.game.components;

import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.entities.Entity;

import java.util.HashMap;

public class ObstacleMapComponent extends Component {
  private final HashMap<Vector2, Entity> obstacleMap;

  public ObstacleMapComponent(HashMap<Vector2, Entity> obstacleMap) {
    this.obstacleMap = obstacleMap;
  }

  public Entity getEntityAtPoint(Vector2 location) {
    return this.obstacleMap.get(location);
  }

  public void addObstacle(Vector2 location, Entity entity) {
    this.obstacleMap.put(location, entity);
  }
}
