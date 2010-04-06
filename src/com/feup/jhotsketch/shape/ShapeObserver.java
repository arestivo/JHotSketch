package com.feup.jhotsketch.shape;

import com.feup.contribution.aida.annotations.PackageName;

@PackageName("Shape")
public interface ShapeObserver {
	public void shapeChanged(Shape shape);
}