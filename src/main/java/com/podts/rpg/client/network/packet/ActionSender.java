package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.Stream;

public final class ActionSender {
	
	private final Stream stream;
	
	public void sendLoginRequest(String username, String password) {
		stream.sendPacket(new LoginPacket(username, password));
	}
	
	public ActionSender(Stream stream) {
		this.stream = stream;
	}
	
}
