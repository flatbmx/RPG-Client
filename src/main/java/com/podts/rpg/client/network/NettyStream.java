package com.podts.rpg.client.network;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.SecretKey;

import com.podts.rpg.client.network.packet.ActionSender;

import io.netty.channel.Channel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class NettyStream extends NioSocketChannel implements NetworkStream {
	
	private static KeyPairGenerator keyPairGenerator;
	protected boolean serverSideClose = false;
	
	static {
		try {
			keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
	}
	
	private final ActionSender sender = new ActionSender(this);
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

	@Override
	public ActionSender getActionSender() {
		return sender;
	}
	
}