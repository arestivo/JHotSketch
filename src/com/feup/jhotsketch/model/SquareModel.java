package com.feup.jhotsketch.model;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public class SquareModel extends FigureModel{
	
	public SquareModel(int x, int y, int width, int height) {
		super(x, y, width, height);
	}

	@Override
	public FigureModel clone() {
		SquareModel clone = new SquareModel(bounds.x, bounds.y, bounds.width, bounds.height);
		return clone;
	}	
}