package com.deco2800.game.components.player;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Array;
import com.deco2800.game.areas.ForestGameArea;
import com.deco2800.game.components.Component;
import com.deco2800.game.entities.Entity;
import com.deco2800.game.physics.components.PhysicsComponent;
import com.deco2800.game.services.ServiceLocator;

/**
 * Action component for interacting with the player. Player events should be initialised in create()
 * and when triggered should call methods within this class.
 */
public class PlayerActions extends Component {

  private ForestGameArea forestGameArea;

  private Array<Entity> friendlyUnits;

  private Array<Entity> selectedUnits;
  private Entity selectedUnit;

  public PlayerActions(ForestGameArea forestGameArea) {
    this.forestGameArea = forestGameArea;
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
    System.out.println(startX);
    System.out.println(startY);
    System.out.println(endX);
    System.out.println(endY);
//    for (Entity entity: friendlyUnits) {
//      entity.getPosition()
//    }
  }

  public void selectUnit(int xCoordinate, int yCoordinate) {
    System.out.println(xCoordinate);
    System.out.println(yCoordinate);
  }
//    for (Entity entity: friendlyUnits) {
//      if (entity.getPosition().epsilonEquals()) {
//        this.selectedUnit
//      }
//    }
//  }

}
