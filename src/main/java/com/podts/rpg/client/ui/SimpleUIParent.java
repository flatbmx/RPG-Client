package com.podts.rpg.client.ui;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public abstract class SimpleUIParent extends UIObject implements UIParent {
	
	public SimpleUIParent(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	public SimpleUIParent(int width, int height) {
		super(width, height);
	}

	private final List<UIObject> children = new LinkedList<UIObject>(), safeChildren = Collections.unmodifiableList(children);
	
	
	
	@Override
	public List<UIObject> getChildren() {
		return safeChildren;
	}
	
	public SimpleUIParent addChild(UIObject o) {
		children.add(o);
		o.parent = this;
		return this;
	}
	
	public SimpleUIParent removeChild(UIObject o) {
		if(children.remove(o)) o.parent = null;
		return this;
	}

	
}
