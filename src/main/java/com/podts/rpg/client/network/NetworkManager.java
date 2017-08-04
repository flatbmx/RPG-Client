package com.podts.rpg.client.network;

import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.LoginPacket;
import com.podts.rpg.client.network.packet.RSAHandShakePacket;
import com.podts.rpg.client.state.LoginState;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
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
					.addLast("packetDecoder", new DefaultPacketDecoder())
					.addLast(new ChannelWatcher());
				}
			});
			
			return b.connect(host, port).addListener(new GenericFutureListener<ChannelFuture>() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if(f.isSuccess()) {
							stream = (NettyStream) f.channel();
							LoginState.responseText.setText("Connected");
							NettyStream s = (NettyStream) f.channel();
							s.getChannel().pipeline().addLast(new SimpleChannelInboundHandler<AESReplyPacket>() {
								@Override
								protected void channelRead0(ChannelHandlerContext c, AESReplyPacket p) throws Exception {
									NettyStream stream = (NettyStream) c.channel();
									stream.setSecretKey(p.getSecretKey());
									c.pipeline().remove(this);
									c.pipeline().addLast(new DefaultPacketHandler());
									LoginState.responseText.setText("Secured.");
									getStream().getActionSender().sendLoginRequest(LoginState.userNameBox.getText(), LoginState.passwordBox.getText());
								}
							});
							
							s.sendPacket(new RSAHandShakePacket(s.getKeyPair()));
						} else {
							LoginState.responseText.setText("Failed to connect.");
						}
					}
				});
			
		} catch(Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	private final class ChannelWatcher extends ChannelOutboundHandlerAdapter {
		
		@Override
	    public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
	        
	    }
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			if(cause.getMessage().equals("An existing connection was forcibly closed by the remote host")) {
				LoginState.responseText.setText("Disconnected!");
			} else {
				cause.printStackTrace();
			}
			ctx.close();
		}
		
	}
	
	public void waitForSecuredConnection() {
		
	}
	
	private class DefaultFrameEncoder extends LengthFieldPrepender {
		
		public DefaultFrameEncoder() {
			super(4);
		}
		
	}
	
	private class DefaultFrameDecoder extends LengthFieldBasedFrameDecoder {

		public DefaultFrameDecoder() {
			super(20_000, 0, 4);
		}

	}

	public void close() {
		workerGroup.shutdownGracefully();
	}

	
}
