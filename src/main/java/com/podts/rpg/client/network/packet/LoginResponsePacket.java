package com.podts.rpg.client.network.packet;

import com.podts.rpg.client.network.Packet;

public final class LoginResponsePacket extends Packet {
	
	public enum LoginResponseType {
		WAIT(),
		ACCEPT(),
		DECLINE();
	}
	
	private final LoginResponseType type;
	private final String response;
	
	public LoginResponseType getType() {
		return type;
	}
	
	public String getResponse() {
		return response;
	}
	
	public LoginResponsePacket(LoginResponseType type, String response) {
		this.type = type;
		this.response = response;
	}
	
}
