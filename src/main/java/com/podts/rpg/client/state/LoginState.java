package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.network.NetworkManager;
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
	
	public static UIText responseText;
	
	public static UITextBox userNameBox;
	public static UISecretTextbox passwordBox;
	
	private UIWindow loginWindow;
	
	@Override
	public void enter(GameContainer gc, StateBasedGame g) throws SlickException {
		UIManager.get().clearChildren().addChild(loginWindow);
		UIManager.get().setFocus(userNameBox);
	}
	
	@Override
	public void init(GameContainer gc, StateBasedGame g) throws SlickException {
		
		Client.get().loadResources();
		
		UIManager.get().setGameContainer(gc);
		
		loginWindow = new UIWindow(500, 300);
		loginWindow.setCenterX(true)
		.setCenterY(true)
		.setCenterParentX(true)
		.setCenterParentY(true);
		
		UITable table = new UITable(200,150,2,4);
		table.setCenterX(true)
		.setCenterY(true);
		
		userNameBox = new UITextBox(120,20) {
			@Override
			protected void handleTextInput(String character) {
				//Do not allow spaces in username.
				if(" ".equals(character)) return;
				super.handleTextInput(character);
			}
		};
		passwordBox = new UISecretTextbox(120, 20);
		
		UITextBox addressBox = new UITextBox(120,20);
		addressBox.setText("localhost");
		UITextBox portBox = new UITextBox(120,20);
		portBox.setText(String.valueOf(Client.DEFAULT_PORT));
		
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
				
				int port = Integer.parseInt(portBox.getText());
				
				responseText.setText("Connecting");
				Client.get().getWorld().clear();
				Client.get().getChatManager().clear();
				
				NetworkManager netManager = Client.get().getNetworkManager();
				
				netManager.connect(addressBox.getText(), port);
				
			}
		};
		
		loginButton.setCenterX(true);
		
		loginWindow.addChild(table);
		loginWindow.addChild(responseText);
		loginWindow.addChild(loginButton);
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
