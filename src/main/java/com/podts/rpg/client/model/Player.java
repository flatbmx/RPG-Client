package com.podts.rpg.client.model;

public class Player implements Locatable {
	
	public static Player me;
	
	private final int id;
	private Location location;
	
	public int getID() {
		return id;
	}
	
	@Override
	public Location getLocation() {
		return location;
	}
	
	public Player(int id) {
		this.id = id;
		location = new Location();
	}
	
}
