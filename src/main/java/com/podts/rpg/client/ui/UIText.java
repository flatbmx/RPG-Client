package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

/**
 * Text that will be displayed somewhere. This does not handle input at all, this simply is meant to display text.
 *
 */
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

	public UIText(String string) {
		
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		g.setColor(UIManager.DEFAULT_FONT_COLOR);
		g.setFont(UIManager.DEFAULT_FONT);
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		g.drawString(getText(), topLeft.getX(), topLeft.getY());
	}

}
