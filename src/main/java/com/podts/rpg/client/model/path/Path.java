package com.podts.rpg.client.model.path;

import java.util.Collection;
import java.util.Optional;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Tile;

public abstract class Path {
	
	public abstract int getLength();
	public abstract int getTurns();
	
	public abstract Tile getStart();
	public abstract Tile getFinish();
	
	public abstract boolean contains(Tile tile);
	
	public abstract Optional<Direction> getLastDirection();
	
	public abstract Path extend(Tile next);
	
	public Path extend(Direction dir) {
		return extend(Client.get().getWorld().getTile(getFinish().getLocation().shift(dir)));
	}
	
	public abstract void insert(Collection<Tile> coll);
	
	public abstract ListPath finalizePath();
	
}
