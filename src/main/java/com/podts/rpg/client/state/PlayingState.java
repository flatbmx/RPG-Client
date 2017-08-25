package com.podts.rpg.client.state;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.model.Locatable;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.World;
import com.podts.rpg.client.network.packet.EntityPacket;
import com.podts.rpg.client.network.packet.EntityPacket.UpdateType;
import com.podts.rpg.client.ui.UIManager;

public final class PlayingState extends UIState {
	
	private double zoom = 1;
	private boolean showGrid = true;
	
	private float tileSize;
	
	private int ax, ay, cx, cy;
	
	private void drawWorld(GameContainer app, Graphics g) {
		
		World world = Client.get().getWorld();
		
		
		for(Tile tile : world.getTiles()) {
			drawTile(tile, g);
		}
		
		if(showGrid) {
			drawGrid(g);
		}
		
		g.setLineWidth(1);
		g.setColor(Color.orange);
		g.drawRect(cx - tileSize + tileSize/2, cy - tileSize + tileSize/2, tileSize, tileSize);
		
	}
	
	private void drawGrid(Graphics g) {
		g.setLineWidth(1);
		g.setColor(new Color(100,100,100));
		int columns = (int) (ax / tileSize + 1);
		int rows = (int) (ay / tileSize + 1);
		
		for(int i=0; i<columns; ++i) {
			Location point = Player.me.getLocation().move(-columns/2 + i, 0, 0);
			g.drawLine(getLocationDisplayX(point), 0, getLocationDisplayX(point), ay);
		}
		
		for(int i=0; i<rows; ++i) {
			Location point = Player.me.getLocation().move(0, -rows/2 + i, 0);
			g.drawLine(0, getLocationDisplayY(point), ax, getLocationDisplayY(point));
		}
		
	}
	
	private void drawTile(Tile tile, Graphics g) {
		g.setColor(tile.getType().getColor());
		g.fillRect(getLocationDisplayX(tile),
				getLocationDisplayY(tile),
				tileSize,
				tileSize);
	}
	
	private float getLocationDisplayX(Locatable loc) {
		return (loc.getLocation().getX() - Player.me.getLocation().getX()) * tileSize + cx + tileSize/2;
	}
	
	private float getLocationDisplayY(Locatable loc) {
		return (loc.getLocation().getY() - Player.me.getLocation().getY()) * tileSize + cy + tileSize/2;
	}
	
	@Override
	public void enter(GameContainer app, StateBasedGame game) throws SlickException {
		UIManager.get().clearChildren();
		ax = app.getWidth();
		ay = app.getHeight();
		cx = ax/2;
		cy = ay/2;
		tileSize = (float) (32 * zoom);
		System.out.println("We are now playing as player " + Player.me.getID());
	}

	@Override
	public final int getID() {
		return States.PLAYING.getID();
	}
	
	@Override
	public void init(GameContainer app, StateBasedGame game) throws SlickException {
		
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
		tileSize = (float) (32 * zoom);
	}
	
	@Override
	public void update(GameContainer app, StateBasedGame game, int delta) throws SlickException {
		
		
	}
	
	@Override
	public void keyPressed(int key, char c) {
		super.keyPressed(key, c);
		if(key == Input.KEY_SPACE) {
			showGrid = !showGrid;
			return;
		}
		
		if(key == Input.KEY_UP) {
			movePlayer(Direction.UP);
			return;
		} else if(key == Input.KEY_DOWN) {
			movePlayer(Direction.DOWN);
			return;
		} else if(key == Input.KEY_LEFT) {
			movePlayer(Direction.LEFT);
			return;
		} else if(key == Input.KEY_RIGHT) {
			movePlayer(Direction.RIGHT);
			return;
		}
		
	}
	
	private void movePlayer(Direction dir) {
		Location newLocation = dir.MoveFromLocation(Player.me.getLocation());
		EntityPacket packet = new EntityPacket(UpdateType.UPDATE, Player.me.getPlayerEntity().getID(), newLocation);
		Client.get().getNetworkManager().getStream().sendPacket(packet);
	}
	
}
