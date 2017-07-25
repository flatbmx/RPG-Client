package com.podts.rpg.client;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.network.NetworkManager;
import com.podts.rpg.client.state.LoginState;
import com.podts.rpg.client.state.PlayingState;
import com.podts.rpg.client.ui.UIManager;

public class Client extends StateBasedGame {
	
	private static Client instance;
	
	public static final Client get() {
		return instance;
	}
	
	private final NetworkManager networkManager = new NetworkManager();
	
	public final NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new LoginState());
		addState(new PlayingState());
	}
	
	public Client() {
		super("RPG-Client");
	}

	public static void main(String[] args) {
		
		instance = new Client();
		AppGameContainer app;
		try {
			app = new AppGameContainer(get());
			app.setDisplayMode(800, 600, false);
			app.setTargetFrameRate(60);
			app.setAlwaysRender(true);
			UIManager.get().setGameContainer(app);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
}
