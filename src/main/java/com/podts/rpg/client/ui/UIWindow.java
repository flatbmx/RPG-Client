package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UIWindow extends SimpleUIParent {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = new Color(0,0,175);
	public static final Color DEFAULT_BORDER_COLOR = new Color(100,100,100);
	
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
		setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
		setBorderColor(DEFAULT_BORDER_COLOR);
	}
	
	public UIWindow(String title, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.title = title;
		setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
		setBorderColor(DEFAULT_BORDER_COLOR);
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		
		g.setClip(topLeft.getX(), topLeft.getY(), getWidth()+1, getHeight()+1);
		g.setColor(getBackgroundColor());
		g.setLineWidth(10);
		g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		
		if(getBorderColor() != null) {
			g.setColor(getBorderColor());
			g.drawRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		}
	}
	
}
