package com.podts.rpg.client.model;

import java.util.HashMap;
import java.util.Map;

public class HashWorld extends World {
	
	private final Map<Location,Tile> tiles = new HashMap<>();
	
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
