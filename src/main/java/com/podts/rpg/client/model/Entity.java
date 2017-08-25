package com.podts.rpg.client.model;

public class Entity implements Locatable {
	
	private final int id;
	private Location location;
	
	public final int getID() {
		return id;
	}
	
	@Override
	public final Location getLocation() {
		return location;
	}
	
	public Entity(int id, Location location) {
		this.id = id;
		this.location = location;
	}

	public void setLocation(Location newLocation) {
		location = newLocation;
	}
	
}
