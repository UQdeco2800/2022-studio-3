package com.deco2800.game.map;

import 

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
}