package com.podts.rpg.client.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.podts.rpg.client.Client;
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
				if(Player.me.getID() == p.getEntityID()) {
					Player.me.getPlayerEntity().setLocation(p.getLocation());
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
				if(TileSendType.GROUP.equals(p.getType())) {
					Tile[][] tiles = p.getTiles();
					for(int i=0; i<tiles.length; ++i) {
						for(int j=0; j<tiles[i].length; ++j) {
							world.addTile(tiles[i][j]);
						}
					}
				} else if(TileSendType.SINGLE.equals(p.getType())) {
					world.addTile(p.getTile());
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
