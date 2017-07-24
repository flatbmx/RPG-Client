package com.podts.rpg.client.network;

import java.util.HashMap;
import java.util.Map;

import com.podts.rpg.client.network.packet.RSAHandShakePacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class DefaultPacketEncoder extends MessageToByteEncoder<Packet> {
	
	private static final byte OP_RSAHANDSHAKE = 0;
	
	private final Map<Class<?>,PacketEncoder> encoders = new HashMap<Class<?>,PacketEncoder>();
	
	@Override
	protected void encode(ChannelHandlerContext c, Packet p, ByteBuf buf) throws Exception {
		
		if(p == null) return;
		Class<?> cp = p.getClass();
		PacketEncoder en = encoders.get(cp);
		
		if(en == null) {
			System.out.println(cp.getName());
			System.out.println("No encoder found.");
			return;
		}
		
		Stream s = (Stream) c.channel();
		
		en.encode(s, p, buf);
		
		System.out.println("Encoded packet.");
		
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
				byte[] bytes = p.getKeyPair().getPublic().getEncoded();
				System.out.println("Handshake was of size " + bytes.length);
				buf.writeBytes(bytes);
			}
		});
	}
	
}
