package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.model.EntityType;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.network.Packet;

public final class EntityPacket extends Packet {
	
	public enum UpdateType {
		CREATE(),
		UPDATE(),
		DESTROY();
	}
	
	private final UpdateType type;
	private final int entityID;
	private final EntityType entityType;
	private final Location location;
	private final String name;
	
	public UpdateType getUpdateType() {
		return type;
	}
	
	public int getEntityID() {
		return entityID;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public EntityType getEntityType() {
		return entityType;
	}
	
	public String getName() {
		return name;
	}
	
	public EntityPacket(UpdateType type, int entityID) {
		this.type = type;
		this.entityID = entityID;
		location = null;
		entityType = null;
		name = null;
	}
	
	public EntityPacket(UpdateType type, int entityID, Location l) {
		this.type = type;
		this.entityID = entityID;
		location = l;
		entityType = null;
		name = null;
	}
	
	public EntityPacket(UpdateType type, int entityID, EntityType eType, Location l) {
		this.type = type;
		this.entityID = entityID;
		location = l;
		entityType = eType;
		name = null;
	}
	
}
