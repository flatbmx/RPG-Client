package com.podts.rpg.client.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Entity;
import com.podts.rpg.client.model.EntityFactory;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.World;
import com.podts.rpg.client.network.packet.EntityPacket;
import com.podts.rpg.client.network.packet.LoginResponsePacket;
import com.podts.rpg.client.network.packet.LoginResponsePacket.LoginResponseType;
import com.podts.rpg.client.network.packet.PlayerInitPacket;
import com.podts.rpg.client.network.packet.StatePacket;
import com.podts.rpg.client.network.packet.TilePacket;
import com.podts.rpg.client.network.packet.TilePacket.TileSendType;
import com.podts.rpg.client.network.packet.TilePacket.TileUpdateType;
import com.podts.rpg.client.state.LoginState;
import com.podts.rpg.client.state.States;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

class DefaultPacketHandler extends SimpleChannelInboundHandler<Packet> {
	
	private static final Map<Class<?>,BiConsumer<Packet,Stream>> handlers = new HashMap<>();
	
	static {
		
		addHandler(LoginResponsePacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				LoginResponsePacket p = (LoginResponsePacket) op;
				LoginState.responseText.setText(p.getResponse());
				if(p.getType().equals(LoginResponseType.DECLINE)) {
					Client.get().getNetworkManager().close();
				}
			}
		});
		
		addHandler(EntityPacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				EntityPacket p = (EntityPacket) op;
				final World world = Client.get().getWorld();
				synchronized(world) {
					switch(p.getUpdateType()) {
					case UPDATE:
						if(Player.me.getID() == p.getEntityID()) {
							Player.me.getPlayerEntity().setLocation(p.getLocation());
						} else {
							Entity e = world.getEntity(p.getEntityID());
							e.setLocation(p.getLocation());
						}
						break;
					case CREATE:
						world.addEntity(EntityFactory.createEntity(p.getEntityID(), p.getName(), p.getEntityType(), p.getLocation()));
						break;
					case DESTROY:
						Entity e = world.getEntity(p.getEntityID());
						world.removeEntity(e);
						break;
					default:
						break;
					}
				}
			}
		});
		
		addHandler(StatePacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				StatePacket p = (StatePacket) op;
				States st = p.getState();
				Client.get().enterState(st.getID());
			}
		});
		
		addHandler(PlayerInitPacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				PlayerInitPacket p = (PlayerInitPacket) op;
				int id = p.getId();
				Location point = p.getLocation();
				Player.me = new Player(id, point);
			}
		});
		
		addHandler(TilePacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				TilePacket p = (TilePacket) op;
				World world = Client.get().getWorld();
				TileUpdateType updateType = p.getUpdateType();
				if(TileSendType.GROUP.equals(p.getType())) {
					Tile[][] tiles = p.getTiles();
					for(int i=0; i<tiles.length; ++i) {
						for(int j=0; j<tiles[i].length; ++j) {
							if(TileUpdateType.CREATE.equals(updateType)) {
								world.addTile(tiles[i][j]);
							} else {
								world.removeTile(tiles[i][j]);
							}
							
						}
					}
				} else if(TileSendType.SINGLE.equals(p.getType())) {
					if(TileUpdateType.CREATE.equals(updateType))
						world.addTile(p.getTile());
					else
						world.removeTile(p.getTile());
				}
			}
		});
		
	}
	
	private static final void addHandler(Class<?> c, BiConsumer<Packet,Stream> handler) {
		if(handlers.containsKey(c)) throw new IllegalArgumentException("There is already a packet handler for " + c.getSimpleName());
		handlers.put(c, handler);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext c, Packet op) throws Exception {
		
		BiConsumer<Packet,Stream> handler = handlers.get(op.getClass());
		
		if(handler != null) {
			handler.accept(op, (Stream)c.channel());
		} else {
			System.out.println("WARNING: Recieved unhandled " + op.getClass().getSimpleName());
		}
		
	}
	
}
