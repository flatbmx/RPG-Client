package com.podts.rpg.client.network;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.EntityPacket;
import com.podts.rpg.client.network.packet.RSAHandShakePacket;
import com.podts.rpg.client.state.LoginState;
import com.podts.rpg.client.state.States;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
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
	
	public static void logout() {
		logout(false);
	}
	
	public static void logout(boolean serverSide) {
		Client.get().getNetworkManager().close();
		Client.get().enterState(States.LOGIN.getID());
		if(serverSide)
			LoginState.responseText.setText("Disconnected!");
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
					channel.closeFuture().addListener(new ChannelFutureListener() {
					    @Override
					    public void operationComplete(ChannelFuture future) throws Exception {
					    	NettyStream s = (NettyStream) future.channel();
					        NetworkManager.logout(s.serverSideClose);
					    }
					});
					channel.pipeline().addLast(new ChannelWatcher())
					.addLast("frameEncoder", new DefaultFrameEncoder())
					.addLast("packetEncoder", new DefaultPacketEncoder())
					.addLast("frameDecoder", new DefaultFrameDecoder())
					.addLast("packetDecoder", new DefaultPacketDecoder());
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
									c.pipeline().remove(this)
									.addLast("player entity handler", new SimpleChannelInboundHandler<EntityPacket>() {
										@Override
										protected void channelRead0(ChannelHandlerContext ctx, EntityPacket p) throws Exception {
											ctx.pipeline().remove(this);
											Player.handleFirstEntityPacket(p);
										}
									})
									.addLast("packetHandler", new DefaultPacketHandler());
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
	        NetworkManager.this.close();
	    }
		
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
			if(cause.getMessage().equals("An existing connection was forcibly closed by the remote host")) {
				NettyStream s = (NettyStream) ctx.channel();
				s.serverSideClose = true;
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
		getStream().close();
		workerGroup.shutdownGracefully();
	}

	
}
