package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.podts.rpg.client.ui.UIObject.MouseClickType;

public abstract class UIButton extends UIObject {
	
	public abstract void handleMouseClick(MouseClickType clickType);
	
	public final boolean handleMouseClick(MouseClickType clickType, int x, int y) {
		handleMouseClick(clickType);
		return true;
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		g.setColor(Color.pink);
		g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());	
	}
	
	public UIButton(int width, int height) {
		super(width, height);
	}

	public UIButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

}
