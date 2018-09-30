package com.podts.rpg.client;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;

public class Resources {
	
	private final Map<String,Image> images = new HashMap<>();
	
	public final Image getImage(String name) {
		return images.get(name);
	}
	
	private final void loadImage(String name, Image image) {
		images.put(name, image);
	}
	
	private final void loadImage(String name, String path) throws SlickException {
		loadImage(name, new Image(path));
	}
	
	void init() throws SlickException {
		
		loadImage("Grass", "res/Grass.png");
		loadImage("Sand", "res/Sand.png");
		loadImage("Water", "res/Water.png");
		
	}
	
	Resources() {
		
	}
	
}
