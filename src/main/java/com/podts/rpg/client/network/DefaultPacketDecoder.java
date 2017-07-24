package com.podts.rpg.client.network;

import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.podts.rpg.client.model.EntityType;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.EntityPacket;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class DefaultPacketDecoder extends ByteToMessageDecoder {
	
	private static final PacketConstructor[] packetConstructors;
	
	private static final void addConstructor(int id, PacketConstructor c) {
		packetConstructors[id] = c;
		c.init();
	}

	static {
		packetConstructors = new PacketConstructor[128];

		addConstructor(1, new PacketConstructor() {
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				int playerID = buf.readInt();
				int shipID = buf.readInt();
				byte[] encryptedBytes = new byte[buf.readableBytes()];
				buf.readBytes(encryptedBytes, 0, buf.readableBytes());

				try {
					Cipher c = Cipher.getInstance("RSA");
					c.init(Cipher.DECRYPT_MODE, s.getKeyPair().getPrivate());
					byte[] secretBytes = c.doFinal(encryptedBytes);

					SecretKey secret = new SecretKeySpec(secretBytes, 0, secretBytes.length, "AES");

					return new AESReplyPacket(secret, playerID);

				} catch (Exception e) {
					e.printStackTrace();	
				}

				return null;
			}
		});
		
		addConstructor(2, new PacketConstructor() {
					private final EntityPacket.UpdateType[] updateTypes = new EntityPacket.UpdateType[EntityPacket.UpdateType.values().length];
					private final EntityType[] entityTypes = new EntityType[EntityType.values().length];
					
					@Override
					public void init() {
						updateTypes[0] = EntityPacket.UpdateType.CREATE;
						updateTypes[1] = EntityPacket.UpdateType.UPDATE;
						updateTypes[2] = EntityPacket.UpdateType.DESTROY;
						
						entityTypes[0] = EntityType.SHIP_ESCAPEPOD;
					}
					
					@Override
					public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
						int typeID = buf.readByte();
						EntityPacket.UpdateType type = updateTypes[typeID];
						int entityID = buf.readInt();
						
						double x, y;
						EntityType eType = null;
						
						switch(type) {
						case DESTROY:
							return new EntityPacket(type, entityID);
						case CREATE:
							int entityType = buf.readByte();
							eType = entityTypes[entityType];
						case UPDATE:
							x = buf.readDouble();
							y = buf.readDouble();
							Location loc = new Location(x, y);
							return new EntityPacket(type, entityID, eType, loc);
						}
						
						return null;
					}
				});
		
	}
	
	@Override
	protected void decode(ChannelHandlerContext c, ByteBuf buf, List<Object> out) throws Exception {
		
		NettyStream stream = (NettyStream) c.channel();
		
		int size = buf.readInt();
		byte opCode = buf.readByte();
		
		if(opCode > -1 && opCode < packetConstructors.length) {
			if(packetConstructors[opCode] != null) {
				Packet p = packetConstructors[opCode].construct(stream, size - 5, opCode, buf);
				if(p != null) {
					System.out.println("Recieved " + p.getClass().getSimpleName());
					out.add(p);
				}
					
			}
		}
		
	}
	
	private static interface PacketConstructor {
		
		public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf);
		
		public default void init() {
			
		}
		
	}
	
	public DefaultPacketDecoder() {
	
	
	
	}
	
}