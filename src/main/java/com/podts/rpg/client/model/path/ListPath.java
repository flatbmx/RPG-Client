package com.podts.rpg.client.model.path;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Optional;

import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Tile;

public class ListPath extends Path {
	
	private final LinkedList<Tile> list;
	private int turns = 0;
	
	final LinkedList<Tile> getList() {
		return list;
	}
	
	@Override
	public int getLength() {
		return getList().size();
	}
	
	@Override
	public int getTurns() {
		return turns;
	}
	
	@Override
	public Optional<Direction> getLastDirection() {
		if(getLength() < 2)
			return Optional.empty();
		Iterator<Tile> revIt = getList().descendingIterator();
		Tile last = revIt.next();
		Tile secondLast = revIt.next();
		return Optional.of(Direction.get(secondLast, last).get());
	}
	
	@Override
	public Tile getStart() {
		return getList().getFirst();
	}

	@Override
	public Tile getFinish() {
		return list.getLast();
	}
	
	@Override
	public boolean contains(Tile tile) {
		return getList().contains(tile);
	}
	
	@Override
	public Path extend(Tile next) {
		return new ReferencePath(this, next);
	}
	
	@Override
	public void insert(Collection<Tile> coll) {
		coll.addAll(getList());
	}
	
	@Override
	public ListPath finalizePath() {
		return this;
	}
	
	public Collection<Tile> getTiles() {
		return Collections.unmodifiableCollection(list);
	}
	
	ListPath(Collection<Tile> tiles) {
		this.list = new LinkedList<>(tiles);
	}
	
	ListPath(Tile... tiles) {
		list = new LinkedList<>();
		for(Tile t : tiles)
			getList().addLast(t);
	}
	
}
