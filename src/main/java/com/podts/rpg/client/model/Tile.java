package com.podts.rpg.client.model;

import org.newdawn.slick.Color;

public final class Tile implements Locatable {
	
	public enum TileType {
		VOID(Color.black,false),
		GRASS(Color.green),
		DIRT(new Color(139,69,19)),
		WATER(new Color(125,125,255),false);
		
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
	
	public boolean isTraversable() {
		return type.isTraversable();
	}
	
	public Tile(TileType type, Location location) {
		this.type = type;
		this.location = location;
	}
	
}
