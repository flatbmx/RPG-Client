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
	
	public static UIText responseText;
	
	@Override
	public void init(GameContainer gc, StateBasedGame g) throws SlickException {
		
		UIManager.get().setGameContainer(gc);
		
		UIWindow loginWindow = new UIWindow(500, 300);
		loginWindow.setCenterX(true)
		.setCenterY(true)
		.setCenterParentX(true)
		.setCenterParentY(true);
		
		UITable table = new UITable(200,150,2,4);
		table.setCenterX(true)
		.setCenterY(true);
		
		UITextBox userNameBox = new UITextBox(120,20);
		UISecretTextbox passwordBox = new UISecretTextbox(120, 20);
		
		UITextBox addressBox = new UITextBox(120,20);
		addressBox.setText("localhost");
		UITextBox portBox = new UITextBox(120,20);
		portBox.setText("1999");
		
		table.addChild(new UIText("Address:", 75, 20), 0, 0);
		table.addChild(addressBox, 1, 0);
		table.addChild(new UIText("Port:", 75, 20), 0, 1);
		table.addChild(portBox, 1, 1);
		table.addChild(new UIText("Username:", 75, 20), 0, 2); //Username
		table.addChild(userNameBox, 1, 2); //Username Box
		table.addChild(new UIText("Password:", 75, 20), 0, 3); //Password
		table.addChild(passwordBox, 1, 3); //Password Box
		
		responseText = new UIText(300,20);
		responseText.setCenterX(true).setBackgroundColor(null);
		
		UIButton loginButton = new UIButton(50,20) {
			@Override
			public void handleMouseClick(MouseClickType clickType) {
				LoginPacket loginPacket = new LoginPacket(userNameBox.getText(), passwordBox.getText());
				
				int port = Integer.parseInt(portBox.getText());
				
				responseText.setText("Connecting");
				
				Client.get().getNetworkManager().connect(addressBox.getText(), port).addListener(new GenericFutureListener<ChannelFuture>() {
					@Override
					public void operationComplete(ChannelFuture f) throws Exception {
						if(f.isSuccess()) {
							responseText.setText("Connected");
							NettyStream s = (NettyStream) f.channel();
							
							
							
							s.getChannel().pipeline().addLast(new SimpleChannelInboundHandler<AESReplyPacket>() {
								@Override
								protected void channelRead0(ChannelHandlerContext c, AESReplyPacket p) throws Exception {
									NettyStream stream = (NettyStream) c.channel();
									stream.setSecretKey(p.getSecretKey());
									c.pipeline().remove(this);
									c.pipeline().addLast(new DefaultPacketHandler());
									stream.sendPacket(loginPacket);
									responseText.setText("Secured.");
								}
							});
							
							s.sendPacket(new RSAHandShakePacket(s.getKeyPair()));
							
						} else {
							responseText.setText("Failed to connect.");
						}
					}
				});
				
			}
		};
		
		loginButton.setCenterX(true);
		
		loginWindow.addChild(table);
		loginWindow.addChild(responseText);
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
