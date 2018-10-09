package com.podts.rpg.client.model;

import java.util.Collection;
import java.util.Optional;

import com.podts.rpg.client.model.path.CompoundPathFinder;
import com.podts.rpg.client.model.path.ListPath;
import com.podts.rpg.client.model.path.PathFinder;

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
	
	public Tile getTile(Locatable loc) {
		return getTile(loc.getLocation());
	}
	
	public abstract Tile getTile(Location loc);
	public abstract void addTile(Tile newTile);
	public abstract void removeTile(Tile tile);
	
	public abstract Collection<Entity> getEntities();
	
	public abstract Entity getEntity(int id);
	public abstract void addEntity(Entity entity);
	public abstract void removeEntity(Entity entity);
	
	public abstract void clear();
	
	private final PathFinder pathFinder = new CompoundPathFinder(500);
	
	public synchronized Optional<ListPath> getPath(Locatable start, Locatable finish) {
		return pathFinder.findPath(start, finish);
	}
	
	/**
	 * Constructs a World object.
	 * @param name - The name of the world.
	 */
	protected World(String name) {
		this.worldname = name;
	}
	
}
