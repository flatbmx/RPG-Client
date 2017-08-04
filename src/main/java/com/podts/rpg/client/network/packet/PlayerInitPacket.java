package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.network.Packet;

public final class PlayerInitPacket extends Packet {
	
	private final int id;
	private final Location location;
	
	public int getId() {
		return id;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public PlayerInitPacket(int id, Location location) {
		this.id = id;
		this.location = location;
	}
	
}
