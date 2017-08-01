package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UITextBox extends UIObject{
	
	private String text = "";
	
	public UITextBox() {
		
	}
	
	public UITextBox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public UITextBox(int width, int height) {
		super(width, height);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		g.setColor(Color.yellow);
		g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		g.setColor(UIManager.DEFAULT_FONT_COLOR);
		g.setFont(UIManager.DEFAULT_FONT);
		g.drawString(getText(), topLeft.getX(), topLeft.getY());
	}
	
	protected void handleTextInput(String character) {
		text += character;
	}
	
	protected void handleBackSpace() {
		if(text.length() == 0) return;
		text = text.substring(0, text.length()-1);
	}
	
	public String getDisplayText() {
		return getText();
	}
	
	public final String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
