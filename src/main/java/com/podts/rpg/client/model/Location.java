package com.podts.rpg.client.model;

import java.util.Objects;

public class Location implements Locatable {
	
	public enum Direction {
		UP(0,-1),
		DOWN(0,1),
		LEFT(-1,0),
		RIGHT(1,0);
		
		private final int dx, dy;
		
		public Location MoveFromLocation(Location origin) {
			return origin.move(dx, dy, 0);
		}
		
		private Direction(int dx, int dy) {
			this.dx = dx;
			this.dy = dy;
		}
		
	}
	
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
	
	public int getZ() {
		return z;
	}
	
	@Override
	public String toString() {
		return "[" + x + ", " + y + ", " + z + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY(), getZ());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(this == o) return true;
		if(o instanceof Location) {
			Location oL = (Location) o;
			return x == oL.x
					&& y == oL.y
					&& z == oL.z;
		}
		return false;
	}
	
	public Location move(int dx, int dy, int dz) {
		return new Location(x + dx, y + dy, z + dz);
	}
	
	public final Location getLocation() {
		return this;
	}
	
	public Location shift(Direction dir) {
		return new Location(x + dir.dx, y + dir.dy, getZ());
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
