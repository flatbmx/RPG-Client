package com.podts.rpg.client.model;

public class Player implements Locatable {
	
	public static Player me;
	
	private final int id;
	private Location location;
	
	/**
	 * Gets the Players Id.
	 * @return The id of the Player.
	 */
	
	public int getID() {
		return id;
	}
	
	/**
	 * Gets the location of the Player.
	 */
	
	@Override
	public Location getLocation() {
		return location;
	}
	
	/**
	 * Constructs a Player Object , sets the id of the Player, and gives the Player a new Location.
	 * @param id the id of the player.
	 */
	
	public Player(int id) {
		this.id = id;
		location = new Location();
	}
	
}
