package com.podts.rpg.client.ui;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;

import com.podts.rpg.client.ui.UIObject.Corner;
import com.podts.rpg.client.ui.UIObject.MouseClickType;

/**
 * A class that manages the entire Games user interface.
 *
 */
public final class UIManager implements UIParent {
	
	private static UIManager instance;
	
	public static final UIManager get() {
		if(instance == null)
			instance = new UIManager();
		return instance;
	}
	
	private final List<UIObject> uiObjects = new LinkedList<UIObject>();
	private GameContainer gc;
	
	public static final TrueTypeFont DEFAULT_FONT = new TrueTypeFont(new Font("Courier",Font.BOLD,12), true);
	public static final Color DEFAULT_FONT_COLOR = new Color(255,255,255);
	
	public static final Color DEFAULT_WINDOW_BORDER_COLOR = new Color(100,100,100);
	public static final Color DEFAULT_WINDOW_BACKGROUND_COLOR = new Color(150,150,150);
	
	public List<UIObject> getUObjects() {
		return uiObjects;
	}
	
	
	public UIManager addChild(UIObject newObject) {
		if(!uiObjects.contains(newObject)) {
			uiObjects.add(newObject);
			newObject.parent = this;
			if(newObject.getWidth() == 0 || newObject.getHeight() == 0) {
				newObject.autoSizing = true;
			}
		}
		return this;
	}
	
	public UIManager removeChild(UIObject object) {
		if(uiObjects.remove(object)) {
			object.parent = null;
			if(object.autoSizing) {
				object.autoSizing = false;
			}
		}
		return this;
	}
	
	/**
	 * Handles a mouse click event and will pass the event down to children if they are clicked in.
	 * @param type - The type of mouse click.
	 * @param x - The x position of the mouse relative to the game window.
	 * @param y - The y position of the mouse relative to the game window.
	 * @return True if a UIObject was clicked on, false otherwise.
	 */
	public boolean handleMouseClick(MouseClickType type, int x, int y) {
		for(UIObject obj : uiObjects) {
			if(obj.isIn(x, y)) {
				obj.handleMouseClick(type);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Clears all child UIObjects.
	 * @return The UIManager for chaining.
	 */
	public UIManager clear() {
		uiObjects.clear();
		return this;
	}

	@Override
	public List<UIObject> getChildren() {
		return uiObjects;
	}
	
	public UIManager setGameContainer(GameContainer newContainer) {
		gc = newContainer;
		return this;
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
		return new UILocation(getWidth()*(c.x+1),getHeight()*(c.y+1));
	}
	
}
