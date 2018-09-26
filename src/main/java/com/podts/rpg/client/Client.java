package com.podts.rpg.client;

import java.util.logging.Logger;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.chat.ChatManager;
import com.podts.rpg.client.model.HashWorld;
import com.podts.rpg.client.model.World;
import com.podts.rpg.client.network.NetworkManager;
import com.podts.rpg.client.state.LoginState;
import com.podts.rpg.client.state.PlayingState;

public class Client extends StateBasedGame {
	
	public static final int DEFAULT_PORT = 7000;
	private static Client instance;
	private static Logger logger = new GameLogger("Client");
	
	public static final Client get() {
		return instance;
	}
	
	public static final Logger getLogger() {
		return logger;
	}
	
	private final World world;
	private final NetworkManager networkManager = new NetworkManager();
	private final ChatManager chatManager = new ChatManager();
	
	public final World getWorld() {
		return world;
	}
	
	public final NetworkManager getNetworkManager() {
		return networkManager;
	}
	
	public final ChatManager getChatManager() {
		return chatManager;
	}
	
	@Override
	public void initStatesList(GameContainer gc) throws SlickException {
		addState(new LoginState());
		addState(new PlayingState(gc));
	}
	
	public void changeState(int id) {
		enterState(id);
	}
	
	Client() {
		super("RPG-Client");
		world = new HashWorld("Earth");
	}

	public static void main(String[] args) {
		
		instance = new Client();
		AppGameContainer app;
		try {
			app = new AppGameContainer(get());
			app.setDisplayMode(800,600,false);
			app.setTargetFrameRate(60);
			app.setMaximumLogicUpdateInterval(60);
			app.setAlwaysRender(true);
			app.setVerbose(true);
			app.start();
		} catch (SlickException e) {
			e.printStackTrace();
		}
		
	}
	
}
