package com.deco2800.game.components.player;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.areas.GameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;
import com.deco2800.game.utils.math.RandomUtils;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {

  private GameArea gameArea;

  private Array<Entity> friendlyUnits = new Array<>();

  private Array<Entity> selectedUnits = new Array<>();
  private Entity selectedUnit = null;

  public PlayerActions(GameArea gameArea) {
    this.gameArea = gameArea;
  }

  @Override
  public void create() {
    entity.getEvents().addListener("selectUnit", this::selectUnit);
    entity.getEvents().addListener("selectUnits", this::selectUnits);

//    entity.getEvents().addListener("walk", this::);
//    entity.getEvents().addListener("walkStop", this::);
  }

  @Override
  public void update() {
  }

  public void selectUnits(int startX, int startY, int endX, int endY) {
    selectedUnits = new Array<>();
    selectedUnit = null;
    for (Entity entity: friendlyUnits) {
      Vector2 centrePosition = entity.getCenterPosition();
      if (RandomUtils.contains(centrePosition.x, startX, endX)
              && RandomUtils.contains(centrePosition.y, startY, endY)) {
        selectedUnits.add(entity);
      }
    }
  }

  public void selectUnit(int xCoordinate, int yCoordinate) {
    selectedUnits = new Array<>();
    selectedUnit = null;
    System.out.println(xCoordinate + ":" + yCoordinate);
    for (Entity entity: friendlyUnits) {
      Vector2 startPosition = entity.getPosition();
      Vector2 endPosition = entity.getPosition().mulAdd(entity.getScale(), 1f);
      if (RandomUtils.contains(xCoordinate, startPosition.x, endPosition.x)
          && RandomUtils.contains(yCoordinate, startPosition.y, endPosition.y)) {
        selectedUnit = entity;
      }
    }
  }

  public void addFriendly(Entity entity) {
    friendlyUnits.add(entity);
    System.out.println(entity.getCenterPosition());
  }

}
