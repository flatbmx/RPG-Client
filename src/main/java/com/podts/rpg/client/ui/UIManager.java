package com.podts.rpg.client.ui;

import java.util.LinkedList;
import java.util.List;

import org.newdawn.slick.GameContainer;

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
	
	public List<UIObject> getUObjects() {
		return uiObjects;
	}
	
	public UIManager addChild(UIObject newObject) {
		if(!uiObjects.contains(newObject))
			uiObjects.add(newObject);
		return this;
	}
	
	public UIManager removeChild(UIObject object) {
		uiObjects.remove(object);
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
		// TODO Auto-generated method stub
		return null;
	}
	
}
