package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.Packet;

public final class MessagePacket extends Packet {
	
	private final String message;
	
	public String getMessage() {
		return message;
	}
	
	public MessagePacket(String message) {
		this.message = message;
	}
	
}
