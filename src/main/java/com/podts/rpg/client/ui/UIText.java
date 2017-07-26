package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
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
	
	public UIText(int width, int height) {
		super(0, 0, width, height);
	}
	
	public UIText(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public void render(GameContainer gc, Graphics g, int oX, int oY) {
		g.setColor(Color.white);
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		g.drawString(getText(), oX + getX(), oY + getY());
	}

}
