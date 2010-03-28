package com.feup.jhotsketch.model;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class RectangleModel extends ShapeModel{
	
	public RectangleModel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public ShapeModel clone() {
		RectangleModel clone = new RectangleModel(bounds.x, bounds.y, bounds.width, bounds.height);
		return clone;
	}	
}