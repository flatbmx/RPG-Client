package com.podts.rpg.client.model;

public abstract class TileSelection implements Iterable<Tile> {
	
	public enum SelectionType {
		SQUARE(),
		SET();
	}
	
	public abstract SelectionType getSelectionType();
	public abstract int size();
	
	protected TileSelection() {
		
	}
	
}
