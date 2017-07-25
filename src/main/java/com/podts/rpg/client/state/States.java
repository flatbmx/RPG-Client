package com.podts.rpg.client.state;

public enum States {
	
	LOGIN(0),
	PLAYING(1);
	
	private final int id;
	
	public final int getID() {
		return id;
	}
	
	private States(int id) {
		this.id = id;
	}
	
}
