package com.podts.rpg.client.ui;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public final class GraphicsHelper {
	
	static GraphicsHelper instance;
	
	public static final GraphicsHelper get() {
		return instance;
	}
	
	public static Color DEFAULT_COLOR = Color.black;
	public static float DEFAULT_LINE_WIDTH = 1f;
	
	private final GameContainer app;
	private final Graphics gr;
	
	private final Graphics getGraphics() {
		return gr;
	}
	
	public final GraphicsHelper drawLine(Graphics g, Color color, float x, float y, float ex, float ey, float lineWidth) {
		g.setColor(color);
		g.setLineWidth(lineWidth);
		g.drawLine(x, y, ex, ey);
		return this;
	}
	
	public final GraphicsHelper drawHorizontalLine(Graphics g, Color color, float x, float y, float length, float lineWidth) {
		return drawLine(g, color, x, y, x + length, y, lineWidth);
	}
	
	public final GraphicsHelper drawHorizontalLine(Graphics g, Color color, float x, float y, float length) {
		return drawHorizontalLine(g, color, x, y, length, DEFAULT_LINE_WIDTH);
	}
	
	public final GraphicsHelper drawHorizontalLine(float x, float y, float length) {
		return drawHorizontalLine(getGraphics(), DEFAULT_COLOR, x, y, length);
	}
	
	public final GraphicsHelper drawHorizontalLine(Color color, float y) {
		return drawHorizontalLine(getGraphics(), color, 0, y, app.getWidth());
	}
	
	public final GraphicsHelper drawHorizontalLine(float y) {
		return drawHorizontalLine(0, y, app.getWidth());
	}
	
	public final GraphicsHelper drawVerticalLine(Graphics g, Color color, float x, float y, float length, float lineWidth) {
		return drawLine(g, color, x, y, x, y + length, lineWidth);
	}
	
	public final GraphicsHelper drawVerticalLine(Graphics g, Color color, float x, float y, float length) {
		return drawVerticalLine(g, color, x, y, length, DEFAULT_LINE_WIDTH);
	}
	
	public final GraphicsHelper drawVerticalLine(float x, float y, float length) {
		return drawVerticalLine(getGraphics(), DEFAULT_COLOR, x, y, length);
	}
	
	public final GraphicsHelper drawVerticalLine(Color color, float x) {
		return drawVerticalLine(getGraphics(), color, x, 0, app.getWidth());
	}
	
	public final GraphicsHelper drawVerticalLine(float x) {
		return drawVerticalLine(x, 0, app.getWidth());
	}
	
	public final GraphicsHelper drawString(Graphics g, Color color, String string, float x, float y) {
		g.setColor(color);
		g.drawString(string, x, y);
		return this;
	}
	
	public final GraphicsHelper drawString(Color color, String string, float x, float y) {
		return drawString(getGraphics(), color, string, x, y);
	}
	
	public final GraphicsHelper drawString(String string, float x, float y) {
		return drawString(DEFAULT_COLOR, string, x, y);
	}
	
	GraphicsHelper(GameContainer app) {
		this.app = app;
		this.gr = app.getGraphics();
	}
	
}
