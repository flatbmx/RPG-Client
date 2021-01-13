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
	
	public boolean isGroup() {
		return TileSendType.GROUP.equals(sendType);
	}
	
	public boolean isSingle() {
		return TileSendType.SINGLE.equals(sendType);
	}
	
	public Location getLocation() {
		return getTile().getLocation();
	}
	
	public Tile[][] getTiles() {
		return tiles;
	}
	
	public boolean isCreate() {
		return TileUpdateType.CREATE.equals(updateType);
	}
	
	public boolean isDestroy() {
		return TileUpdateType.DESTROY.equals(updateType);
	}
	
	public static final TilePacket constructCreate(Tile tile) {
		return new TilePacket(tile, TileUpdateType.CREATE);
	}
	
	public static final TilePacket constructCreate(Tile[][] tiles) {
		return new TilePacket(tiles, TileUpdateType.CREATE);
	}
	
	public static final TilePacket constructDestroy(Tile tile) {
		return new TilePacket(tile, TileUpdateType.DESTROY);
	}
	
	public static final TilePacket constructDestroy(Tile[][] tiles) {
		return new TilePacket(tiles, TileUpdateType.DESTROY);
	}
	
	TilePacket(Tile tile, TileUpdateType updateType) {
		sendType = TileSendType.SINGLE;
		this.updateType = updateType;
		this.tile = tile;
		tiles = null;
	}
	
	public TilePacket(Tile tile, Location location, TileUpdateType updateType) {
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
