package com.podts.rpg.client.network;

import java.security.KeyPair;

import javax.crypto.SecretKey;

public interface Stream {
	
	public boolean isOpen();
	
	public SecretKey getSecretKey();
	
	public KeyPair getKeyPair();
	
	public void sendPacket(Packet p);
	
}
