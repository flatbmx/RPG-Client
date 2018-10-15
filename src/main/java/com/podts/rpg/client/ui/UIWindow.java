package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UIWindow extends SimpleUIParent {
	
	public UIWindow(String title, int x, int y, int width, int height, Color background, Color border) {
		super(x, y, width, height);
		this.title = title;
		setBackgroundColor(background);
		setBorderColor(border);
	}
	
	public UIWindow(String title, int x, int y, int width, int height) {
		this(title, x, y, width, height, UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR, UIManager.DEFAULT_WINDOW_BORDER_COLOR);
	}
	
	public UIWindow(int x, int y, int width, int height) {
		super(x, y, width, height);
		setBackgroundColor(UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR);
		setBorderColor(UIManager.DEFAULT_WINDOW_BORDER_COLOR);
	}

	public UIWindow(int width, int height) {
		super(width, height);
		setBackgroundColor(UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR);
		setBorderColor(UIManager.DEFAULT_WINDOW_BORDER_COLOR);
	}
	
	private String title;
	private boolean showTitle;
	
	public String getTitle() {
		return title;
	}

	public UIWindow setTitle(String title) {
		this.title = title;
		return this;
	}
	
	public boolean showsTitle() {
		return showTitle;
	}

	public UIWindow setShowTitle(boolean showTitle) {
		this.showTitle = showTitle;
		return this;
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		g.setClip(topLeft.getX(), topLeft.getY(), getWidth()+1, getHeight()+1);
		
		//Draw the whole background.
		if(hasBackgroundColor()) {
			g.setColor(getBackgroundColor());
			g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		}
		
		//Draw the border
		int lineWidth = 10;
		if(hasBorderColor()) {
			g.setLineWidth(lineWidth);
			g.setColor(getBorderColor());
			g.drawRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		}
		
		g.setClip(topLeft.getX() + lineWidth
				, topLeft.getY() + lineWidth
				, getWidth() - lineWidth
				, getHeight() - lineWidth);
		
		for(UIObject child : getChildren()) {
			child.render(gc, g);
		}
	}
	
}
