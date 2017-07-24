package com.podts.rpg.client.network;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyStream extends NioSocketChannel implements Stream {
	
	private static KeyPairGenerator keyPairGenerator;
	
	static {
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private final KeyPair keyPair;
	private SecretKey secret;
	
	@Override
	public KeyPair getKeyPair() {
		return keyPair;
	}
	
	@Override
	public SecretKey getSecretKey() {
		return secret;
	}
	
	public NettyStream setSecretKey(SecretKey k) {
		if(k == null) throw new IllegalArgumentException("Cannot set secret key to null.");
		secret = k;
		return this;
	}
	
	@Override
	public void sendPacket(Packet p) {
		writeAndFlush(p);
	}
	
	public final Channel getChannel() {
		return this;
	}
	
	public NettyStream() {
		keyPair = keyPairGenerator.generateKeyPair();
	}
	
}