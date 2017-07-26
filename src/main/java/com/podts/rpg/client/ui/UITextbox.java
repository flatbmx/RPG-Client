package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UITextbox extends UIObject{
	
	private String text;
	
	public UITextbox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
	
		
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
