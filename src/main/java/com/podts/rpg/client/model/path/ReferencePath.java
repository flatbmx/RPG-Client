package com.podts.rpg.client.model.path;

import java.util.Collection;

import com.podts.rpg.client.model.Tile;

public class ReferencePath extends Path {
	
	private final Path reference;
	private final int size;
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
	public Tile getStart() {
		return getReference().getStart();
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
	
	ReferencePath(Path reference, Tile last) {
		this.reference = reference;
		this.last = last;
		size = reference.getLength() + 1;
	}
	
	ReferencePath(Tile start) {
		reference = null;
		last = start;
		size = 1;
	}
	
}
