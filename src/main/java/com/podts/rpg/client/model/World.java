package com.podts.rpg.client.model;

import java.util.Collection;

public abstract class World {
	
	private String worldname;
	
	/**
	 * Gets the name of the World.
	 * @return worldname - The name of the world.
	 */
	
	@SuppressWarnings("unused")
	private final String getName() { 
		
		return worldname;
	}
	
	/**
	 * Sets the name of the World.
	 * @param newname - The new name of the World.
	 */
	
	@SuppressWarnings("unused")
	private void setName(String newname) {
		
		worldname = newname;
		
	}
	
	public abstract Collection<Tile> getTiles();
	
	public abstract Tile getTile(Location loc);
	public abstract void addTile(Tile newTile);
	public abstract void removeTile(Tile tile);
	
	/**
	 * Constructs a World object.
	 * @param name - The name of the world.
	 */
	
	protected World(String name) {
		
		this.worldname = name;
		
	}
	
}
