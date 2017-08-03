package com.podts.rpg.client.network;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.Tile.TileType;
import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.LoginResponsePacket;
import com.podts.rpg.client.network.packet.LoginResponsePacket.LoginResponseType;
import com.podts.rpg.client.network.packet.TilePacket;
import com.podts.rpg.client.network.packet.TilePacket.TileSendType;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

class DefaultPacketDecoder extends ByteToMessageDecoder {
	
	private static final PacketConstructor[] packetConstructors;
	
	private static final void addConstructor(int id, PacketConstructor c) {
		packetConstructors[id] = c;
		c.init();
	}
	
	private static final int PID_RSAHANDSHAKE = 0;
	private static final int PID_LOGINREQUST = 1;
	private static final int PID_TILE = 2;
	private static final int PID_ENTITY = 3;
	private static final int PID_MESSAGE = 4;
	
	static {
		packetConstructors = new PacketConstructor[128];

		addConstructor(PID_RSAHANDSHAKE, new PacketConstructor() {
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				byte[] encryptedBytes = new byte[buf.readableBytes()];
				buf.readBytes(encryptedBytes, 0, buf.readableBytes());

				try {
					Cipher c = Cipher.getInstance("RSA");
					c.init(Cipher.DECRYPT_MODE, s.getKeyPair().getPrivate());
					byte[] secretBytes = c.doFinal(encryptedBytes);

					SecretKey secret = new SecretKeySpec(secretBytes, 0, secretBytes.length, "AES");

					return new AESReplyPacket(secret);

				} catch (Exception e) {
					e.printStackTrace();	
				}

				return null;
			}
		});
		
		addConstructor(PID_LOGINREQUST, new PacketConstructor() {
			private final LoginResponseType[] type = new LoginResponseType[LoginResponseType.values().length];
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				LoginResponseType rType = type[buf.readByte()];
				String response = readString(buf);
				return new LoginResponsePacket(rType, response);
			}
			public void init() {
				type[0] = LoginResponseType.ACCEPT;
				type[1] = LoginResponseType.DECLINE;
			}
		});
		
		addConstructor(PID_TILE, new PacketConstructor() {
			private final TileSendType[] sendTypes = new TileSendType[TileSendType.values().length];
			private final TileType[] types = new TileType[TileType.values().length];
			public void init() {
				sendTypes[0] = TileSendType.GROUP;
				sendTypes[1] = TileSendType.SINGLE;
				
				types[0] = TileType.VOID;
				types[1] = TileType.DIRT;
				types[2] = TileType.GRASS;
				types[3] = TileType.WATER;
			}
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				TileSendType sendType = sendTypes[buf.readByte()];
				if(sendType.equals(TileSendType.SINGLE)) {
					TileType type = types[buf.readByte()];
					Location location = readLocation(buf);
					Tile tile = new Tile(type, location);
					return new TilePacket(tile);
				} else if(sendType.equals(TileSendType.GROUP)) {
					Location topLeft = readLocation(buf);
					int width = buf.readInt();
					int height = buf.readInt();
					Tile[][] tiles = new Tile[width][height];
					for(int y=0; y<height; ++y) {
						for(int x=0; x<height; ++x) {
							tiles[x][y] = new Tile(types[buf.readByte()], topLeft.move(x,y,0));
						}
					}
					return new TilePacket(tiles);
				}
				return null;
			}
		});
		
		/*addConstructor(5, new PacketConstructor() {
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
				});*/
		
	}
	
	private static String readString(ByteBuf buf) {
		int length = buf.readInt();
		byte[] arr = new byte[length];
		buf.readBytes(arr);
		try {
			return new String(arr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static Location readLocation(ByteBuf buf) {
		return new Location(buf.readInt(), buf.readInt(), buf.readInt());
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
	
}