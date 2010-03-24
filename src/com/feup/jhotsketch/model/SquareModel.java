package com.feup.jhotsketch.model;

import org.eclipse.swt.graphics.Rectangle;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class SquareModel extends FigureModel{
	private int width;
	private int height;	
	
	public SquareModel(int x, int y, int width, int height) {
		super(x, y);
		this.setWidth(width);
		this.setHeight(height);
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getWidth() {
		return width;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getHeight() {
		return height;
	}

	@Override
	public Rectangle getBounds() {
		return new Rectangle(getX(), getY(), width, height);
	}

	@Override
	public FigureModel clone() {
		SquareModel clone = new SquareModel(x, y, width, height);
		return clone;
	}	
}