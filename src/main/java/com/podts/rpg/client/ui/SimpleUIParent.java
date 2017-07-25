package com.podts.rpg.client.ui;

import java.util.LinkedList;
import java.util.List;

public abstract class SimpleUIParent extends UIObject implements UIParent {
	
	public SimpleUIParent(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	private final List<UIObject> children = new LinkedList<UIObject>();
	
	@Override
	public List<UIObject> getChildren() {
		return children;
	}

}
