package com.feup.jhotsketch.model;

import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class Handle {
	private FigureModel figure;
	private int x;
	private int y;
	private int ox;
	private int oy;
	private static int size = 10;
	
	public Handle(FigureModel figure, int x, int y, int ox, int oy) {
		this.figure = figure;
		this.x = x;
		this.y = y;
		this.ox = ox;
		this.oy = oy;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getX() {
		return x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	public int getY() {
		return y;
	}

	public static int getSize() {
		return size;
	}

	public Rectangle getBounds() {
		return new Rectangle(figure.bounds.x + x - size / 2, figure.bounds.y + y - size / 2, size, size);
	}
	
	public boolean contains(int x, int y) {
		return getBounds().contains(x, y);
	}

	public Point getOppositePoint() {
		return new Point(figure.getBounds().x + ox, figure.getBounds().y + oy);
	}
}