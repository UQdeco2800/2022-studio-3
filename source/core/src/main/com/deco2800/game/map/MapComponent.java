package com.deco2800.game.map;

import com.badlogic.gdx.maps.Map;
import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;
import com.badlogic.gdx.graphics.Color;

/**
 * If entity has a MapComponent they are added to the map.
 * 
 */
public class MapComponent extends Component {
	private boolean display;
	private Color colour;

	public MapComponent() {
		this.display = false;
		this.colour = Color.RED;
	}
	
	@Override
	public void create() {
		ServiceLocator.getMapService().register(this);
	}
	
	@Override
    public void dispose() {
		ServiceLocator.getMapService().unregister(this);
    }

	@Override
	public void update() {
		ServiceLocator.getMapService().update(this);
	}

	public void display() {
		this.display = true;
	}

	public void noDisplay() {
		this.display = false;
	}

	public boolean isDisplay() {
		return this.display;
	}

	public void setDisplayColour(Color colour) {
		this.colour = colour;
	}

	public Color getColour() {
		return this.colour;
	} 
}