package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.World;
import com.podts.rpg.client.ui.UIManager;

public final class PlayingState extends UIState {
	
	private double zoom = 1;
	private boolean showGrid = true;
	
	private void drawWorld(GameContainer app, Graphics g) {
		
		World world = Client.get().getWorld();
		Location center = Player.me.getLocation();
		
		int cx = app.getWidth()/2;
		int cy = app.getHeight()/2;
		
		final int tileSize = (int) (32 * zoom);
		
		for(Tile tile : world.getTiles()) {
			Location tLoc = tile.getLocation();
			int dx = tLoc.getX() - center.getX();
			int dy = tLoc.getY() - center.getY();
			g.setColor(tile.getType().getColor());
			g.fillRect(dx*tileSize + cx,
					dy*tileSize + cy,
					tileSize,
					tileSize);
		}
		
		g.setLineWidth(1);
		g.setColor(Color.orange);
		g.drawRect(cx - tileSize, cy - tileSize, tileSize, tileSize);
		
		if(showGrid) {
			g.setColor(Color.gray);
			
		}
		
	}
	
	@Override
	public void enter(GameContainer app, StateBasedGame game) throws SlickException {
		UIManager.get().clearChildren();
		System.out.println("We are now playing as player " + Player.me.getID());
	}

	@Override
	public final int getID() {
		return States.PLAYING.getID();
	}
	
	@Override
	public void init(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
	}

	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		
	}

	@Override
	public void render(GameContainer app, StateBasedGame game, Graphics g) throws SlickException {
		drawWorld(app, g);
		super.render(app, game, g);
	}
	
	@Override
	public void mouseWheelMoved(int change) {
		zoom += zoom*change/4500;
	}
	
	@Override
	public void update(GameContainer app, StateBasedGame game, int delta) throws SlickException {
		
		
	}
	
}
