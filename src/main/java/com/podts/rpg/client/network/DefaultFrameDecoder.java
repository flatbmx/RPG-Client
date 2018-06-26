package com.podts.rpg.client.network;

import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

public class DefaultFrameDecoder extends LengthFieldBasedFrameDecoder {

	public DefaultFrameDecoder() {
		super(20_000, 0, 4);
	}

}
