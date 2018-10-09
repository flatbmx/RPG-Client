package com.podts.rpg.client.model.path;

import java.util.Optional;

import com.podts.rpg.client.model.Tile;

/**
 * Attempts to find a path with up to one turn first.
 * If no such path exists, returns a path using breath first search.
 * @author David
 *
 */
public class CompoundPathFinder extends PathFinder {
	
	public static final int DEFAULT_MAX_LENGTH = 50;
	
	private final PathFinder straight = new StraightPathFinder();
	private final PathFinder star;
	
	@Override
	protected Optional<ListPath> doFindPath(Tile start, Tile finish) {
		Optional<ListPath> result = straight.doFindPath(start, finish);
		if(result.isPresent())
			return result;
		return star.doFindPath(start, finish);
	}
	
	public CompoundPathFinder(int maxLength) {
		star = new StarPathFinder(maxLength);
	}
	
	public CompoundPathFinder() {
		this(DEFAULT_MAX_LENGTH);
	}
	
}
