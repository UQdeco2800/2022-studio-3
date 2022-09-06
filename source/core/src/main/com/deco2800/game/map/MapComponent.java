package com.deco2800.game.map;

import com.deco2800.game.components.Component;
import com.deco2800.game.services.ServiceLocator;

/**
 * If entity has a MapComponent they are added to the map.
 * 
 */
public class MapComponent extends Component {
	
	@Override
	public void create() {
		ServiceLocator.getMapService().register(this);
	}
	
	@Override
    public void dispose() {
		super.dispose();
		ServiceLocator.getMapService().unregister(this);
    }

	@Override
	public void update() {
		ServiceLocator.getMapService().update(this);
	}
}