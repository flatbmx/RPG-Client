package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.model.Location;
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
	private final Location location;
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
	
	public Location getLocation() {
		return location;
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public TilePacket(Tile tile, Location location, TileUpdateType updateType) {
		sendType = TileSendType.SINGLE;
		this.updateType = updateType;
		this.tile = tile;
		this.location = location;
		tiles = null;
	}
	
	public TilePacket(Tile[][] tiles, TileUpdateType updateType) {
		sendType = TileSendType.GROUP;
		this.updateType = updateType;
		this.tiles = tiles;
		tile = null;
		location = null;
	}
	
}
