package com.podts.rpg.client.model;

public class TypedEntity extends Entity {
	
	private final EntityType type;

	@Override
	public EntityType getType() {
		return type;
	}
	
	protected TypedEntity(int id, String name, EntityType type, Location location) {
		super(id, name, location);
		this.type = type;
	}
	
}
