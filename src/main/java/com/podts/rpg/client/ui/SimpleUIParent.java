package com.podts.rpg.client.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleUIParent extends UIObject implements UIParent {
	
	public SimpleUIParent(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	private final List<UIObject> children = new LinkedList<UIObject>(), safeChildren = Collections.unmodifiableList(children);
	
	@Override
	public List<UIObject> getChildren() {
		return safeChildren;
	}
	
	public SimpleUIParent addChild(UIObject o) {
		children.add(o);
		return this;
	}
	
	public SimpleUIParent removeChild(UIObject o) {
		children.remove(o);
		return this;
	}
}
