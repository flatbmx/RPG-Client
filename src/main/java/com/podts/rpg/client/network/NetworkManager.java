package com.podts.rpg.client.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.util.concurrent.GenericFutureListener;

public class NetworkManager {
	
	private String host;
	private int port;
	private EventLoopGroup workerGroup;
	private Bootstrap b;
	
	private NettyStream stream;
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
	
	public final NettyStream getStream() {
		return stream;
	}
	
	public ChannelFuture connect(String host, int port) {
		
		this.host = host;
		this.port = port;
		try {
			b = new Bootstrap();
			workerGroup = new NioEventLoopGroup();
			b.group(workerGroup);
			b.channel(NettyStream.class);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel channel) throws Exception {
					channel.pipeline().addLast("frameEncoder", new DefaultFrameEncoder())
					.addLast("packetEncoder", new DefaultPacketEncoder())
					.addLast("frameDecoder", new DefaultFrameDecoder())
					.addLast("packetDecoder", new DefaultPacketDecoder());
				}
			});
			
			return b.connect("localhost", 1999).addListener(new GenericFutureListener<ChannelFuture>() {
				@Override
				public void operationComplete(ChannelFuture f) throws Exception {
					if(f.isSuccess()) {
						stream = (NettyStream) f.channel();
					} else {
						
					}
				}
			 });
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
}
