package com.podts.rpg.client.network;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.network.packet.LoginResponsePacket;
import com.podts.rpg.client.network.packet.TilePacket;
import com.podts.rpg.client.network.packet.TilePacket.TileSendType;
import com.podts.rpg.client.state.LoginState;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DefaultPacketHandler extends SimpleChannelInboundHandler<Packet> {
	
	private static final Map<Class<?>,BiConsumer<Packet,Stream>> handlers = new HashMap<>();
	
	static {
		
		addHandler(LoginResponsePacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				LoginResponsePacket p = (LoginResponsePacket) op;
				LoginState.responseText.setText(p.getResponse());
			}
		});
		
		addHandler(TilePacket.class, new BiConsumer<Packet,Stream>() {
			@Override
			public void accept(Packet op, Stream s) {
				
			}
		});
		
	}
	
	private static final void addHandler(Class<?> c, BiConsumer<Packet,Stream> handler) {
		handlers.put(c, handler);
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext c, Packet op) throws Exception {
		
		BiConsumer<Packet,Stream> handler = handlers.get(op.getClass());
		
		if(handler != null) {
			handler.accept(op, (Stream)c.channel());
		} else {
			System.out.println("Recieved unhandled " + op.getClass().getSimpleName());
		}
		
	}
	
}
