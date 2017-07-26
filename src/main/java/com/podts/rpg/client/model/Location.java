package com.podts.rpg.client.model;

public class Location {
	
	/**
	 * Declares two double variables(x and y).
	 */
	
	private double x, y;
	
	/**
	 * Gets a locations X variable.
	 * @return x
	 */
	
	public double getX() {
		return x;
	}
	
	/**
	 * Gets a locations Y variable.
	 * @return y
	 */
	
	public double getY() {
		return y;
	}
	
	public Location() {
		
	}
	/**
	 * Constructs a Location object.
	 * @param x the x coordinate.
	 * @param y the y coordinate.
	 */
	
	public Location(double x, double y) {
		this.x = x;
		this.y = y;
	}
	
}
