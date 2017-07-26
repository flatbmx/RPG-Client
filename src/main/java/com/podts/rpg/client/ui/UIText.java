package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UIText extends UIObject {
	
	private String text;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public UIText(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		// TODO Auto-generated method stub

	}

}
