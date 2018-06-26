package com.podts.rpg.client.network;

import io.netty.handler.codec.LengthFieldPrepender;

public class DefaultFrameEncoder extends LengthFieldPrepender {
	
	public DefaultFrameEncoder() {
		super(4);
	}
	
}
