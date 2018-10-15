package com.podts.rpg.client.ui;

import java.util.Objects;

public class UILocation implements Cloneable {
	
	private final int x, y;
	
	public UILocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
	
	public UILocation shift(int dx, int dy) {
		return new UILocation(getX() + dx, getY() + dy);
	}
	
	public UILocation shift(UILocation other) {
		return shift(other.getX(), other.getY());
	}
	
	@Override
	public final String toString() {
		return "[" + x + ", " + y + "]";
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getX(), getY());
	}
	
	@Override
	public boolean equals(Object o) {
		if(o == null) return false;
		if(o == this) return true;
		if(o instanceof UILocation) {
			UILocation other = (UILocation) o;
			return getX() == other.getX()
					&& getY() == other.getY();
		}
		return false;
	}
	
	@Override
	public UILocation clone() {
		return new UILocation(getX(), getY());
	}
}
