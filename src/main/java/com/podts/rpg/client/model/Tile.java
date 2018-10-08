package com.podts.rpg.client.model;

import java.util.Objects;

import org.newdawn.slick.Color;

public final class Tile implements Locatable {
	
	public enum TileType {
		VOID(Color.black,false),
		GRASS(Color.green.darker()),
		DIRT(new Color(139,69,19)),
		SAND(Color.yellow),
		WATER(new Color(125,125,255), false);
		
		private final boolean traversable;
		private final Color color;
		
		public boolean isTraversable() {
			return traversable;
		}
		
		public Color getColor() {
			return color;
		}
		
		private TileType(Color color) {
			traversable = true;
			this.color = color;
		}
		
		private TileType(Color color, boolean travel) {
			traversable = travel;
			this.color = color;
		}
		
	}
	
	private final TileType type;
	private final Location location;
	
	public TileType getType() {
		return type;
	}
	
	public Location getLocation() {
		return location;
	}
	
	public final boolean isVoid() {
		return isType(TileType.VOID);
	}
	
	public final boolean isType(TileType type) {
		return Objects.equals(getType(), type);
	}
	
	public boolean isTraversable() {
		return type.isTraversable();
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getType(), getLocation());
	}
	
	@Override
	public boolean equals(Object o ) {
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof Tile) {
			Tile other = (Tile) o;
			return getType().equals(other.getType())
					&& getLocation().equals(other.getLocation());
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "[" + getType() + " " + getLocation() + "]";
	}
	
	public Tile(TileType type, Location location) {
		this.type = type;
		this.location = location;
	}
	
}
