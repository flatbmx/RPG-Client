package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.model.EntityType;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.network.Packet;

public class EntityPacket extends Packet {
	
	public enum UpdateType {
		CREATE(),
		UPDATE(),
		DESTROY();
	}
	
	private final UpdateType type;
	private final int entityID;
	private final EntityType entityType;
	private final Location location;
	
	public final UpdateType getUpdateType() {
		return type;
	}
	
	public final int getEntityID() {
		return entityID;
	}
	
	public final Location getLocation() {
		return location;
	}
	
	public final EntityType getEntityType() {
		return entityType;
	}
	
	public EntityPacket(UpdateType type, int entityID) {
		this.type = type;
		this.entityID = entityID;
		location = null;
		entityType = null;
	}
	
	public EntityPacket(UpdateType type, int entityID, Location l) {
		this.type = type;
		this.entityID = entityID;
		location = l;
		entityType = null;
	}
	
	public EntityPacket(UpdateType type, int entityID, EntityType eType, Location l) {
		this.type = type;
		this.entityID = entityID;
		location = l;
		entityType = eType;
	}
	
}
