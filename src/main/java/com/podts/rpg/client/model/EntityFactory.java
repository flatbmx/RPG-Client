package com.podts.rpg.client.model;

public final class EntityFactory {
	
	public static final Entity createEntity(int id, String name, EntityType type, Location location) {
		return new TypedEntity(id, name, type, location);
	}
	
}
