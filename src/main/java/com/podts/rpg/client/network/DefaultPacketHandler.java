package com.podts.rpg.client.network;

import com.podts.rpg.client.network.packet.AESReplyPacket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class DefaultPacketHandler extends SimpleChannelInboundHandler<Packet> {
	
	@Override
	protected void channelRead0(ChannelHandlerContext c, Packet op) throws Exception {
		
		if(op instanceof AESReplyPacket) {
			AESReplyPacket p = (AESReplyPacket) op;
			
			
			
		}
		
	}
	
}
