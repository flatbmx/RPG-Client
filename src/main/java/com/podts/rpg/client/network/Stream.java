package com.podts.rpg.client.network;

import java.security.KeyPair;

import javax.crypto.SecretKey;

import com.podts.rpg.client.network.packet.ActionSender;

public interface Stream {
	
	public boolean isOpen();
	
	public SecretKey getSecretKey();
	
	public KeyPair getKeyPair();
	
	public void sendPacket(Packet p);
	
	public ActionSender getActionSender();
	
}
