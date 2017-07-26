package com.podts.rpg.client.ui;

import java.util.List;

import com.podts.rpg.client.ui.UIObject.Corner;

public interface UIParent {
	
	public List<UIObject> getChildren();
	public int getWidth();
	public int getHeight();
	
	public UILocation getCorner(Corner c);
	
	public UIParent addChild(UIObject o);
	public UIParent removeChild(UIObject o);
	
}
