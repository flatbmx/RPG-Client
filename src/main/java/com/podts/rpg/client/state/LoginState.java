package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.network.packet.LoginPacket;
import com.podts.rpg.client.ui.UIButton;
import com.podts.rpg.client.ui.UIManager;
import com.podts.rpg.client.ui.UISecretTextbox;
import com.podts.rpg.client.ui.UITable;
import com.podts.rpg.client.ui.UIText;
import com.podts.rpg.client.ui.UITextBox;
import com.podts.rpg.client.ui.UIWindow;

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
		
		UITable table = new UITable(400,400,2,2);
		
		UITextBox userNameBox = new UITextBox(0,20);
		UISecretTextbox passwordBox = new UISecretTextbox(0, 20);
		
		table.addChild(new UIText("Username:", 0, 20), 0, 0); //Username
		table.addChild(userNameBox, 1, 0); //Username Box
		table.addChild(new UIText("Password:", 0, 20), 0, 1); //Password
		table.addChild(passwordBox, 0, 1); //Password Box
		
		UIButton loginButton = new UIButton(50,20) {
			@Override
			public void handleMouseClick(MouseClickType clickType) {
				LoginPacket loginPacket = new LoginPacket(userNameBox.getText(), passwordBox.getText());
				//TODO Make easier/faster/cleaner way of sending packets.
				Client.get().getNetworkManager().getStream().sendPacket(loginPacket);
			}
		};
		
		loginWindow.addChild(table).addChild(loginButton);
		
		UIManager.get().clear().addChild(loginWindow);
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
