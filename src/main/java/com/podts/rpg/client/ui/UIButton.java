package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public abstract class UIButton extends UIObject {
	
	public abstract void handleMouseClick(MouseClickType clickType);
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		
	}
	
	public UIButton(int width, int height) {
		super(width, height);
	}

	public UIButton(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

}
