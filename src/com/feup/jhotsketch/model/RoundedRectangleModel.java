package com.feup.jhotsketch.model;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class RoundedRectangleModel extends ShapeModel{
	private int radius = 15;
	
	public RoundedRectangleModel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public ShapeModel clone() {
		RoundedRectangleModel clone = new RoundedRectangleModel(bounds.x, bounds.y, bounds.width, bounds.height);
		return clone;
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}	
}