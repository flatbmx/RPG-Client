package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UIWindow extends UIObject {
	
	private String title;
	private boolean showTitle;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean showsTitle() {
		return showTitle;
	}

	public void setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
	}
	
	public UIWindow(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public UIWindow(String title, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.title = title;
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		
		g.setColor(getBackgroundColor());
		g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		
		if(getBorderColor() != null) {
			g.setColor(getBorderColor());
			g.drawRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		}
	}
	
}
