package com.podts.rpg.client.ui;

import java.util.LinkedList;
import java.util.List;

import com.podts.rpg.client.ui.UIObject.MouseClickType;

public final class UIManager {
	
	private static final List<UIObject> uiObjects = new LinkedList<UIObject>();
	
	public static List<UIObject> getUObjects() {
		return uiObjects;
	}
	
	public static void addObject(UIObject newObject) {
		if(uiObjects.contains(newObject)) return;
		uiObjects.add(newObject);
	}
	
	public static void removeObject(UIObject object) {
		uiObjects.remove(object);
	}
	
	public static boolean handleMouseClick(MouseClickType type, int x, int y) {
		for(UIObject obj : uiObjects) {
			if(obj.isIn(x, y)) {
				obj.handleMouseClick(type);
				return true;
			}
		}
		return false;
	}

	public static void clear() {
		uiObjects.clear();
	}
	
}
