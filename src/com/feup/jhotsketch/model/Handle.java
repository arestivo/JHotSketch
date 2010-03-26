package com.feup.jhotsketch.model;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class Handle {
	private FigureModel figure;
	private int x;
	private int y;
	private static int size = 10;
	private int id;

	public final static int NW = 1;
	public final static int NE = 2;
	public final static int SE = 3;
	public final static int SW = 4;
	
	public Handle(FigureModel figure, int x, int y, int id) {
		this.figure = figure;
		this.x = x;
		this.y = y;
		this.setId(id);
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

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}
}