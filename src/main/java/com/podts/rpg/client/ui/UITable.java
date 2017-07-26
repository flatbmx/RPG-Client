package com.podts.rpg.client.ui;

import java.util.List;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UITable extends SimpleUIParent {
	
	private final UIObject[][] grid;
	
	public UITable(int x, int y, int width, int height, int rows, int columns) {
		super(x, y, width, height);
		grid = new UIObject[rows][columns];
	}

	@Override
	public void render(GameContainer gc, Graphics g) {
		// TODO Auto-generated method stub

	}
	
	private void removeGridChild(UIObject o) {
		for(int i=0; i < grid.length; ++i) {
			for(int j=0; j < grid.length; ++j) {
				if(o.equals(grid[i][j])) {
					grid[i][j] = null;
					return;
				}
			}
		}
	}
	
	@Override
	public UITable addChild(UIObject o) {
		super.addChild(o);
		return this;
	}
	
	public UITable addChild(UIObject o, int row, int coloumn) {
		super.addChild(o);
		grid[row][coloumn] = o;
		return this;
	}
	
	@Override
	public UITable removeChild(UIObject o) {
		super.removeChild(o);
		removeGridChild(o);
		return this;
	}

}
