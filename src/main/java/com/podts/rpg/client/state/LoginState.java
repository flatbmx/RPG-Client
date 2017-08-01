package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.network.DefaultPacketHandler;
import com.podts.rpg.client.network.NettyStream;
import com.podts.rpg.client.network.packet.AESReplyPacket;
import com.podts.rpg.client.network.packet.LoginPacket;
import com.podts.rpg.client.network.packet.RSAHandShakePacket;
import com.podts.rpg.client.ui.UIButton;
import com.podts.rpg.client.ui.UIManager;
import com.podts.rpg.client.ui.UISecretTextbox;
import com.podts.rpg.client.ui.UITable;
import com.podts.rpg.client.ui.UIText;
import com.podts.rpg.client.ui.UITextBox;
import com.podts.rpg.client.ui.UIWindow;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;

/**
 * A state for when a user has not logged in.
 * @author Will
 *
 */
public class LoginState extends UIState {

	@Override
	public void init(GameContainer gc, StateBasedGame g) throws SlickException {
		
		UIManager.get().setGameContainer(gc);
		
		UIWindow loginWindow = new UIWindow(500, 300);
		loginWindow.setCenterX(true)
		.setCenterY(true)
		.setCenterParentX(true)
		.setCenterParentY(true);
		
		UITable table = new UITable(300,75,2,2);
		table.setCenterX(true)
		.setCenterY(true);
		
		UITextBox userNameBox = new UITextBox(120,20);
		UISecretTextbox passwordBox = new UISecretTextbox(120, 20);
		
		table.addChild(new UIText("Username:", 75, 20), 0, 0); //Username
		table.addChild(userNameBox, 1, 0); //Username Box
		table.addChild(new UIText("Password:", 75, 20), 0, 1); //Password
		table.addChild(passwordBox, 1, 1); //Password Box
		
		userNameBox.setText("blurh");
		passwordBox.setText("123456");
		
		UIButton loginButton = new UIButton(50,20) {
			@SuppressWarnings({ "rawtypes", "unchecked" })
			@Override
			public void handleMouseClick(MouseClickType clickType) {
				LoginPacket loginPacket = new LoginPacket(userNameBox.getText(), passwordBox.getText());
				System.out.println("Clicked login button");
				
				Client.get().getNetworkManager().connect("localhost", 1999).addListener(new GenericFutureListener<ChannelFuture>() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if(f.isSuccess()) {
							System.out.println("Connected");
							NettyStream s = (NettyStream) f.channel();

							s.getChannel().pipeline().addLast(new SimpleChannelInboundHandler<AESReplyPacket>() {
								@Override
								protected void channelRead0(ChannelHandlerContext c, AESReplyPacket p) throws Exception {
									Player.me = new Player(p.getPlayerID());
									NettyStream stream = (NettyStream) c.channel();
									stream.setSecretKey(p.getSecretKey());
									System.out.println("Recieved AES reply.");
									c.pipeline().remove(this);
									c.pipeline().addLast(new DefaultPacketHandler());
									stream.sendPacket(loginPacket);
								}
							});
							
							s.sendPacket(new RSAHandShakePacket(s.getKeyPair()));
						} else {
							System.out.println("Failed to connect");
						}
					}
				});
				
			}
		};
		
		loginButton.setCenterX(true);
		
		loginWindow.addChild(table);
		loginWindow.addChild(loginButton);
		
		UIManager.get().clear().addChild(loginWindow);
		UIManager.get().setFocus(userNameBox);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(Color.black);
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		super.render(gc, game, g);
	}
	
	@Override
	public void update(GameContainer container, StateBasedGame game, int delta) throws SlickException {
		
	}
	
	public final int getID() {
		return States.LOGIN.getID();
	}
	
	public LoginState() {
		
	}
	
}
