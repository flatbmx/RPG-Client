package com.podts.rpg.client.model.path;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

import com.podts.rpg.client.model.Tile;

public class ListPath extends Path {
	
	private final LinkedList<Tile> list;
	
	final LinkedList<Tile> getList() {
		return list;
	}
	
	@Override
	public int getLength() {
		return getList().size();
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
