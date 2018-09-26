package com.podts.rpg.client.ui;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

import com.podts.rpg.client.Client;

/**
 * A class that manages the entire Games user interface.
 *
 */
public final class UIManager extends SimpleUIParent {
	
	private static UIManager instance;
	
	public static final UIManager get() {
		if(instance == null)
			instance = new UIManager();
		return instance;
	}
	
	private final List<UIObject> uiObjects = new LinkedList<UIObject>();
	private GameContainer gc;
	private UIObject focus;
	
	public static final TrueTypeFont DEFAULT_FONT = new TrueTypeFont(new Font("Lucida Console", Font.BOLD, 12), true);
	public static final Color DEFAULT_FONT_COLOR = new Color(255,255,255);
	
	public static final Color DEFAULT_WINDOW_BORDER_COLOR = new Color(100,100,100);
	public static final Color DEFAULT_WINDOW_BACKGROUND_COLOR = new Color(150,150,150);
	
	public List<UIObject> getUObjects() {
		return uiObjects;
	}
	
	public UIObject getFocus() {
		return focus;
	}
	
	public UIManager setFocus(UIObject o) {
		if(focus != null)
			focus.focused = false;
		
		focus = o;
		
		if(o != null)
			o.focused = true;
		return this;
	}
	
	public String getFocusCharacter(UIObject o) {
		if(!o.focused)
			return "";
		if(System.currentTimeMillis() % 750 < 375)
			return UITextBox.DEFAULT_FOCUS_CHARACTER;
		return "";
	}
	
	/**
	 * Handles a mouse click event and will pass the event down to children if they are clicked in.
	 * @param type - The type of mouse click.
	 * @param x - The x position of the mouse relative to the game window.
	 * @param y - The y position of the mouse relative to the game window.
	 * @return True if a UIObject was clicked on, false otherwise.
	 */
	public boolean handleMouseClick(MouseClickType type, int x, int y) {
		for(UIObject obj : getChildren()) {
			if(obj.isIn(x, y)) {
				if(!obj.handleMouseClick(type, x, y)) {
					if(focus != null) {
						focus.focused = false;
						focus = null;
					}
				}
				return true;
			}
		}
		if(focus != null) {
			focus.focused = false;
			focus = null;
		}
		return false;
	}
	
	public void handleKeyPress(int key) {
		if(key == 14) {
			UIObject o = UIManager.get().getFocus();
			if(o instanceof UITextBox) {
				UITextBox box = (UITextBox) o;
				box.handleBackSpace();
			}
		}
	}
	
	public void handleTextInput(char c) {
		if(c != 0) {
			UIObject o = UIManager.get().getFocus();
			if(o instanceof UITextBox) {
				UITextBox box = (UITextBox) o;
				box.handleTextInput(String.valueOf(c));
			}
		}
	}
	
	public UIManager setGameContainer(GameContainer newContainer) {
		gc = newContainer;
		GraphicsHelper.instance = new GraphicsHelper(newContainer);
		gc.getGraphics().setFont(DEFAULT_FONT);
		return this;
	}
	
	public final void clear() {
		clearChildren();
	}
	
	@Override
	public int getWidth() {
		return gc.getWidth();
	}

	@Override
	public int getHeight() {
		return gc.getHeight();
	}

	@Override
	public UILocation getCorner(Corner c) {
		return new UILocation(getWidth()*c.x,getHeight()*c.y);
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		for(UIObject obj : UIManager.get().getChildren()) {
			g.setClip(obj.getX(), obj.getY(), obj.getWidth(), obj.getHeight());
			obj.render(gc, g);
		}
		g.setClip(0, 0, gc.getWidth(), gc.getHeight());
		g.setColor(Color.white);
		g.drawString("mx " + gc.getInput().getMouseX() + ", my " + gc.getInput().getMouseY(), 0, 0);
	}
	
	UIManager() {
		
	}
	
}
