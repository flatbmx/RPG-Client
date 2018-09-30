package com.podts.rpg.client.network.packet;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.network.Packet;

public class TileSelectionPacket extends Packet implements Iterable<Tile> {
	
	private final Collection<Tile> tiles;
	
	public Collection<Tile> getTiles() {
		return tiles;
	}
	
	public Iterator<Tile> iterator() {
		return getTiles().iterator();
	}
	
	public TileSelectionPacket(Collection<? extends Tile> tiles) {
		this.tiles = Collections.unmodifiableCollection(new HashSet<>(tiles));
	}
	
}
