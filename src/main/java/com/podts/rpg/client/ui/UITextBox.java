package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UITextBox extends UIObject {
	
	public static final Color DEFAULT_BACKGROUND_COLOR = Color.orange;
	public static final String DEFAULT_FOCUS_CHARACTER = "|";
	
	private Color textColor = UIManager.DEFAULT_FONT_COLOR;
	private String focusCharacter;
	private String text = "";
	
	public UITextBox() {
		setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
	}
	
	public UITextBox(int x, int y, int width, int height) {
		super(x, y, width, height);
		setBackgroundColor(DEFAULT_BACKGROUND_COLOR);
		focusCharacter = DEFAULT_FOCUS_CHARACTER;
	}
	
	public UITextBox(int width, int height) {
		this(0, 0, width, height);
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		UILocation topLeft = getCorner(Corner.TOP_LEFT);
		g.setColor(getBackgroundColor());
		g.fillRect(topLeft.getX(), topLeft.getY(), getWidth(), getHeight());
		g.setColor(getTextColor());
		g.drawString(getDisplayText() + UIManager.get().getFocusCharacter(this), topLeft.getX(), topLeft.getY());
	}
	
	@Override
	public boolean handleMouseClick(MouseClickType clickType, int x, int y) {
		UIManager.get().setFocus(this);
		return true;
	}
	
	protected void handleTextInput(String character) {
		text += character;
	}
	
	protected UITextBox handleBackSpace() {
		if(text.length() > 0)
			text = text.substring(0, text.length()-1);
		return this;
	}
	
	public String getFocusCharacter() {
		return focusCharacter;
	}
	
	public Color getTextColor() {
		return textColor;
	}
	
	public String getDisplayText() {
		return getText();
	}
	
	public final String getText() {
		return text;
	}

	public UITextBox setText(String text) {
		this.text = text;
		return this;
	}
	
	public UITextBox clear() {
		setText("");
		return this;
	}
	
}
