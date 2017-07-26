package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.ui.UIManager;
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
		
		UITable table = new UITable(2,2);
		
		table.addChild(new UIText("Username:"), 0, 0); //Username
		table.addChild(new UITextBox(), 0, 1); //Username Box
		table.addChild(new UIText("Password:"), 0, 0); //Password
		table.addChild(new UITextBox(), 0, 1); //Password Box
		
		loginWindow.addChild(table);
		
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
