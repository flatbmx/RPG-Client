package com.podts.rpg.client.model.path;

import java.util.Optional;

import com.podts.rpg.client.model.Tile;

/**
 * Attempts to find a path with up to one turn first.
 * If no such path exists, returns a path using breath first search.
 * @author David
 *
 */
public class CompundPathFinder extends PathFinder {
	
	private final PathFinder straight = new StraightPathFinder();
	private final PathFinder star = new StarPathFinder();
	
	@Override
	protected Optional<ListPath> doFindPath(Tile start, Tile finish) {
		Optional<ListPath> result = straight.doFindPath(start, finish);
		if(result.isPresent())
			return result;
		return star.doFindPath(start, finish);
	}

}
