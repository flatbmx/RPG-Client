package com.podts.rpg.client.state;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Optional;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import com.podts.rpg.client.Client;
import com.podts.rpg.client.chat.ChatMessage;
import com.podts.rpg.client.model.Entity;
import com.podts.rpg.client.model.Locatable;
import com.podts.rpg.client.model.Location;
import com.podts.rpg.client.model.Location.Direction;
import com.podts.rpg.client.model.path.ListPath;
import com.podts.rpg.client.model.path.Path;
import com.podts.rpg.client.model.Player;
import com.podts.rpg.client.model.Tile;
import com.podts.rpg.client.model.World;
import com.podts.rpg.client.network.NetworkManager;
import com.podts.rpg.client.network.packet.EntityPacket;
import com.podts.rpg.client.network.packet.EntityPacket.UpdateType;
import com.podts.rpg.client.network.packet.MessagePacket;
import com.podts.rpg.client.network.packet.TileSelectionPacket;
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
	
	private Collection<Tile> selectedTiles = new HashSet<>();
	private LinkedList<Tile> walkingPath = new LinkedList<>();
	
	private ChatWindow chatWindow;
	
	private Location currentHoveringLocation;
	
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
				.setHeight(15)
				.setX(0)
				.setY(getChatWindow().getHeight() - 15)	//14px above bottom of window, bottom row of lines of text.
				.setBackgroundColor(Color.red);
			}
			
		}
		
		private final ChattingBox chatBox = new ChattingBox();
		private ListIterator<String> entryIterator;
		
		final boolean isChatting() {
			return !getChildren().isEmpty();
		}
		
		final ChatWindow setChatText(String text) {
			chatBox.setText(text);
			return this;
		}
		
		final ChatWindow toggleChatting() {
			setChatting(!isChatting());
			return this;
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
					String entry = chatBox.getText();
					Client.get().getNetworkManager().sendPacket(new MessagePacket(entry));
					Client.get().getChatManager().addEntry(entry);
					entryIterator = null;
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
			y -= 16;
			GraphicsHelper.get().drawHorizontalLine(y);
			y -= 4;
			
			//Draw all messages
			synchronized(Client.get().getChatManager()) {
				for(ChatMessage message : Client.get().getChatManager().getMessages()) {
					y -= 13;
					g.setColor(Color.red);
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
	
	private Collection<? extends Tile> getSelectedTiles() {
		return selectedTiles;
	}
	
	private LinkedList<Tile> getWalkingPath() {
		return walkingPath;
	}
	
	private ListIterator<Tile> walkingIterator() {
		return getWalkingPath().listIterator();
	}
	
	private Tile getLastStep() {
		Tile last = getWalkingPath().getLast();
		if(last == null)
			last = Client.get().getWorld().getTile(Player.me.getLocation());
		return last;
	}
	
	private World getWorld() {
		return Client.get().getWorld();
	}
	
	private Tile getTile(Location loc) {
		return getWorld().getTile(loc);
	}
	
	public void addStep(Direction dir) {
		Tile latest = getLastStep();
		walkingPath.addLast(getTile(latest.getLocation().shift(dir)));
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
		
		highlightTileLocation(findHoveringLocation());
		
		drawWalkingPath();
		
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
	
	private void drawWalkingPath() {
		if(getWalkingPath().isEmpty())
			return;
		Locatable prev = Player.me.getLocation();
		Iterator<Tile> it = walkingIterator();
		while(it.hasNext()) {
			Tile next = it.next();
			getGraphics().drawLine(getLocationDisplayCenterX(prev)
					, getLocationDisplayCenterY(prev)
					, getLocationDisplayCenterX(next)
					, getLocationDisplayCenterY(next));
			prev = next;
		}
	}
	
	private void drawSelectedTiles() {
		for(Tile tile : getSelectedTiles()) {
			highlightTile(tile);
		}
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
		if(tile.isVoid()) {
			drawColorTile(tile);
		} else {
			drawImageTile(tile);
		}
	}
	
	private void drawImageTile(Tile tile) {
		Image tileImage = Client.get().getResources().getImage(tile.getType().name().toLowerCase());
		int size = (int)Math.ceil(getTileSize());
		Image scaled = tileImage.getScaledCopy(size, size);
		g.drawImage(scaled, getLocationDisplayX(tile),
				getLocationDisplayY(tile));
	}
	
	private void drawColorTile(Tile tile) {
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
	
	private float getLocationDisplayCenterX(Locatable loc) {
		return getLocationDisplayX(loc) + getTileSize()/2;
	}
	
	private float getLocationDisplayY(Locatable loc) {
		return (loc.getLocation().getY() - Player.me.getLocation().getY()) * getTileSize() + centerY - getTileSize()/2;
	}
	
	private float getLocationDisplayCenterY(Locatable loc) {
		return getLocationDisplayY(loc) + getTileSize()/2;
	}
	
	private Tile findHoveringTile() {
		return Client.get().getWorld().getTile(findHoveringLocation());
	}
	
	private Location findHoveringLocation() {
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
	
	private Location getHoveringLocation() {
		return currentHoveringLocation;
	}
	
	private void setZoom(double zoom) {
		this.zoom = zoom;
		tileSize = (float) (DEFAULT_TILE_SIZE * zoom);
		onTilePositionChange();
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
		clearSelectedTiles();
		UIManager.get().clear();
		UIManager.get().setCompactable(true);
		Client.get().getNetworkManager().close();
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
	public void mouseMoved(int oldX, int oldY, int newX, int newY) {
		onTilePositionChange();
	}
	
	public void onTilePositionChange() {
		Location oldHover = getHoveringLocation();
		Location newHover = findHoveringLocation();
		if(!newHover.equals(oldHover)) {
			currentHoveringLocation = newHover;
			onHoverLocationChange(oldHover, newHover);
		}
	}
	
	/**
	 * Called when the mouse is hovering over a new location than before.
	 * @param oldLoc - the old location that the mouse was hovering over
	 * @param newLoc - the new location  that the mouse is currently hovering over
	 */
	private void onHoverLocationChange(Location oldLoc, Location newLoc) {
		
		Optional<ListPath> path = Client.get().getWorld().getPath(Player.me, newLoc);
		walkingPath.clear();
		if(path.isPresent()) {
			walkingPath.addAll(path.get().getTiles());
		}
		
	}
	
	@Override
	public void mouseDragged(int oldX, int oldY, int newX, int newY) {
		
	}
	
	@Override
	public void mouseReleased(int button, int x, int y) {
		
	}
	
	public void selectTile(Tile tile) {
		if(selectedTiles.add(tile)) {
			Client.get().getNetworkManager().sendPacket(new TileSelectionPacket(getSelectedTiles()));
		}
	}
	
	public void clearSelectedTile(boolean update) {
		selectedTiles.clear();
		if(update)
			sendServerSelectedTiles();
	}
	
	public void clearSelectedTiles() {
		clearSelectedTile(false);
	}
	
	public void setSelectedTiles(Collection<Tile> tiles) {
		clearSelectedTiles();
		selectedTiles.addAll(tiles);
	}
	
	private final void sendServerSelectedTiles() {
		Client.get().getNetworkManager().sendPacket(new TileSelectionPacket(getSelectedTiles()));
	}
	
	@Override
	public void onMouseClick(MouseClickType type, int x, int y) {
		if(type.equals(MouseClickType.LEFT_CLICK)) {
			Tile tile = findHoveringTile();
			if(tile != null) {
				if(selectedTiles.contains(tile)) {
					selectedTiles.remove(tile);
				} else {
					selectTile(tile);
				}
			}
		} else if(MouseClickType.RIGHT_CLICK.equals(type)) {
			clearSelectedTiles();
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
		
		if(input.isKeyPressed(Input.KEY_ENTER)) {
			getChatWindow().toggleChatting();
		}
		
		//Move Player with Arrow Keys or WASD if not Chatting
		
		if(!isChatting()) {
			if(input.isKeyDown(Input.KEY_UP) || input.isKeyDown(Input.KEY_W)) {
				movePlayer(Direction.UP);
			} else if(input.isKeyDown(Input.KEY_DOWN) || input.isKeyDown(Input.KEY_S)) {
				movePlayer(Direction.DOWN);
			} else if(input.isKeyDown(Input.KEY_LEFT) || input.isKeyDown(Input.KEY_A)) {
				movePlayer(Direction.LEFT);
			} else if(input.isKeyDown(Input.KEY_RIGHT) || input.isKeyDown(Input.KEY_D)) {
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
				return;
			}
		}
		if(isChatting()) {
			ChatWindow w = getChatWindow();
			if(key == Input.KEY_UP) {
				if(w.entryIterator == null) {
					w.entryIterator = Client.get().getChatManager().getRecentEntriesIterator();
				}
				if(w.entryIterator.hasNext()) {
					w.setChatText(w.entryIterator.next());
				}
				return;
			}
			if(key == Input.KEY_DOWN) {
				if(w.entryIterator != null) {
					if(w.entryIterator.hasPrevious()) {
						if(!w.entryIterator.hasNext()) {
							w.entryIterator.previous();
							if(w.entryIterator.hasPrevious())
								w.setChatText(w.entryIterator.previous());
						} else
							w.setChatText(w.entryIterator.previous());
					} else {
						w.entryIterator = null;
						w.chatBox.clear();
					}
				}
				return;
			}
		}
		if(c == '/') {
			if(!isChatting()) {
				getChatWindow().toggleChatting();
			}
		}
		super.keyPressed(key, c);
	}
	
	private void movePlayer(Direction dir) {
		if(!canWalk())
			return;
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
