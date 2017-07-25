package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.ui.UIManager;
import com.podts.rpg.client.ui.UIWindow;

public class LoginState extends UIState {
	
	@Override
	public void init(GameContainer gc, StateBasedGame g) throws SlickException {
		UIManager.clear();
		
		UIWindow loginWindow = new UIWindow(gc.getWidth()/2, gc.getHeight()/2, 500, 300);
		loginWindow.setCenterX(true);
		loginWindow.setCenterY(true);
		
		UIManager.addObject(loginWindow);
	}
	
	@Override
	public void render(GameContainer gc, StateBasedGame game, Graphics g) throws SlickException {
		g.setColor(new Color(50,20,0));
		g.fillRect(0, 0, gc.getWidth(), gc.getHeight());
		super.render(gc, game, g);
	}
	
	public final int getID() {
		return States.LOGIN.getID();
	}
	
	public LoginState() {
		
	}
	
}
