package com.podts.rpg.client.model.path;

import java.util.Collection;
import java.util.Iterator;
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
	
	@Override
	public String toString() {
		if(getLength() < 2)
			return "[Empty]";
		StringBuilder builder = new StringBuilder();
		builder.append("[");
		Iterator<Tile> it = finalizePath().getTiles().iterator();
		Tile oldTile = it.next();
		Tile newTile;
		while(it.hasNext()) {
			newTile = it.next();
			Direction dir = Direction.get(oldTile, newTile).get();
			builder.append(dir);
			oldTile = newTile;
			if(it.hasNext())
				builder.append(", ");
		}
		builder.append("]");
		return builder.toString();
	}
	
}
