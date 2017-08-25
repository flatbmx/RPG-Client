package com.podts.rpg.client.model;

public class Location implements Locatable {
	
	/**
	 * Declares two double variables(x and y).
	 */
	
	private int x, y, z;
	
	/**
	 * Gets a locations X variable.
	 * @return x
	 */
	
	public int getX() {
		return x;
	}
	
	/**
	 * Gets a locations Y variable.
	 * @return y
	 */
	
	public int getY() {
		return y;
	}
	
	public Location move(int dx, int dy, int dz) {
		return new Location(x + dx, y + dy, z + dz);
	}
	
	public final Location getLocation() {
		return this;
	}
	
	public Location() {
		
	}
	
	/**
	 * Constructs a Location object.
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 */
	public Location(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
}
