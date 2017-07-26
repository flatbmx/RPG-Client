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
	
	protected UIParent parent;
	
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
	
	public UIParent getParent() {
		return parent;
	}
	
	public final boolean isIn(int mx, int my) {
		if(mx < getCorner(Corner.TOP_LEFT).getX()) return false;
		if(mx > getCorner(Corner.TOP_RIGHT).getX()) return false;
		if(my < getCorner(Corner.TOP_LEFT).getY()) return false;
		if(my > getCorner(Corner.BOTTOM_LEFT).getY()) return false;
		return true;
	}
	
	/**
	 * Returns the absolute UILocation of the game screen for this object.
	 * @param c - The chosen corner.
	 * @return The Corners location.
	 */
	public UILocation getCorner(Corner c) {
		int lx = 0;
		int ly = 0;
		
		int ox = 0;
		int oy = 0;
		
		UIParent prnt = getParent();
		if(prnt != null) {
			UILocation topLeftParent = prnt.getCorner(Corner.TOP_LEFT);
			ox = topLeftParent.getX();
			oy = topLeftParent.getY();
		}
		
		if(isCenterX()) {
			lx = width/2;
		}
		
		if(isCenterY()) {
			ly = height/2;
		}
		
		return new UILocation(x + c.x*lx + ox,y + c.y*ly + oy);
	}
	
	public void handleMouseClick(MouseClickType clickType) {
		
	}
	
	public void onHover() {
		
	}
	
	public void onHoverLeave() {
		
	}
	
	/**
	 * Renders this UIObject with the given GameContainer and Graphics with a given x and y coordinates
	 * as the initial origin for where to draw.
	 * @param gc - The GameContainer
	 * @param g - The Graphics
	 * @param x - Origin X
	 * @param y
	 */
	public abstract void render(GameContainer gc, Graphics g, int x, int y);

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
	
	public UIObject(int width, int height) {
		x = 0;
		y = 0;
		this.width = width;
		this.height = height;
	}
	
	public UIObject(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
}
