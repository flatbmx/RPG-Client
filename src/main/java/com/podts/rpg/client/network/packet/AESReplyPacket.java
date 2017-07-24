package com.podts.rpg.client.network.packet;

import javax.crypto.SecretKey;

import com.podts.rpg.client.network.Packet;

public class AESReplyPacket extends Packet {
	
	private final SecretKey secret;
	private final int playerID;
	
	public final int getPlayerID() {
		return playerID;
	}
	
	public final SecretKey getSecretKey() {
		return secret;
	}
	
	public AESReplyPacket(SecretKey secret, int playerID) {
		this.secret = secret;
		this.playerID = playerID;
	}
	
}
