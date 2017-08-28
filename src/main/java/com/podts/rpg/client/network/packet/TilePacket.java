package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.network.Packet;

public final class TilePacket extends Packet {
	
	public enum TileUpdateType {
		CREATE(),
		DESTROY();
	}
	
	public enum TileSendType {
		GROUP(),
		SINGLE();
	}
	
	private final TileUpdateType updateType;
	private final TileSendType sendType;
	private final Tile tile;
	private final Tile[][] tiles;
	
	public TileUpdateType getUpdateType() {
		return updateType;
	}
	
	public TileSendType getType() {
		return sendType;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public TilePacket(Tile tile, TileUpdateType updateType) {
		sendType = TileSendType.SINGLE;
		this.updateType = updateType;
		this.tile = tile;
		tiles = null;
	}
	
	public TilePacket(Tile[][] tiles, TileUpdateType updateType) {
		sendType = TileSendType.GROUP;
		this.updateType = updateType;
		this.tiles = tiles;
		tile = null;
	}
	
}
