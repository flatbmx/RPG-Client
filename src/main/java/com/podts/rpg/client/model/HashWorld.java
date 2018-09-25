package com.podts.rpg.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class HashWorld extends World {
	
	private final Map<Location,Tile> tiles = new ConcurrentHashMap<>();
	private final Collection<Tile> safeTiles = Collections.unmodifiableCollection(tiles.values());
	
	private final Map<Integer,Entity> entities = new ConcurrentHashMap<>();
	private final Collection<Entity> safeEntities = Collections.unmodifiableCollection(entities.values());
	
	public Collection<Tile> getTiles() {
		return safeTiles;
	}
	
	@Override
	public Tile getTile(Location loc) {
		return tiles.get(loc);
	}

	@Override
	public void addTile(Tile newTile) {
		tiles.put(newTile.getLocation(), newTile);
	}
	
	public HashWorld(String name) {
		super(name);
	}

	@Override
	public void removeTile(Tile tile) {
		if(tile != null)
			tiles.remove(tile.getLocation());
	}
	
	@Override
	public Collection<Entity> getEntities() {
		return safeEntities;
	}
	
	@Override
	public Entity getEntity(int id) {
		return entities.get(id);
	}
	
	@Override
	public void addEntity(Entity entity) {
		entities.put(entity.getID(), entity);
	}
	
	@Override
	public void removeEntity(Entity entity) {
		if(entity != null)
			entities.remove(entity.getID());
	}
	
	@Override
	public void clear() {
		tiles.clear();
		entities.clear();
	}
	
}
