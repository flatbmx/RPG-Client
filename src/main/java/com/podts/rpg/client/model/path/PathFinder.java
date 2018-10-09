package com.podts.rpg.client.model.path;

import java.util.Optional;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Locatable;
import com.podts.rpg.client.model.Tile;

public abstract class PathFinder {
	
	public Optional<ListPath> findPath(Locatable start, Locatable finish) {
		if(start == null || finish == null)
			return Optional.empty();
		Tile startTile = Client.get().getWorld().getTile(start);
		Tile finishTile = Client.get().getWorld().getTile(finish);
		if(start.isAt(finish)) {
			return Optional.of(new ListPath(startTile, finishTile));
		}
		return findPath(startTile, finishTile);
	}
	
	protected Optional<ListPath> findPath(Tile start, Tile finish) {
		if(start == null || finish == null)
			return Optional.empty();
		if(!start.isTraversable() || !finish.isTraversable())
			return Optional.empty();
		return doFindPath(start, finish);
	}
	
	protected abstract Optional<ListPath> doFindPath(Tile start, Tile finish);
	
}
