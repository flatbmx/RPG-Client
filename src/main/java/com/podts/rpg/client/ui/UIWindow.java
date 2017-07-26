package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UIWindow extends SimpleUIParent {
	
	
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
		setBackgroundColor(UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR);
		setBorderColor(UIManager.DEFAULT_WINDOW_BORDER_COLOR);
	}
	
	public UIWindow(String title, int x, int y, int width, int height) {
		super(x, y, width, height);
		this.title = title;
		setBackgroundColor(UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR);
		setBorderColor(UIManager.DEFAULT_WINDOW_BORDER_COLOR);
	}

	public UIWindow(int width, int height) {
		super(width, height);
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		
		int lineWidth = 10;
		
		//Draw the whole background.
		g.setClip(topLeft.getX(), topLeft.getY(), getWidth()+1, getHeight()+1);
		g.setColor(UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR);
		g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		
		//Draw the border
		g.setLineWidth(lineWidth);
		g.setColor(UIManager.DEFAULT_WINDOW_BORDER_COLOR);
		g.drawRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		
		g.setClip(topLeft.getX() + lineWidth/2, topLeft.getY() + lineWidth/2,
				getWidth() - lineWidth/2, getHeight() - lineWidth/2);
		
		for(UIObject child : getChildren()) {
			child.render(gc, g);
		}
	}
	
}
