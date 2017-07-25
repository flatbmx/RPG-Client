package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.ui.UIManager;
import com.podts.rpg.client.ui.UIWindow;

public class LoginState extends UIState {
	
	@Override
	public void init(GameContainer gc, StateBasedGame g) throws SlickException {
		System.out.println("init called.");
		UIManager.clear();
		
		UIWindow loginWindow = new UIWindow(gc.getWidth()/2, gc.getHeight()/2, 300, 200);
		loginWindow.setCenterX(true);
		loginWindow.setCenterY(true);
		loginWindow.setBackgroundColor(new Color(50,50,200));
		loginWindow.setBorderColor(new Color(255,0,0));
		
		UIManager.addObject(loginWindow);
	}
	
	public LoginState() {
		
	}
	
}
