package com.podts.rpg.client.model;

import com.podts.rpg.client.network.packet.EntityPacket;

public class Player implements Locatable {
	
	public static Player me;
	
	public static final void handleFirstEntityPacket(EntityPacket p) {
		me.playerEntity = EntityFactory.createEntity(p.getEntityID(), "Me", EntityType.PLAYER, p.getLocation());
	}
	
	private final int id;
	private Entity playerEntity;
	
	/**
	 * Gets the Players Id.
	 * @return The id of the Player.
	 */
	public int getID() {
		return id;
	}
	
	public Entity getPlayerEntity() {
		return playerEntity;
	}
	
	/**
	 * Gets the location of the Player.
	 */
	@Override
	public Location getLocation() {
		return playerEntity.getLocation();
	}
	
	/**
	 * Constructs a Player Object , sets the id of the Player, and gives the Player a new Location.
	 * @param id the id of the player.
	 */
	public Player(int id) {
		this.id = id;
	}
	
	public Player(int id, Location loc) {
		this.id = id;
	}
	
}
