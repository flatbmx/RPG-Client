package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UITextbox extends UIObject{
	
	private String text;
	
	public UITextbox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g, int x, int y) {
	
		
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
