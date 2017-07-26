package com.podts.rpg.client.ui;

import java.util.List;

public interface UIParent {
	
	public List<UIObject> getChildren();
	public int getWidth();
	public int getHeight();
	
	public UIParent addChild(UIObject o);
	public UIParent removeChild(UIObject o);
	
}
