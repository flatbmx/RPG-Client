package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class UIObject {
	
	public enum MouseClickType {
		LEFT_CLICK(),
		RIGHT_CLICK(),
		MIDDLE_CLICK();
		
		public static MouseClickType getFromID(int id) {
			return vals[id];
		}
		
		private MouseClickType() {
		}
		
		private static final MouseClickType[] vals = MouseClickType.values();
		
	}
	
	public enum Corner {
		TOP_LEFT(-1,-1),
		TOP_RIGHT(1,-1),
		BOTTOM_LEFT(-1,1),
		BOTTOM_RIGHT(1,1);
		
		private int x, y;
		
		private Corner(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private int x, y;
	private int width,height;
	private boolean centerX, centerY;
	
	private Color BorderColor;
	private Color backgroundColor;
	
	public final UILocation getTopLeft() {
		return getCorner(Corner.TOP_LEFT);
	}
	
	public boolean isCenterX() {
		return centerX;
	}

	public void setCenterX(boolean centerX) {
		this.centerX = centerX;
	}

	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public final boolean isIn(int mx, int my) {
		if(mx < getCorner(Corner.TOP_LEFT).getX()) return false;
		if(mx > getCorner(Corner.TOP_RIGHT).getX()) return false;
		if(my < getCorner(Corner.TOP_LEFT).getY()) return false;
		if(my > getCorner(Corner.BOTTOM_LEFT).getY()) return false;
		return true;
	}
	
	protected UILocation getCorner(Corner c) {
		int lx = 0;
		int ly = 0;
		
		if(isCenterX()) {
			lx = width/2;
		}
		
		if(isCenterY()) {
			ly = height/2;
		}
		
		return new UILocation(x + c.x*lx,y + c.y*ly);
	}
	
	public void handleMouseClick(MouseClickType clickType) {
		
	}
	
	public void onHover() {
		
	}
	
	public void onHoverLeave() {
		
	}
	
	public abstract void render(GameContainer gc, Graphics g);
	
	public UIObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public boolean isCenterY() {
		return centerY;
	}

	public void setCenterY(boolean centerY) {
		this.centerY = centerY;
	}

	public Color getBorderColor() {
		return BorderColor;
	}

	public void setBorderColor(Color borderColor) {
		BorderColor = borderColor;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
}
