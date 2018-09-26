package com.podts.rpg.client.state;

import java.util.Collection;
import java.util.HashSet;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.chat.ChatMessage;
import com.podts.rpg.client.model.Entity;
import com.podts.rpg.client.model.Locatable;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.TileSelection;
import com.podts.rpg.client.model.World;
import com.podts.rpg.client.network.NetworkManager;
import com.podts.rpg.client.network.packet.EntityPacket;
import com.podts.rpg.client.network.packet.EntityPacket.UpdateType;
import com.podts.rpg.client.network.packet.MessagePacket;
import com.podts.rpg.client.ui.GraphicsHelper;
import com.podts.rpg.client.ui.UIManager;
import com.podts.rpg.client.ui.UIObject.MouseClickType;
import com.podts.rpg.client.ui.UITextBox;
import com.podts.rpg.client.ui.UIWindow;

public final class PlayingState extends UIState {
	
	private static Color TILE_HOVER_COLOR = Color.orange;
	private static final int DEFAULT_TILE_SIZE = 64;
	private static final int WALK_DELAY = 75;
	
	private double zoom = 1;
	private boolean showGrid = true;
	
	private float tileSize;
	
	private int ax, ay, centerX, centerY;
	
	private final GameContainer app;
	private final Graphics g;
	
	private long lastStep = 0;
	
	private Collection<TileSelection> tileSelections = new HashSet<>();
	
	private ChatWindow chatWindow;
	
	private final GameContainer getGameContainer() {
		return app;
	}
	
	private final Graphics getGraphics() {
		return g;
	}
	
	private final ChatWindow getChatWindow() {
		return chatWindow;
	}
	
	private float getTileSize() {
		return tileSize;
	}
	
	private class ChatWindow extends UIWindow {
		
		private class ChattingBox extends UITextBox {
			
			private ChatWindow getChatWindow() {
				return ChatWindow.this;
			}
			
			ChattingBox() {
				setWidth(getChatWindow().getWidth())
				.setHeight(14)
				.setX(0)
				.setY(getChatWindow().getHeight() - 14)	//14px above bottom of window, bottom row of lines of text.
				.setBackgroundColor(Color.red);
			}
			
		}
		
		private final ChattingBox chatBox = new ChattingBox();
		
		final boolean isChatting() {
			return !getChildren().isEmpty();
		}
		
		final ChatWindow setChatting(boolean isChatting) {
			if(isChatting) {
				if(!isChatting()) {
					addChild(chatBox);
					UIManager.get().setFocus(chatBox);
				}
			} else {
				if(!chatBox.getText().isEmpty()) {
					//Send to server
					Client.get().getNetworkManager().sendPacket(new MessagePacket(chatBox.getText()));
					chatBox.clear();
				}
				removeChild(chatBox);
				UIManager.get().setFocus(null);
			}
			return this;
		}
		
		@Override
		public void render(GameContainer app, Graphics g) {
			//Draws chat messages from bottom to top.
			int y = getBottomY();
			
			//Move up 12 for chat input.
			//Give horizontal line 1 px of padding
			y -= 15;
			GraphicsHelper.get().drawHorizontalLine(y);
			y -= 4;
			
			//Draw all messages
			synchronized(Client.get().getChatManager()) {
				for(ChatMessage message : Client.get().getChatManager().getMessages()) {
					y -= 13;
					g.setColor(Color.yellow);
					g.drawString(message.getText(), 0, y);
				}
			}
			
			//Draws the currentlyChatting window if need be
			drawChildren(app, g);
			super.render(app, g);
		}
		
		private Color createAlphaBackgroundColor() {
			return new Color(UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR.getRed()
					, UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR.getGreen()
					, UIManager.DEFAULT_WINDOW_BACKGROUND_COLOR.getBlue()
					, 50);
		}
		
		ChatWindow() {
			super(getGameContainer().getWidth(), 200);
			setY(getGameContainer().getHeight() - 200);
			setBackgroundColor(createAlphaBackgroundColor());
			setBorderColor(getBackgroundColor());
			setCompactable(false);
		}
		
	}
	
	private void drawWorld() {
		
		World world = Client.get().getWorld();
		
		synchronized(world) {
			for(Tile tile : world.getTiles()) {
				drawTile(tile);
			}
			for(Entity entity : world.getEntities()) {
				drawEntity(entity);
			}
		}
		
		if(isShowingGrid()) {
			drawGrid();
		}
		
		
		drawEntity(Player.me.getPlayerEntity());
		
		drawSelectedTiles();
		
		highlightTileLocation(getHoveringTileLocation());
		
		//Draw UI windows including possibly chat window.
		UIManager.get().render(getGameContainer(), getGraphics());
		
	}
	
	private boolean isShowingGrid() {
		return showGrid;
	}
	
	private void drawGrid() {
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
	
	private void drawSelectedTiles() {
		
	}
	
	private void highlightTile(Tile tile) {
		highlightTileLocation(tile.getLocation());
	}	

	private void highlightTileLocation(Location point, Color color) {
		g.setLineWidth(1);
		g.setColor(color);
		g.drawRect(getLocationDisplayX(point), getLocationDisplayY(point), getTileSize(), getTileSize());
	}
	
	private void highlightTileLocation(Location point) {
		highlightTileLocation(point, TILE_HOVER_COLOR);
	}
	
	private void crossTileLocation(Location point) {
		crossTileLocation(point, Color.orange);
	}
	
	private void crossTileLocation(Location point, Color color) {
		g.setLineWidth(1);
		g.setColor(color);
		float x = getLocationDisplayX(point);
		float y = getLocationDisplayY(point);
		g.drawLine(x, y, x + getTileSize(), y + getTileSize());
		g.drawLine(x + getTileSize(), y, x, y + getTileSize());
	}
	
	private void drawTile(Tile tile) {
		g.setColor(tile.getType().getColor());
		g.fillRect(getLocationDisplayX(tile),
				getLocationDisplayY(tile),
				getTileSize(),
				getTileSize());
	}
	
	private void drawEntity(Entity entity) {
		crossTileLocation(entity.getLocation(), Color.red);
		g.drawString(entity.getName(), getLocationDisplayX(entity), getLocationDisplayY(entity));
	}
	
	private float getLocationDisplayX(Locatable loc) {
		return (loc.getLocation().getX() - Player.me.getLocation().getX()) * getTileSize() + centerX - getTileSize()/2;
	}
	
	private float getLocationDisplayY(Locatable loc) {
		return (loc.getLocation().getY() - Player.me.getLocation().getY()) * getTileSize() + centerY - getTileSize()/2;
	}
	
	private Location getHoveringTileLocation() {
		int mx = app.getInput().getMouseX();
		int my = app.getInput().getMouseY();
		
		float tx = mx - centerX - getTileSize()/2;
		float ty = my - centerY - getTileSize()/2;
		
		tx /= getTileSize();
		ty /= getTileSize();
		
		tx += Player.me.getLocation().getX();
		ty += Player.me.getLocation().getY();
		
		return new Location((int)Math.ceil(tx),(int)Math.ceil(ty), Player.me.getLocation().getZ());
	}
	
	private void setZoom(double zoom) {
		this.zoom = zoom;
		tileSize = (float) (DEFAULT_TILE_SIZE * zoom);
	}

	@Override
	public final int getID() {
		return States.PLAYING.getID();
	}
	
	@Override
	public void init(GameContainer app, StateBasedGame game) throws SlickException {
		
	}
	
	@Override
	public void enter(GameContainer app, StateBasedGame game) throws SlickException {
		UIManager.get().setCompactable(false);
		UIManager.get().clear();
		ax = app.getWidth();
		ay = app.getHeight();
		centerX = ax/2;
		centerY = ay/2;
		setZoom(1);
		System.out.println("We are now playing as player " + Player.me.getID());
		
		chatWindow = new ChatWindow();
		UIManager.get().addChild(getChatWindow());
	}
	
	@Override
	public void leave(GameContainer arg0, StateBasedGame arg1) throws SlickException {
		UIManager.get().clear();
		UIManager.get().setCompactable(true);
	}

	@Override
	public void render(GameContainer app, StateBasedGame game, Graphics g) throws SlickException {
		drawWorld();
		super.render(app, game, g);
	}
	
	@Override
	public void mouseWheelMoved(int change) {
		setZoom(zoom + zoom*change/4500);
	}
	
	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		
	}
	
	@Override
	public void onMouseClick(MouseClickType type, int x, int y) {
		if(type.equals(MouseClickType.LEFT_CLICK)) {
			
		} else if(MouseClickType.MIDDLE_CLICK.equals(type)) {
			setZoom(1);
		}
	}
	
	private boolean isChatting() {
		return getChatWindow().isChatting();
	}
	
	@Override
	public void update(GameContainer app, StateBasedGame game, int delta) throws SlickException {
		
		Input input = app.getInput();
		
		if(input.isKeyPressed(Input.KEY_ESCAPE)) {
			NetworkManager.logout();
			return;
		}
		
		//TODO is this handled by UIManager with focused textbox?
		if(input.isKeyPressed(Input.KEY_ENTER)) {
			getChatWindow().setChatting(!getChatWindow().isChatting());
		}
		
		if(canWalk()) {
			if(app.getInput().isKeyDown(Input.KEY_UP)) {
				movePlayer(Direction.UP);
			} else if(app.getInput().isKeyDown(Input.KEY_DOWN)) {
				movePlayer(Direction.DOWN);
			} else if(app.getInput().isKeyDown(Input.KEY_LEFT)) {
				movePlayer(Direction.LEFT);
			} else if(app.getInput().isKeyDown(Input.KEY_RIGHT)) {
				movePlayer(Direction.RIGHT);
			}
		}
		
	}
	
	private boolean canWalk() {
		return System.currentTimeMillis() > lastStep + WALK_DELAY;
	}
	
	@Override
	public void keyPressed(int key, char c) {
		if(key == Input.KEY_SPACE) {
			if(!isChatting()) {
				showGrid = !showGrid;
			}
		}
		if(c == '/') {
			if(!getChatWindow().isChatting()) {
				getChatWindow().setChatting(!getChatWindow().isChatting());
			}
		}
		super.keyPressed(key, c);
	}
	
	private void movePlayer(Direction dir) {
		lastStep = System.currentTimeMillis();
		Location newLocation = dir.MoveFromLocation(Player.me.getLocation());
		EntityPacket packet = new EntityPacket(UpdateType.UPDATE, Player.me.getPlayerEntity().getID(), newLocation);
		Client.get().getNetworkManager().getStream().sendPacket(packet);
	}
	
	public PlayingState(GameContainer app) {
		this.app = app;
		this.g = app.getGraphics();
	}
	
}
