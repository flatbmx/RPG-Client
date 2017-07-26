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

	public SimpleUIParent() {
		super();
	}

	private final LinkedList<UIObject> children = new LinkedList<UIObject>();
	private final List<UIObject> safeChildren = Collections.unmodifiableList(children);
	
	@Override
	public List<UIObject> getChildren() {
		return safeChildren;
	}
	
	public SimpleUIParent addChild(UIObject o) {
		children.addLast(o);
		o.parent = this;
		if(o.getWidth() == 0 || o.getHeight() == 0) {
			o.autoSizing = true;
		}
		autoSizeAll();
		return this;
	}
	
	public SimpleUIParent removeChild(UIObject o) {
		if(children.remove(o)) o.parent = null;
		autoSizeAll();
		return this;
	}
	
	private void autoSizeAll() {
		//TODO Get our shit together, all objects should be autoSized(X and/or Y) or static sizes.
		int x,y;
		for(UIObject child : children) {
			if(child.autoSizing) {
				int newWidth = getWidth() - child.getPaddingX()*2;
				int newHeight = getHeight() - child.getPaddingY()*2;
				child.setWidth(newWidth);
				child.setHeight(newHeight);
			}
		}
	}
	
}
