package com.deco2800.game.areas.island;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Just a dummy tile class to replace later.
 * 
 * @author s4642637
 *
 */
public class DummyTile implements TiledMapTile {
	public DummyTile() {}
	
	// implement all methods in TiledMapTile interface here
	// atm i have just copied and pasted from TerrainTile
	
  	private int id;
    private BlendMode blendMode = BlendMode.ALPHA;
    private TextureRegion textureRegion;
	private float offsetX;
	private float offsetY;
	
    public DummyTile(TextureRegion textureRegion) {
	    this.textureRegion = textureRegion;
    }
	
    @Override
    public int getId() {
    	return id;
	}
	
    @Override
    public void setId(int id) {
	    this.id = id;
	}
	
    @Override
    public BlendMode getBlendMode() {
	    return blendMode;
    }
	
	  @Override
	  public void setBlendMode(BlendMode blendMode) {
	    this.blendMode = blendMode;
	  }
	
	  @Override
	  public TextureRegion getTextureRegion() {
	    return textureRegion;
	  }
	
	  @Override
	  public void setTextureRegion(TextureRegion textureRegion) {
	    this.textureRegion = textureRegion;
	  }
	
	  @Override
	  public float getOffsetX() {
	    return offsetX;
	  }
	
	  @Override
	  public void setOffsetX(float offsetX) {
	    this.offsetX = offsetX;
	  }
	
	  @Override
	  public float getOffsetY() {
	    return offsetY;
	  }
	
	  @Override
	  public void setOffsetY(float offsetY) {
	    this.offsetY = offsetY;
	  }
	
	  /**
	   * Not required for game, unimplemented
	   * @return null
	   */
	  @Override
	  public MapProperties getProperties() {
	    return null;
	  }
	
	  /**
	   * Not required for game, unimplemented
	   * @return null
	   */
	  @Override
	  public MapObjects getObjects() {
	    return null;
	  }
}