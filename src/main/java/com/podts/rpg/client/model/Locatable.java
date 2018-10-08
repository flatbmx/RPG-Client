package com.podts.rpg.client.model;

/**
 * Objects that implement the interface Locatable must have a method included in the class named getLocation().
 * This method will get the location of the object in which the Locatable interface was implemented.
 * @author Will
 */

public interface Locatable {
	
	public static int getWalkingDistance(Locatable a, Locatable b) {
		return getWalkingDistance(a.getLocation(), b.getLocation());
	}
	
	public static int getWalkingDistance(Location a, Location b) {
		return a.getX() - b.getX() + a.getY() - b.getY();
	}
	
	public Location getLocation();
	
	public default boolean isAt(Locatable loc) {
		return getLocation().equals(loc.getLocation());
	}
	
	public default int getWalkingDistance(Locatable other) {
		return getWalkingDistance(this, other);
	}
	
}
