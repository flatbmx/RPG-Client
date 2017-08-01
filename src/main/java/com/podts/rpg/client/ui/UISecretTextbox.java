package com.podts.rpg.client.ui;

public class UISecretTextbox extends UITextBox {
	
	private String displayText = "";
	
	@Override
	protected void handleTextInput(String character) {
		super.handleTextInput(character);
		displayText += "*";
	}
	
	@Override
	protected void handleBackSpace() {
		if(displayText.length() == 0) return;
		super.handleBackSpace();
		displayText = displayText.substring(0, displayText.length()-1);
	}
	
	@Override
	public String getDisplayText() {
		return displayText;
	}
	
	@Override
	public void setText(String text) {
		super.setText(text);
		String newDisplay = "";
		for(int i=0; i<text.length(); ++i)
			newDisplay += "*";
		displayText = newDisplay;
	}
	
	public UISecretTextbox(int x, int y, int width, int height) {
		super(x, y, width, height);
	}
	
	public UISecretTextbox(int width, int height) {
		super(width, height);
	}

	public UISecretTextbox() {
		
	}

}
