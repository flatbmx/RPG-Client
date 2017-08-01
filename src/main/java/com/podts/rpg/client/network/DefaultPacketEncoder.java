package com.podts.rpg.client.network;

import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import com.podts.rpg.client.network.packet.LoginPacket;
import com.podts.rpg.client.network.packet.RSAHandShakePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class DefaultPacketEncoder extends MessageToByteEncoder<Packet> {
	
	private static final byte OP_RSAHANDSHAKE = 0, OP_LOGIN = 1;
	
	private final Map<Class<?>,PacketEncoder> encoders = new HashMap<>();
	
	@Override
	protected void encode(ChannelHandlerContext c, Packet p, ByteBuf buf) throws Exception {
		
		if(p == null) return;
		Class<?> cp = p.getClass();
		PacketEncoder en = encoders.get(cp);
		
		if(en == null) {
			System.out.println(cp.getName());
			System.out.println("No encoder found for " + p.getClass());
			return;
		}
		
		Stream s = (Stream) c.channel();
		en.encode(s, p, buf);
		
	}
	
	private static void writeEncryptedString(String string, Stream stream, ByteBuf buf) {
		try {
			ByteBuf plainBuf = Unpooled.buffer();
			byte[] plain = string.getBytes("UTF-8");
			plainBuf.writeInt(plain.length).writeBytes(plain);
			byte[] encryptedBytes = encrypt(plainBuf.array(), stream.getSecretKey());
			buf.writeInt(encryptedBytes.length).writeBytes(encryptedBytes);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}	
	}
	
	private static byte[] encrypt(byte[] bytes, SecretKey secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			return cipher.doFinal(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static byte[] encrypt(byte[] bytes, PublicKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private interface PacketEncoder {
		public void encode(Stream s, Packet p, ByteBuf buf);
	}
	
	public DefaultPacketEncoder() {
		encoders.put(RSAHandShakePacket.class, new PacketEncoder() {
			@Override
			public void encode(Stream s, Packet op, ByteBuf buf) {
				if(!(op instanceof RSAHandShakePacket)) throw new IllegalArgumentException();
				RSAHandShakePacket p = (RSAHandShakePacket) op;
				buf.writeByte(OP_RSAHANDSHAKE);
				buf.writeBytes(p.getKeyPair().getPublic().getEncoded());
			}
		});
		encoders.put(LoginPacket.class, new PacketEncoder() {
			@Override
			public void encode(Stream s, Packet op, ByteBuf buf) {
				if(!(op instanceof LoginPacket)) throw new IllegalArgumentException();
				LoginPacket p = (LoginPacket) op;
				buf.writeByte(OP_LOGIN);
				writeEncryptedString(p.getUsername(), s, buf);
				writeEncryptedString(p.getPassword(), s, buf);
			}
		});
	}
	
}
