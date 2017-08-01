package com.podts.rpg.client.ui;

import java.util.Collections;
import java.util.Iterator;
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
		if(o.getWidth() == 0 || o.getHeight() == 0) o.autoSizing = true;
		compact();
		return this;
	}
	
	public SimpleUIParent removeChild(UIObject o) {
		if(!children.remove(o)) return this;
		o.parent = null;
		if(o.autoSizing) o.autoSizing = false;
		compact();
		return this;
	}
	
	/*private void compact() {
		//TODO Get our shit together, all objects should be autoSized(X and/or Y) or static sizes.
		int total = children.size();
		int totalHeight = getHeight() - UIObject.DEFAULT_PADDING * (total + 1);
		int eachHeight = totalHeight / total;
		nextY = UIObject.DEFAULT_PADDING;
		for(UIObject child : children) {
			if(child.autoSizing) {
				int newWidth = getWidth() - child.getPaddingX()*2;
				child.setWidth(newWidth);
				child.setHeight(eachHeight);
				child.setY(nextY);
				nextY += UIObject.DEFAULT_PADDING;
			}
			
		}
	}*/
	
	private void compact() {
		if(children.isEmpty()) return;
		int totalHeight = getHeight();
		Iterator<UIObject> it = children.iterator();
		UIObject ch = it.next();
		totalHeight -= ch.getHeight() + ch.getPaddingY();
		
		while(it.hasNext()) {
			ch = it.next();
			totalHeight -= ch.getPaddingY() * 2 + ch.getHeight();
		}
		int eachHeight = totalHeight / children.size();
		
		int nextY = 0;
		
		for(UIObject child : children) {
			if(child.autoSizing) {
				if(child.getHeight() == 0) {
					child.setHeight(eachHeight);
				}
				if(child.getWidth() == 0) {
					child.setWidth(getWidth() - child.getPaddingX() * 2);
				}
				
			}
			
			if(child.isCenterX()) {
				int extraRoom = (getWidth() - child.getWidth()) / 2;
				child.setX(child.getPaddingX() + extraRoom);
			} else {
				child.setX(child.getPaddingX());
			}
			
			if(child.isCenterY() && children.size() == 1) {
				int extraRoom = (getHeight() - child.getHeight()) / 2;
				child.setY(child.getPaddingY() + extraRoom);
			} else {
				child.setY(child.getPaddingY() + nextY);
			}
			
			nextY += child.getPaddingY() + child.getHeight();
		}
	}
	
}
