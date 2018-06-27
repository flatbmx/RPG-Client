package com.podts.rpg.client.chat;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

public class ChatManager {
	
	public static final int DEFAULT_MAX_SIZE = 1000;
	
	private final LinkedList<ChatMessage> chatMessages = new LinkedList<>();
	private final List<ChatMessage> safeChatMessages = Collections.unmodifiableList(chatMessages);
	
	private int maxSize;
	
	public Collection<ChatMessage> getMessages() {
		return safeChatMessages;
	}
	
	public Stream<ChatMessage> messages() {
		return chatMessages.stream();
	}
	
	public ChatManager addMessage(ChatMessage message) {
		if(chatMessages.size() == maxSize) {
			chatMessages.removeLast();
		}
		chatMessages.addFirst(message);
		return this;
	}
	
	public ChatManager(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public ChatManager() {
		this(DEFAULT_MAX_SIZE);
	}
	
}
