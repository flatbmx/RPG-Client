package com.podts.rpg.client.state;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.network.DefaultPacketHandler;
import com.podts.rpg.client.network.NettyStream;
import com.podts.rpg.client.network.NetworkManager;
import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.RSAHandShakePacket;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.GenericFutureListener;

public class ConnectingState implements GameState {

	private ChannelFuture connectionFuture;

	private String message;

	@Override
	public void mouseClicked(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseDragged(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseMoved(int arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(int arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseWheelMoved(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputEnded() {
		// TODO Auto-generated method stub

	}

	@Override
	public void inputStarted() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAcceptingInput() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setInput(Input arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyPressed(int arg0, char arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(int arg0, char arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerButtonPressed(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerButtonReleased(int arg0, int arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerDownPressed(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerDownReleased(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerLeftPressed(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerLeftReleased(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerRightPressed(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerRightReleased(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerUpPressed(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void controllerUpReleased(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void enter(final GameContainer app, final StateBasedGame game) throws SlickException {
		NetworkManager net = Client.get().getNetworkManager();
		connectionFuture = net.connect("localhost", 1999);

		message = "Connecting";

		connectionFuture.addListener(new GenericFutureListener<ChannelFuture>() {
			@Override
			public void operationComplete(ChannelFuture f) throws Exception {
				if(f.isSuccess()) {
					message = "Connected";
					NettyStream s = (NettyStream) f.channel();

					s.getChannel().pipeline().addLast(new SimpleChannelInboundHandler<AESReplyPacket>() {
						@Override
						protected void channelRead0(ChannelHandlerContext c, AESReplyPacket p) throws Exception {
							Player.me = new Player(p.getPlayerID());
							NettyStream stream = (NettyStream) c.channel();
							stream.setSecretKey(p.getSecretKey());
							game.enterState(1);
							c.pipeline().remove(this);
							c.pipeline().addLast(new DefaultPacketHandler());
						}
					});
					
					s.sendPacket(new RSAHandShakePacket(s.getKeyPair()));
				} else {
					message = "Failed to connect";
				}

			}
		});

	}

	@Override
	public int getID() {
		return -1;
	}

	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {

	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {

	}

	@Override
	public void render(GameContainer app, StateBasedGame game, Graphics g) throws SlickException {
		g.drawString(message, 400, 300);
	}

	@Override
	public void update(GameContainer arg0, StateBasedGame arg1, int arg2) throws SlickException {

	}

}
