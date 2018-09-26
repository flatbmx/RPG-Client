package com.podts.rpg.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.podts.rpg.client.Client;

public final class HashWorld extends World {
	
	private final Map<Location,Tile> tiles = new HashMap<>();
	private final Collection<Tile> safeTiles = Collections.unmodifiableCollection(tiles.values());
	
	private final Map<Integer,Entity> entities = new HashMap<>();
	private final Collection<Entity> safeEntities = Collections.unmodifiableCollection(entities.values());
	
	@Override
	public Collection<Tile> getTiles() {
		return safeTiles;
	}
	
	@Override
	public Tile getTile(Location loc) {
		return tiles.get(loc);
	}

	@Override
	public void addTile(Tile newTile) {
		int oldSize = tiles.size();
		tiles.put(newTile.getLocation(), newTile);
		Client.getLogger().info("Added " + newTile + " from size " + oldSize + " to " + tiles.size());
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
		Client.getLogger().info("Clearing world");
		tiles.clear();
		entities.clear();
	}
	
	public HashWorld(String name) {
		super(name);
	}
	
}
