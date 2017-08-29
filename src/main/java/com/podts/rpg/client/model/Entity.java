package com.podts.rpg.client.model;

public abstract class Entity implements Locatable {
	
	private String name;
	private final int id;
	private Location location;
	
	public final int getID() {
		return id;
	}
	
	public final String getName() {
		return name;
	}
	
	public abstract EntityType getType();
	
	@Override
	public final Location getLocation() {
		return location;
	}
	
	public void setLocation(Location newLocation) {
		location = newLocation;
	}
	
	protected Entity(int id, String name, Location location) {
		this.id = id;
		this.name = name;
		this.location = location;
	}
	
}
