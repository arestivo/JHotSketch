package com.feup.jhotsketch.model;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class Handle {
	private FigureModel figure;
	private int x;
	private int y;
	private static int size = 10;

	public Handle(FigureModel figure, int x, int y) {
		this.figure = figure;
		this.x = x;
		this.y = y;
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
		return new Rectangle(figure.getX() + x - size / 2, figure.getY() + y - size / 2, size, size);
	}

}