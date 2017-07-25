package com.podts.rpg.client.model;

public abstract class World {
	
	private String worldname;
	
	private final String getName() { 
		
		return worldname;
	}
	
	private void setName(String newname) {
		
		worldname = newname;
		
	}
	
	protected World(String name) {
		
		this.worldname = name;
		
	}
	
}
