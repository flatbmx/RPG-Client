package com.podts.rpg.client.ui;

import java.awt.Font;
import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.TrueTypeFont;

import com.podts.rpg.client.ui.UIObject.Corner;
import com.podts.rpg.client.ui.UIObject.MouseClickType;

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
		}
		return this;
	}
	
	public UIManager removeChild(UIObject object) {
		if(uiObjects.remove(object)) object.parent = null;
		return this;
	}
	
	public boolean handleMouseClick(MouseClickType type, int x, int y) {
		for(UIObject obj : uiObjects) {
			if(obj.isIn(x, y)) {
				obj.handleMouseClick(type);
				return true;
			}
		}
		return false;
	}

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
