package com.podts.rpg.client.model.path;

import java.util.Collection;
import java.util.Optional;

import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Tile;

public class ReferencePath extends Path {
	
	private final Path reference;
	private final int size, turns;
	private final Tile last;
	
	private final boolean hasReference() {
		return reference != null;
	}
	
	private final Path getReference() {
		return reference;
	}
	
	@Override
	public int getLength() {
		return size;
	}
	
	@Override
	public int getTurns() {
		return turns;
	}
	
	@Override
	public Tile getStart() {
		if(hasReference())
			return getReference().getStart();
		return getFinish();
	}

	@Override
	public Tile getFinish() {
		return last;
	}
	
	@Override
	public boolean contains(Tile tile) {
		if(getFinish().equals(tile))
			return true;
		if(hasReference())
			return getReference().contains(tile);
		return false;
	}
	
	@Override
	public ReferencePath extend(Tile next) {
		return new ReferencePath(this, next);
	}
	
	@Override
	public void insert(Collection<Tile> coll) {
		if(hasReference())
			getReference().insert(coll);
		coll.add(getFinish());
	}
	
	@Override
	public ListPath finalizePath() {
		ListPath result = new ListPath();
		insert(result.getList());
		return result;
	}
	
	@Override
	public Optional<Direction> getLastDirection() {
		if(getLength() < 2)
			return Optional.empty();
		return Optional.of(Direction.get(getReference().getFinish(), getFinish()).get());
	}
	
	ReferencePath(Path reference, Tile last) {
		this.reference = reference;
		this.last = last;
		int newTurns = 0;
		if(hasReference()) {
			newTurns += getReference().getTurns();
			Optional<Direction> lastDir = getReference().getLastDirection();
			if(lastDir.isPresent()) {
				if(!lastDir.get().equals(Direction.get(getReference().getFinish(),getFinish()))) {
					++newTurns;
				}
			}
		}
		turns = newTurns;
		size = reference.getLength() + 1;
	}
	
	ReferencePath(Tile start) {
		reference = null;
		last = start;
		size = 1;
		turns = 0;
	}
	
}
