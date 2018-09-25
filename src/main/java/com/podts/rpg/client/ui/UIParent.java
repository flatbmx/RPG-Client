package com.podts.rpg.client.ui;

import java.util.List;
import java.util.stream.Stream;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

import com.podts.rpg.client.ui.UIObject.Corner;

public interface UIParent {
	
	/**
	 * Returns a Unmodifiable-List of all the children of this parent.
	 * Children cannot be changed from this method, to do so you 
	 * must call {@link #addChild(UIObject)} or {@link #removeChild(UIObject)}.
	 * @return The list of all the children.
	 */
	public List<UIObject> getChildren();
	
	public default Stream<UIObject> children() {
		return getChildren().stream();
	}
	
	public int getWidth();
	public int getHeight();
	
	public UILocation getCorner(Corner c);
	
	/**
	 * Sets the given UIObject as a child.
	 * If the UIObject already has a parent then this method will do nothing.
	 * This will set the UIObjects parent to this.
	 * @param o - The UIObject that should be added as a child.
	 * @return The UIParent for chaining.
	 */
	public UIParent addChild(UIObject o);
	
	/**
	 * Removes the given UIObject as a child.
	 * This will set the UIObjects parent to null afterwards.
	 * If this UIObject is not a child to this parent then nothing will happen.
	 * @param o - The UIObject that should be removed as a child.
	 * @return The UIParent for chaining.
	 */
	public UIParent removeChild(UIObject o);
	
	public default UIParent drawChildren(GameContainer gc, Graphics g) {
		children()
		.forEach(child -> child.render(gc, g));
		return this;
	}
	
	public UIParent setCompactable(boolean compact);
	
}
