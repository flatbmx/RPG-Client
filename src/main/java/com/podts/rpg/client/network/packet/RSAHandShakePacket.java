package com.podts.rpg.client.network.packet;

import java.security.KeyPair;

import com.podts.rpg.client.network.Packet;

public class RSAHandShakePacket extends Packet {
	
	private final KeyPair pair;
	
	public final KeyPair getKeyPair() {
		return pair;
	}
	
	public RSAHandShakePacket(KeyPair pair) {
		this.pair = pair;
	}
	
}
