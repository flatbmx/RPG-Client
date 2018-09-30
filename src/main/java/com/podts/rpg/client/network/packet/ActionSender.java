package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.NetworkStream;

public final class ActionSender {
	
	private final NetworkStream stream;
	
	public void sendLoginRequest(String username, String password) {
		stream.sendPacket(new LoginPacket(username, password));
	}
	
	public ActionSender(NetworkStream stream) {
		this.stream = stream;
	}
	
}
