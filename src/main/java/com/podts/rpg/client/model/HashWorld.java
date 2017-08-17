package com.podts.rpg.client.model;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class HashWorld extends World {
	
	private final Map<Location,Tile> tiles = new HashMap<>();
	private final Collection<Tile> safeTiles = Collections.unmodifiableCollection(tiles.values());
	
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
	
}
