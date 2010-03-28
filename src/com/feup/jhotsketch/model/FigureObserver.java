package com.feup.jhotsketch.model;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Model")
public interface FigureObserver {
	public void shapeChanged(ShapeModel shape);
}
