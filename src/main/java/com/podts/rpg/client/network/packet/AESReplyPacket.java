package com.podts.rpg.client.network.packet;

import javax.crypto.SecretKey;

import com.podts.rpg.client.network.Packet;

public class AESReplyPacket extends Packet {
	
	private final SecretKey secret;
	
	public final SecretKey getSecretKey() {
		return secret;
	}
	
	public AESReplyPacket(SecretKey secret) {
		this.secret = secret;
	}
	
}
