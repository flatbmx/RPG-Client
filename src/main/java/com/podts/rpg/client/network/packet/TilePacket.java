package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.network.Packet;

public final class TilePacket extends Packet {
	
	public enum TileSendType {
		GROUP(),
		SINGLE();
	}
	
	private final TileSendType type;
	private final Tile tile;
	private final Tile[][] tiles;
	
	public TileSendType getType() {
		return type;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public TilePacket(Tile tile) {
		type = TileSendType.SINGLE;
		this.tile = tile;
		tiles = null;
	}
	
	public TilePacket(Tile[][] tiles) {
		type = TileSendType.GROUP;
		this.tiles = tiles;
		tile = null;
	}
	
}
