package com.podts.rpg.client.chat;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.stream.Stream;

public class ChatManager {
	
	public static final int DEFAULT_MAX_SIZE = 1000;
	
	private final LinkedList<ChatMessage> chatMessages = new LinkedList<>();
	private final List<ChatMessage> safeChatMessages = Collections.unmodifiableList(chatMessages);
	
	private final LinkedList<String> recentEntries = new LinkedList<>();
	private final List<String> safeRecentEntries = Collections.unmodifiableList(recentEntries);
	
	private int maxSize;
	
	public Collection<ChatMessage> getMessages() {
		return safeChatMessages;
	}
	
	public Stream<ChatMessage> messages() {
		return getMessages().stream();
	}
	
	public ChatManager addMessage(ChatMessage message) {
		if(chatMessages.size() == maxSize) {
			chatMessages.removeLast();
		}
		chatMessages.addFirst(message);
		return this;
	}
	
	public List<String> getRecentEntries() {
		return safeRecentEntries;
	}
	
	public ListIterator<String> getRecentEntriesIterator() {
		return safeRecentEntries.listIterator();
	}
	
	public ChatManager addEntry(String entry) {
		Iterator<String> it = recentEntries.iterator();
		while(it.hasNext()) {
			if(it.next().equals(entry)) {
				it.remove();
				break;
			}
		}
		recentEntries.addFirst(entry);
		return this;
	}
	
	public ChatManager clear() {
		chatMessages.clear();
		recentEntries.clear();
		return this;
	}
	
	public ChatManager(int maxSize) {
		this.maxSize = maxSize;
	}
	
	public ChatManager() {
		this(DEFAULT_MAX_SIZE);
	}
	
}
