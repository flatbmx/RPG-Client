package com.podts.rpg.client.chat;

import org.newdawn.slick.Color;

public class ChatMessage {
	
	private final String text;
	
	public final String getText() {
		return text;
	}
	
	public Color getChatColor() {
		return Color.black;
	}
	
	public ChatMessage(String message) {
		this.text = message;
	}
	
}
