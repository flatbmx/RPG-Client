package com.podts.rpg.client.network;

import java.io.UnsupportedEncodingException;
import java.security.PrivateKey;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.EntityType;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.Tile.TileType;
import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.AcknowledgePacket;
import com.podts.rpg.client.network.packet.EntityPacket;
import com.podts.rpg.client.network.packet.LoginResponsePacket;
import com.podts.rpg.client.network.packet.LoginResponsePacket.LoginResponseType;
import com.podts.rpg.client.network.packet.MessagePacket;
import com.podts.rpg.client.network.packet.PlayerInitPacket;
import com.podts.rpg.client.network.packet.StatePacket;
import com.podts.rpg.client.network.packet.TilePacket;
import com.podts.rpg.client.network.packet.TilePacket.TileSendType;
import com.podts.rpg.client.network.packet.TilePacket.TileUpdateType;
import com.podts.rpg.client.state.States;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

class DefaultPacketDecoder extends ByteToMessageDecoder {
	
	private static final PacketConstructor[] packetConstructors;
	
	private static final void addConstructor(int id, PacketConstructor c) {
		packetConstructors[id] = c;
		c.init();
	}
	
	private static final int PID_AESREPLY = 0;
	private static final int PID_LOGINRESPONSE = 1;
	private static final int PID_TILE = 2;
	private static final int PID_INIT = 3;
	private static final int PID_STATE = 4;
	private static final int PID_ENTITY = 5;
	private static final int PID_MESSAGE = 6;
	private static final int PID_ACK = 7;
	
	
	
	static {
		packetConstructors = new PacketConstructor[128];

		addConstructor(PID_AESREPLY, new PacketConstructor() {
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
		
		addConstructor(PID_ACK, new PacketConstructor() {
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				return new AcknowledgePacket(buf.readInt());
			}
		});
		
		addConstructor(PID_LOGINRESPONSE, new PacketConstructor() {
			private final LoginResponseType[] type = new LoginResponseType[LoginResponseType.values().length];
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				LoginResponseType rType = type[buf.readByte()];
				String response = readString(buf);
				return new LoginResponsePacket(rType, response);
			}
			public void init() {
				type[0] = LoginResponseType.WAIT;
				type[1] = LoginResponseType.ACCEPT;
				type[2] = LoginResponseType.DECLINE;
			}
		});
		
		addConstructor(PID_MESSAGE, new PacketConstructor() {
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				return new MessagePacket(readEncryptedString(s, buf));
			}
		});
		
		addConstructor(PID_TILE, new PacketConstructor() {
			private final TileUpdateType[] updateTypes = new TileUpdateType[TileUpdateType.values().length];
			private final TileSendType[] sendTypes = new TileSendType[TileSendType.values().length];
			private final TileType[] types = new TileType[TileType.values().length];
			
			public void init() {
				updateTypes[0] = TileUpdateType.CREATE;
				updateTypes[1] = TileUpdateType.DESTROY;
				
				sendTypes[0] = TileSendType.GROUP;
				sendTypes[1] = TileSendType.SINGLE;
				
				types[0] = TileType.VOID;
				types[1] = TileType.DIRT;
				types[2] = TileType.GRASS;
				types[3] = TileType.SAND;
				types[4] = TileType.WATER;
			}
			
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				TileUpdateType updateType = updateTypes[buf.readByte()];
				TileSendType sendType = sendTypes[buf.readByte()];
				if(TileUpdateType.CREATE.equals(updateType)) {
					if(sendType.equals(TileSendType.SINGLE)) {
						TileType type = types[buf.readByte()];
						Location location = readLocation(buf);
						Tile tile = new Tile(type, location);
						return new TilePacket(tile, location, updateType);
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
						return new TilePacket(tiles, updateType);
					}
				} else if(TileUpdateType.DESTROY.equals(updateType)) {
					if(sendType.equals(TileSendType.SINGLE)) {
						Location location = readLocation(buf);
						Tile tile = Client.get().getWorld().getTile(location);
						return new TilePacket(tile, location, updateType);
					} else if(sendType.equals(TileSendType.GROUP)) {
						Location topLeft = readLocation(buf);
						int width = buf.readInt();
						int height = buf.readInt();
						Tile[][] tiles = new Tile[width][height];
						for(int y=0; y<height; ++y) {
							for(int x=0; x<height; ++x) {
								tiles[x][y] = Client.get().getWorld().getTile(topLeft.move(x,y,0));
							}
						}
						return new TilePacket(tiles, updateType);
					}
				}
				
				return null;
			}
		});
		
		addConstructor(PID_STATE, new PacketConstructor() {
			private final States[] states = States.values();
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				byte stateID = buf.readByte();
				States st = states[stateID];
				return new StatePacket(st);
			}
		});
		
		addConstructor(PID_INIT, new PacketConstructor() {
			@Override
			public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
				int id = buf.readInt();
				Location point = readLocation(buf);
				return new PlayerInitPacket(id, point);
			}
		});
		
		addConstructor(PID_ENTITY, new PacketConstructor() {
					private final EntityPacket.UpdateType[] updateTypes = new EntityPacket.UpdateType[EntityPacket.UpdateType.values().length];
					private final EntityType[] entityTypes = new EntityType[EntityType.values().length];
					
					@Override
					public void init() {
						updateTypes[0] = EntityPacket.UpdateType.CREATE;
						updateTypes[1] = EntityPacket.UpdateType.UPDATE;
						updateTypes[2] = EntityPacket.UpdateType.DESTROY;
						
						entityTypes[0] = EntityType.PLAYER;
					}
					
					@Override
					public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf) {
						int typeID = buf.readByte();
						EntityPacket.UpdateType type = updateTypes[typeID];
						int entityID = buf.readInt();
						
						EntityType eType = null;
						Location loc = null;
						
						switch(type) {
						case DESTROY:
							return new EntityPacket(type, entityID);
						case CREATE:
							String name = readString(buf);
							int entityType = buf.readByte();
							eType = entityTypes[entityType];
							loc = readLocation(buf);
							return new EntityPacket(type, name, entityID, eType, loc);
						case UPDATE:
							loc = readLocation(buf);
							return new EntityPacket(type, entityID, eType, loc);
						}
						
						return null;
					}
				});
		
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
	
	private static String readEncryptedString(Stream stream, ByteBuf buf) {
		int encryptedLength = buf.readInt();
		byte[] encryptedBytes = new byte[encryptedLength];
		buf.readBytes(encryptedBytes);
		ByteBuf realBuf = Unpooled.buffer();
		realBuf.writeBytes(decrypt(encryptedBytes, stream.getSecretKey()));
		realBuf.resetReaderIndex();
		int size = realBuf.readInt();
		String result = null;
		byte[] realChars = new byte[size];
		realBuf.readBytes(realChars);
		try {
			result = new String(realChars, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	public static byte[] decrypt(byte[] bytes, SecretKey secretKey) {
		try {
			Cipher cipher = Cipher.getInstance("AES");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] decrypt(byte[] bytes, PrivateKey publicKey) {
		try {
			Cipher cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);
			return cipher.doFinal(bytes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static final Location readLocation(ByteBuf buf) {
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
					out.add(p);
				}
					
			} else {
				System.out.println("WARNING ==== Recieved unknown Packet OPCODE = " + opCode + " with size " + (size-1));
				buf.skipBytes(size-1);
			}
		}
		
	}
	
	private static interface PacketConstructor {
		
		public Packet construct(NettyStream s, int size, byte opCode, ByteBuf buf);
		
		public default void init() {
			
		}
		
	}
	
}