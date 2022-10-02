package com.deco2800.game.rendering;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.deco2800.game.components.building.TextureScaler;
import com.deco2800.game.components.building.GateCollider;
import com.deco2800.game.services.ServiceLocator;

import java.util.List;

/** Render a static texture. */
public class TextureRenderComponent extends RenderComponent {
  private Texture texture;
  private Texture textureOG;
  public String texturePath;


  /**
   * @param texturePath Internal path of static texture to render.
   *                    Will be scaled to the entity's scale.
   */
  public TextureRenderComponent(String texturePath) {
    this(ServiceLocator.getResourceService().getAsset(texturePath, Texture.class));
    this.texturePath = texturePath;
  }

//...
  /** @param texture Static texture to render. Will be scaled to the entity's scale. */
  public TextureRenderComponent(Texture texture) {
    this.texture = texture;
    textureOG = texture;
  }

  /** Scale the entity to a width of 1 and a height matching the texture's ratio */
  public void scaleEntity() {
    entity.setScale(1f, (float) texture.getHeight() / texture.getWidth());
  }

  /**
   * Draws a square around the area enclosed by the texture - for debugging
   * @param batch SpriteBatch of game
   */
  public void drawTextureBox(SpriteBatch batch) {
    ShapeRenderer sr = new ShapeRenderer();
    sr.setProjectionMatrix(batch.getProjectionMatrix());
    batch.end();
    //Set hollow fill for black rectangle outline
    sr.begin(ShapeRenderer.ShapeType.Line);
    sr.setColor(Color.BLACK);
    //Get size of texture in world coordinates
    Vector2 entityScale = entity.getScale();
    sr.rect(entity.getPosition().x, entity.getPosition().y, entityScale.x, entityScale.y);
    sr.end();
    sr.dispose();
    batch.begin();
  }

  /**
   * Debug function which draws a box around an entity with a TextureRenderComponent
   * @param positions positions to draw a box around
   * @param linePos list of points to draw lines between
   * @param batch the SpriteBatch of the game
   */
  public void drawTextureBox (List<Vector2> positions, List<Vector2> linePos, SpriteBatch batch) {
    for (Vector2 position : positions) {
      ShapeRenderer sr = new ShapeRenderer();
      sr.setProjectionMatrix(batch.getProjectionMatrix());
      batch.end();
      //Set hollow fill for black rectangle outline
      sr.begin(ShapeRenderer.ShapeType.Line);
      if (positions.indexOf(position) % 4 == 1) {
        sr.setColor(Color.RED); //Colour of second index - initial position
      } else if (positions.indexOf(position) % 4 == 2) {
        sr.setColor(Color.YELLOW);  //Colour of third index - offset position
      } else if (positions.indexOf(position) % 4 == 3) {
        sr.setColor(Color.BLUE); //Colour of fourth index - centre position
      }
      else {
        sr.setColor(Color.GREEN); //Colour of first index - spawn tile point
      }
      //Get size of texture in world coordinates
      Vector2 entityScale = entity.getScale();
      sr.rect(position.x, position.y, entityScale.x, entityScale.y);

      //Draw lines
      sr.setColor(Color.VIOLET);
      for (int i = 0; i < linePos.size(); i+=2) {
        float x1 = linePos.get(i).x;
        float x2 = linePos.get(i+1).x;
        float y1 = linePos.get(i).y;
        float y2 = linePos.get(i+1).y;
        float a = x2-x1;
        float b = y2-y1;
        float c = (float) Math.sqrt(Math.pow(a,2) + Math.pow(b,2));
        float c2 = (float) Math.sqrt(Math.pow(0.26562f * 5,2) + Math.pow(0.49687f * 5f,2));
        //System.out.println("distance observed: " +c + " distance theoretical: " + c2);
        sr.line(linePos.get(i), linePos.get(i+1));
      }

      sr.end();
      sr.dispose();
      batch.begin();
    }

  }

  public void setTexture(Texture texture) {
    this.texture = texture;
  }
  public void setTextureOG() {
    this.textureOG = this.texture;
  }

  public Texture getTextureOG() {
    return this.textureOG;
  }

  @Override
  protected void draw(SpriteBatch batch) {
    Vector2 position = entity.getPosition();
    Vector2 scale = entity.getScale();
    batch.draw(texture, position.x, position.y, scale.x, scale.y);

    //debug
    if (entity.getComponent(GateCollider.class) !=  null) {
      drawTextureBox(batch);
      TextureScaler bo = entity.getComponent(TextureScaler.class);
      drawTextureBox(bo.drawPoints, bo.linePoints,  batch);
    }
  }
}
