package com.podts.rpg.client.ui;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class UITable extends SimpleUIParent {
	
	private final UIObject[][] grid;
	private final int[] columnWidths;
	private final int[] rowHeights;
	
	public UITable(int x, int y, int width, int height, int columns, int rows) {
		super(x, y, width, height);
		grid = new UIObject[columns][rows];
		columnWidths = new int[columns];
		rowHeights = new int[rows];
	}
	
	public UITable(int width, int height, int columns, int rows) {
		super(width, height);
		grid = new UIObject[columns][rows];
		columnWidths = new int[columns];
		rowHeights = new int[rows];
	}
	
	public UITable(int columns, int rows) {
		super();
		grid = new UIObject[columns][rows];
		columnWidths = new int[columns];
		rowHeights = new int[rows];
	}
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		for(UIObject child : getChildren()) {
			child.render(gc, g);
		}
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
	
	public UITable addChild(UIObject o, int column, int row) {
		grid[column][row] = o;
		super.addChild(o);
		return this;
	}
	
	@Override
	public UITable removeChild(UIObject o) {
		removeGridChild(o);
		super.removeChild(o);
		return this;
	}
	
	@Override
	protected void compact() {
		
		//Column width
		for(int c=0; c<columnWidths.length; ++c) {
			int width = 0;
			for(int r=0; r<rowHeights.length; ++r) {
				UIObject o = grid[c][r];
				if(o == null) continue;
				width = Math.max(width, o.getWidth() + o.getPaddingY());
			}
			columnWidths[c] = width;
		}
		
		//Row height
		for(int r=0; r<rowHeights.length; ++r) {
			int height = 0;
			for(int c=0; c<columnWidths.length; ++c) {
				UIObject o = grid[c][r];
				if(o == null) continue;
				height = Math.max(height, o.getHeight() + o.getPaddingY());
			}
			rowHeights[r] = height;
		}
		
		int nextY = 0;
		for(int r=0; r < rowHeights.length; ++r) {
			int nextX = 0;
			for(int c=0; c < columnWidths.length; ++c) {
				UIObject o = grid[c][r];
				if(o != null) {
					o.setX(nextX);
					o.setY(nextY);
				}
				nextX += columnWidths[c];
			}
			nextY += rowHeights[r];
		}
		
	}
	
}
