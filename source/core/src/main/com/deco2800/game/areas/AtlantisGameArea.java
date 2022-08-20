package com.deco2800.game.areas;

import com.badlogic.gdx.audio.Music;

/**
 * Creates the environment of the game, including generating all entities and the terrain.
 * 
 * @author s4642637
 *
 */
public class AtlantisGameArea extends GameArea {
	private IslandFactory islandFactory;
	
	// also need to store info here about the game assets
	
	public AtlantisGameArea(IslandFactory islandFactory) {
		super();
		this.islandFactory = islandFactory;
	}
	
	/*
	 * Create the atlantis game area.
	 */
	@Override
	public void create() {
		/*
		 * Should be the only public method.
		 * 
		 * Here we need to spawn the entities, play any music, and load all assets (music, sounds, 
		 * atlases, etc.) with services
		 */
	}
	
	// to register the entities with the game engine, must call GameArea -> spawnEntity() or spawnEntityAt()
	
	private void spawnIsland() {}
	
	private void spawnNPCs() {}
	
	private void spawnBuildings() {}
	
	private void spawnTrees() {}
	
	// add more spawn methods as we like to add entities to the environment, and call them in create()
	
}